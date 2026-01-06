package com.liq.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ровный интерфейс без XML
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 100, 60, 60);

        final EditText user = new EditText(this);
        user.setHint("Username");
        
        final EditText pass = new EditText(this);
        pass.setHint("Password");

        Button btn = new Button(this);
        btn.setText("CONNECT TO LIQ");

        layout.addView(user);
        layout.addView(pass);
        layout.addView(btn);
        setContentView(layout);

        // Логика отправки на твой IP
        btn.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    URL url = new URL("http://80.74.29.21:8080/register");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    
                    String json = "{\"username\":\"" + user.getText().toString() + "\", \"password\":\"" + pass.getText().toString() + "\"}";
                    
                    OutputStream os = conn.getOutputStream();
                    os.write(json.getBytes());
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Status: " + responseCode, Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }).start();
        });
    }
}
