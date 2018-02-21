package com.tokopedia.abstraction.base.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.webview.TkpdWebView;


public abstract class BaseWebViewFragment extends BaseDaggerFragment {
    private static final int MAX_PROGRESS = 100;

    private TkpdWebView webView;
    private ProgressBar progressBar;

    /**
     * return the url to load in the webview
     * You can use URLGenerator.java to use generate the seamless URL.
     */
    protected abstract String getUrl();

    /**
     * this is to put in header with key X-User-ID when the webview loadUrl
     * fill with blank or NULL if authorization header is not needed.
     */
    @Nullable
    protected abstract String getUserIdForHeader();

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        webView = (TkpdWebView) view.findViewById(R.id.webview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        return view;
    }

    protected int getLayout(){
        return R.layout.fragment_general_web_view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setIndeterminate(true);

        if(!TextUtils.isEmpty(getUrl())) {
            loadWeb();
        }
    }

    private void loadWeb() {
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == MAX_PROGRESS) {
                    onLoadFinished();
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.loadAuthUrlWithFlags(getUrl(), getUserIdForHeader());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return BaseWebViewFragment.this.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

    protected void onLoadFinished(){
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
