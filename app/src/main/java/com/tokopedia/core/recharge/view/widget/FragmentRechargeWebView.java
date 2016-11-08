package com.tokopedia.core.recharge.view.widget;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.core.R;

/**
 * @author by kulomady on 7/22/2016.
 */
public class FragmentRechargeWebView extends Fragment {
    private static final String TAG = "FragmentRechargeWebView";
    private static final String RECHARGE_HOME_URL = "https://pulsa.tokopedia.com/";
    private static final String ACTION_KEY = "action";
    private static final String ACTION_EDIT_VALUE = "edit_data";
    private ProgressBar progressBar;
    private WebView webviewRecharge;

    private class MyWebViewClient extends WebChromeClient {
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

    private class RechargeWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try {
                getActivity().setProgressBarIndeterminateVisibility(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlString) {
            Log.d(TAG, "shouldOverrideUrlLoading() called with: " + "view = [" + view + "], urlString = [" + urlString + "]");
            if (URLUtil.isValidUrl(urlString)) {
                if (RECHARGE_HOME_URL.equals(urlString)) {
                    getActivity().finish();
                } else {
                    Uri uri = Uri.parse(urlString);
                    String action = uri.getQueryParameter(ACTION_KEY);
                    if (ACTION_EDIT_VALUE.equals(action)) getActivity().finish();
                }
            }

            return false;
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            progressBar.setVisibility(View.GONE);
        }
    }


    public FragmentRechargeWebView() {
    }

    public static FragmentRechargeWebView createInstance(String url) {
        FragmentRechargeWebView fragment = new FragmentRechargeWebView();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        getActivity().setProgressBarIndeterminateVisibility(false);
        getActivity().finish();
        super.onDestroyView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_general_web_view, container, false);
        String url = getArguments().getString("url", RECHARGE_HOME_URL);
        webviewRecharge = (WebView) view.findViewById(R.id.webview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        clearCache(webviewRecharge);
        webviewRecharge.loadUrl(url);
        webviewRecharge.setWebViewClient(new RechargeWebClient());
        webviewRecharge.setWebChromeClient(new MyWebViewClient());
        getActivity().setProgressBarIndeterminateVisibility(true);
        WebSettings webSettings = webviewRecharge.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        optimizeWebView();
        CookieManager.getInstance().setAcceptCookie(true);
        return view;
    }

    private void clearCache(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    public WebView getWebviewRecharge() {
        return webviewRecharge;
    }

    @SuppressLint("unused")
    public void setWebviewRecharge(WebView webviewRecharge) {
        this.webviewRecharge = webviewRecharge;
    }

    private void optimizeWebView() {
        if (Build.VERSION.SDK_INT >= 19) {
            webviewRecharge.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webviewRecharge.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
}
