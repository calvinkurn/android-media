package com.tokopedia.imagepicker.picker.instagram.view.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.instagram.InstagramConstant;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public class InstagramLoginDialog extends BottomSheets {

    private ListenerLoginInstagram listenerLoginInstagram;
    private WebView instagramWebview;
    private ProgressBar progressBar;

    @Override
    protected String title() {
        return getString(R.string.instagram_label_login);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.layout_instagram_login;
    }

    @Override
    public void initView(View view) {
        instagramWebview = view.findViewById(R.id.webview_instagram);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        WebSettings ws = instagramWebview.getSettings();
        ws.setDomStorageEnabled(true);
        ws.setAppCacheEnabled(false);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setSaveFormData(false);
        ws.setSavePassword(false);
        ws.setJavaScriptEnabled(true);
        instagramWebview.setWebViewClient(new InstagramWebViewClient());

        instagramWebview.setWebChromeClient(new MyWebChromeClient());
        instagramWebview.loadUrl(InstagramConstant.URL_LOGIN_INSTAGRAM);
        instagramWebview.clearCache(true);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        updateHeight(screenHeight/2);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void setListenerLoginInstagram(ListenerLoginInstagram listenerLoginInstagram) {
        this.listenerLoginInstagram = listenerLoginInstagram;
    }

    public interface ListenerLoginInstagram {
        void onSuccessLogin(String code);
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (newProgress == 100) {
                    view.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    getActivity().setProgressBarIndeterminateVisibility(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    private class InstagramWebViewClient extends WebViewClient {

        public InstagramWebViewClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("?code")) {
                Uri codeUri = Uri.parse(url);
                String code = codeUri.getQueryParameter("code");
                if (listenerLoginInstagram != null) {
                    listenerLoginInstagram.onSuccessLogin(code);
                    dismiss();
                } else
                    throw new RuntimeException("Listener is null");
                view.stopLoading();
            } else {
                view.loadUrl(url);
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            progressBar.setVisibility(View.GONE);
        }


        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
        }

    }
}
