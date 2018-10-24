package com.tokopedia.kelontongapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.tokopedia.kelontongapp.firebase.Preference;
import com.tokopedia.kelontongapp.helper.ConnectionManager;
import com.tokopedia.kelontongapp.webview.KelontongWebChromeClient;
import com.tokopedia.kelontongapp.webview.FilePickerInterface;
import com.tokopedia.kelontongapp.webview.KelontongWebview;
import com.tokopedia.kelontongapp.webview.KelontongWebviewClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by meta on 02/10/18.
 */
public class KelontongMainActivity extends AppCompatActivity
        implements FilePickerInterface {

    private static final String GCM_ID = "gcm_id";
    private static final String ANDROID = "tkpd/mitra/android";
    private static final String MOBILE = "mobile";
    private static final String X_REQUESTED_WITH = "X-Requested-With";
    private static final int EXIT_DELAY_MILLIS = 2000;

    private KelontongWebChromeClient webViewChromeClient;
    private KelontongWebviewClient webviewClient;
    private KelontongWebview webView;

    private boolean doubleTapExit = false;

    private Map<String, String> headers = new HashMap<>();

    public static Intent start(Context context) {
        return new Intent(context, KelontongMainActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadWebViewPage();
    }

    private void loadWebViewPage() {
        if (ConnectionManager.isNetworkConnected(this)) {
            setContentView(R.layout.activity_main_kelontong);
            initializeWebview();

            if (Preference.isFirstTime(this)) {
                requestPermission();
                Preference.saveFirstTime(this);
            }
        } else {
            noInternetConnection();
        }
    }

    private void noInternetConnection() {
        setContentView(R.layout.activity_no_internet);
        Button btnTryAgain = findViewById(R.id.btn_retry);
        btnTryAgain.setOnClickListener(v -> loadWebViewPage());
    }

    private void initializeWebview() {
        webView = findViewById(R.id.webview);

        webViewChromeClient = new KelontongWebChromeClient(this, this);
        webviewClient = new KelontongWebviewClient(this);

        headers.put(X_REQUESTED_WITH, "");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(webViewChromeClient);
        webView.setWebViewClient(webviewClient);

        webView.getSettings().setAllowFileAccess(true);

        if(Build.VERSION.SDK_INT >= 21){
            webView.getSettings().setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else if(Build.VERSION.SDK_INT >= 19){
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        String userAgent = System.getProperty("http.agent");
        userAgent += String.format(" %s-%s %s", ANDROID, BuildConfig.VERSION_NAME, MOBILE);
        webView.getSettings().setUserAgentString(userAgent);

        String fcmToken = Preference.getFcmToken(this);
        CookieManager.getInstance().setCookie(KelontongBaseUrl.COOKIE_URL, String.format("%s=%s", GCM_ID, fcmToken));
        webView.loadUrl(KelontongBaseUrl.BASE_URL, headers);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!webviewClient.checkPermission()) {
                webviewClient.requestPermission();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (isHome()) {
                        onBackPressed();
                    } else if (webView.canGoBack()) {
                        webView.goBack();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isHome() {
        return webView.getUrl().equalsIgnoreCase(KelontongBaseUrl.BASE_URL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        webviewClient.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KelontongWebChromeClient.ATTACH_FILE_REQUEST && webViewChromeClient != null) {
            webViewChromeClient.onActivityResult(requestCode, resultCode, data);
        }
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
