package com.tokopedia.kelontongapp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tokopedia.kelontongapp.webview.KelontongWebChromeClient;
import com.tokopedia.kelontongapp.webview.FilePickerInterface;
import com.tokopedia.kelontongapp.webview.KelontongWebview;
import com.tokopedia.kelontongapp.webview.KelontongWebviewClient;

/**
 * Created by meta on 02/10/18.
 */
public class KelontongMainActivity extends AppCompatActivity implements FilePickerInterface {

    private static final int EXIT_DELAY_MILLIS = 2000;

    private KelontongWebChromeClient webViewClient;
    private KelontongWebview webView;

    private boolean doubleTapExit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kelontong);

        webView = findViewById(R.id.webview);
        ProgressBar progressBar = findViewById(R.id.progress);

        webViewClient = new KelontongWebChromeClient(this, progressBar);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(webViewClient);
        webView.setWebViewClient(new KelontongWebviewClient());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        webView.loadUrl(KelontongBaseUrl.BASE_URL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KelontongWebChromeClient.ATTACH_FILE_REQUEST && webViewClient != null) {
            webViewClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        onBackPressed();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        doubleTapExit();
    }

    private void doubleTapExit() {
        if (doubleTapExit) {
            this.finish();
        } else {
            doubleTapExit = true;
            Toast.makeText(this, R.string.msg_exit, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleTapExit = false, EXIT_DELAY_MILLIS);
        }
    }

}
