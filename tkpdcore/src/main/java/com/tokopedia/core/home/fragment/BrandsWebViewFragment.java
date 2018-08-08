package com.tokopedia.core.home.fragment;

import android.app.Fragment;
import android.content.Intent;
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

import com.tokopedia.core.R;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.TkpdWebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by brilliant.oka on 15/03/17.
 */

public class BrandsWebViewFragment extends Fragment {
    private static final String EXTRA_URL = "url";
    private static final String FORMAT_UTF_8 = "UTF-8";
    private static final String KEYWORD_PL_SUFFIX = ".pl";
    private static final String KEYWORD_LOGIN = "login";
    private static final String KEYWORD_OFFICIAL_STORE = "tokopedia.com/official-store";
    private static final String KEYWORD_APPAUTH = "appauth";
    private static final String FLAG_APP = "?flag_app=1";

    private TkpdWebView webview;
    private ProgressBar progressBar;

    public static BrandsWebViewFragment createInstance(String url) {
        BrandsWebViewFragment fragment = new BrandsWebViewFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public BrandsWebViewFragment() {
    }

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

        @Override
        public void onPageFinished(WebView wv, String url) {
            webViewLoadComplete(wv);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_general_web_view, container, false);
        String url = getArguments().getString(EXTRA_URL, TkpdBaseURL.OfficialStore.URL_WEBVIEW);
        webview = (TkpdWebView) view.findViewById(R.id.webview);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        clearCache(webview);
        webview.setWebViewClient(new BrandsWebViewFragment.MyWebClient());
        webview.setWebChromeClient(new BrandsWebViewFragment.MyWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        WebSettings webSettings = webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        optimizeWebView();
        CookieManager.getInstance().setAcceptCookie(true);
        webview.loadAuthUrlWithFlags(
                URLGenerator.generateURLSessionLoginV4(
                        encodeUrl(url), getActivity()
                )
        );
        return view;
    }

    private void clearCache(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    private void optimizeWebView() {
        if (Build.VERSION.SDK_INT >= 19) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public TkpdWebView getWebview() {
        return webview;
    }

    public void setWebview(TkpdWebView webview) {
        this.webview = webview;
    }

    void webViewLoadComplete(WebView wv) {
        wv.clearAnimation();
        wv.clearDisappearingChildren();
        wv.destroyDrawingCache();
    }

    private boolean overrideUrl(String url) {
        if ((Uri.parse(url).getHost().contains(Uri.parse(TkpdBaseURL.WEB_DOMAIN).getHost()) ||
                Uri.parse(url).getHost().contains(Uri.parse(TkpdBaseURL.MOBILE_DOMAIN).getHost()))
                && !url.endsWith(KEYWORD_PL_SUFFIX)
                && !url.contains(KEYWORD_LOGIN)
                && !url.contains(KEYWORD_OFFICIAL_STORE)
                && !url.contains(KEYWORD_APPAUTH)) {
            switch ((DeepLinkChecker.getDeepLinkType(url))) {
                case DeepLinkChecker.BROWSE:
                    DeepLinkChecker.openBrowse(url, getActivity());
                    return true;
                case DeepLinkChecker.HOT:
                    DeepLinkChecker.openHot(url, getActivity());
                    return true;
                case DeepLinkChecker.HOT_LIST:
                    DeepLinkChecker.openHomepage(getActivity(), HomeRouter.INIT_STATE_FRAGMENT_HOTLIST);
                    return true;
                case DeepLinkChecker.CATALOG:
                    DeepLinkChecker.openCatalog(url, getActivity());
                    return true;
                case DeepLinkChecker.PRODUCT:
                    DeepLinkChecker.openProduct(url, getActivity());
                    return true;
                case DeepLinkChecker.SHOP:
                    DeepLinkChecker.openShop(url, getActivity());
                    return true;
                case DeepLinkChecker.ETALASE:
                    Intent intent = new Intent(getActivity(), BannerWebView.class);
                    intent.putExtra(BannerWebView.EXTRA_URL, url + FLAG_APP);
                    startActivity(intent);
                    return true;
                case DeepLinkChecker.PROMO:
                    intent = new Intent(getActivity(), BannerWebView.class);
                    intent.putExtra(BannerWebView.EXTRA_URL, url + FLAG_APP);
                    startActivity(intent);
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private String encodeUrl(String url) {
        String encodedUrl;
        try {
            encodedUrl = URLEncoder.encode(url, FORMAT_UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return encodedUrl;
    }
}
