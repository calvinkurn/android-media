package com.tokopedia.imagepicker.picker.instagram.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.instagram.di.InstagramModule;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramConstant;
import com.tokopedia.imagepicker.picker.instagram.view.presenter.InstagramLoginPresenter;
import com.tokopedia.imagepicker.picker.instagram.di.DaggerInstagramComponent;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.instagram.util.InstagramConstant.SESSIONID;

/**
 * Created by zulfikarrahman on 6/8/18.
 */

public class InstagramLoginFragment extends BaseDaggerFragment{

    @Inject
    InstagramLoginPresenter instagramLoginPresenter;
    private WebView instagramWebview;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_instagram_login, container, false);
        initView(view);
        return view;
    }

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
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerInstagramComponent
                .builder()
                .instagramModule(new InstagramModule())
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
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
                Intent intent = new Intent();
                intent.putExtra(InstagramConstant.EXTRA_CODE_LOGIN, code);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                view.stopLoading();
            } else {
                view.loadUrl(url);
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String cookies = CookieManager.getInstance().getCookie(url);
            if (!TextUtils.isEmpty(cookies) && cookies.contains(SESSIONID)) {
                instagramLoginPresenter.saveCookies(cookies);
            }

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
