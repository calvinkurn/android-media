package com.tokopedia.kelontongapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by meta on 02/10/18.
 */
public class KelontongMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kelontong);

        WebView webView = findViewById(R.id.webview);
        webView.loadUrl("https://www.google.com/");
    }
}
