package com.tokopedia.core.home.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.util.TkpdWebView;

/**
 * Created by Nisie on 8/25/2015.
 */
public class FragmentBannerWebView extends Fragment {

    private ProgressBar progressBar;
    private TkpdWebView webview;

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

    private class MyWebClient extends WebViewClient {
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
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return overrideUrl(url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            progressBar.setVisibility(View.GONE);
        }


        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            CommonUtils.dumper("DEEPLINK " + errorCode + "  " + description + " " + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
        }

    }


    public FragmentBannerWebView() {
    }

    public static FragmentBannerWebView createInstance(String url) {
        FragmentBannerWebView fragment = new FragmentBannerWebView();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    private boolean overrideUrl(String url) {
        if (TrackingUtils.getBoolean(AppEventTracking.GTM.OVERRIDE_BANNER)) {

            if (((Uri.parse(url).getHost().contains("www.tokopedia.com"))
                    || Uri.parse(url).getHost().contains("m.tokopedia.com"))
                    && !url.endsWith(".pl")) {
                switch ((DeepLinkChecker.getDeepLinkType(url))) {
                    case DeepLinkChecker.BROWSE:
                        DeepLinkChecker.openBrowse(url, getActivity());
                        return true;
                    case DeepLinkChecker.HOT:
                        DeepLinkChecker.openHot(url, getActivity());
                        return true;
                    case DeepLinkChecker.CATALOG:
                        DeepLinkChecker.openCatalog(url, getActivity());
                        return true;
                    case DeepLinkChecker.PRODUCT:
                        DeepLinkChecker.openProduct(url, getActivity());
                        return true;
                    case DeepLinkChecker.SHOP:
                        ((BannerWebView) getActivity()).openShop(url);
                        return true;
                    default:
                        return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        getActivity().setProgressBarIndeterminateVisibility(false);
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_general_web_view, container, false);
        System.out.println("KIRISAME use URL: " + getArguments().getString("url", "http://blog.tokopedia.com"));
        String url = getArguments().getString("url", "http://blog.tokopedia.com");
        webview = (TkpdWebView) view.findViewById(R.id.webview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        clearCache(webview);
        webview.loadAuthUrl(url);
        webview.setWebViewClient(new MyWebClient());
        webview.setWebChromeClient(new MyWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
            webview.setWebContentsDebuggingEnabled(true);
            CommonUtils.dumper("webviewconf debugging = true");
        }
        getActivity().setProgressBarIndeterminateVisibility(true);
        WebSettings webSettings = webview.getSettings();
        webSettings.setDomStorageEnabled(true);
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


    public WebView getWebview() {
        return webview;
    }

    public void setWebview(TkpdWebView webview) {
        this.webview = webview;
    }

    private void optimizeWebView() {
        if (Build.VERSION.SDK_INT >= 19) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
}
