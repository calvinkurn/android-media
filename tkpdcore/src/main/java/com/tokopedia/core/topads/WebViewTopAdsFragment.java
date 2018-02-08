package com.tokopedia.core.topads;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.V2BaseFragment;
import com.tokopedia.core.var.TkpdUrl;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WebViewTopAdsFragment extends V2BaseFragment {

    @BindView(R2.id.scroll_view)
    ScrollView mainView;

    @BindView(R2.id.webview)
    WebView webView;

    public static final String SOURCE_EXTRA = "SOURCE";
    public static final String TAG = WebViewTopAdsFragment.class.getSimpleName();
    public static WebViewTopAdsFragment newInstance(String source) {
        
        Bundle args = new Bundle();
        args.putString(SOURCE_EXTRA, source);
        WebViewTopAdsFragment fragment = new WebViewTopAdsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    public WebViewTopAdsFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getRootViewId() {
        return R.layout.fragment_web_view_top_ads;
    }

    @Override
    protected void onCreateView() {
        ButterKnife.bind(this,getRootView());
        clearCache(webView);
        String url = TkpdUrl.INFO_TOPADS;
        Uri uri = Uri.parse(url);
        Uri.Builder uriBuilder = uri.buildUpon();
        uriBuilder.appendQueryParameter("campaign", "topads");
        uriBuilder.appendQueryParameter("source", getArguments().getString(SOURCE_EXTRA));
        uriBuilder.appendQueryParameter("medium", "android");
        String newUrl = uriBuilder.toString();
        Log.d(TAG, "url "+newUrl);
        webView.loadUrl(newUrl);
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new MyWebViewClient());
        getActivity().setProgressBarIndeterminateVisibility(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
    }

    @Override
    protected Object getHolder() {
        return null;
    }

    @Override
    protected void setHolder(Object holder) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }

    private void clearCache(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
        }
    }
    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (newProgress == 100) {
                    view.setVisibility(View.VISIBLE);
                    getActivity().setProgressBarIndeterminateVisibility(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try {
                getActivity().setProgressBarIndeterminateVisibility(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            CommonUtils.dumper("DEEPLINK " + errorCode + "  " + description + " " + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mainView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainView.smoothScrollTo(0, 0);
                }
            }, 100);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // https://stackoverflow.com/questions/4229494/webview-link-click-open-default-browser
            // this is to enable deeplink from "Mulai Promo TopAds" to the app
            if (url != null && ( url.startsWith("http://") || url.startsWith("https://") )) {
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } else {
                return false;
            }
        }
    }

    public WebViewTopAdsActivity.BackButtonListener getOnBackPressedListener() {
        return new WebViewTopAdsActivity.BackButtonListener() {
            @Override
            public void onBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }

            @Override
            public boolean canGoBack() {
                return webView.canGoBack();
            }
        };
    }
}
