package com.tokopedia.kelontongapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tokopedia.kelontongapp.widget.CommonWebViewClient;
import com.tokopedia.kelontongapp.widget.FilePickerInterface;
import com.tokopedia.kelontongapp.widget.KelontongWebview;

/**
 * Created by meta on 02/10/18.
 */
public class KelontongMainActivity extends AppCompatActivity implements FilePickerInterface {

    private static final int EXIT_DELAY_MILLIS = 2000;

    private CommonWebViewClient webViewClient;
    private KelontongWebview webView;

    private boolean doubleTapExit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kelontong);

        webView = findViewById(R.id.webview);
        ProgressBar progressBar = findViewById(R.id.progress);

        webViewClient = new CommonWebViewClient(this, progressBar);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(webViewClient);

        webView.loadUrl(KelontongBaseUrl.URL_BASE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonWebViewClient.ATTACH_FILE_REQUEST && webViewClient != null) {
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
