package com.HackZone.TargetApp.Controller;

import jakarta.persistence.EntityManager; // 1. Import essentiel
import jakarta.persistence.PersistenceContext; // 2. Pour l'injection
import jakarta.persistence.Query; // 3. Attention : Jakarta Persistence, pas Management !
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // 4. Import du Model
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class VulnerableController {

    // --- CORRECTION 1 : On injecte l'EntityManager ici ---
    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/")
    public String loginPage(){
        return "login";
    }

    // --- CORRECTION 2 : On ajoute "Model model" dans les parenthèses ---
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model){

        String sql = "SELECT * FROM Users WHERE username = '" + username + "' and password = '" + password + "'";

        System.out.println("Requete exécute " + sql);

        try {
            Query query = entityManager.createNativeQuery(sql);
            List result = query.getResultList();

            if(!result.isEmpty()){
                Object[] userRow = (Object[]) result.get(0);
                // Attention aux index selon ta base de données (1 = username, 3 = secret ?)
                String name = (String) userRow[1];
                String secret = (String) userRow[3];

                model.addAttribute("username" , name);
                model.addAttribute("secret", secret);
                // Assure-toi que ton fichier s'appelle bien "dashboard.html" (minuscule recommandée)
                return "dashboard";

            } else {
                // --- CORRECTION 3 : Ajout du point-virgule manquant ---
                model.addAttribute("error", "Identifiants incorrects");
                return "login";
            }
        } catch(Exception e) {
            model.addAttribute("error", "Erreur SQL : " + e.getMessage());
            return "login";
        }
    }
}