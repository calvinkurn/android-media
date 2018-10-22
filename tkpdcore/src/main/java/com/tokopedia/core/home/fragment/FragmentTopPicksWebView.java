
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
import com.tokopedia.core2.R;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.TkpdWebView;

/**
 * Created by Alifa on 1/10/2017.
 */

public class FragmentTopPicksWebView extends Fragment {

    private ProgressBar progressBar;
    private TkpdWebView webview;
    private static final String TOP_PICKS_URL = "https://www.tokopedia.com/toppicks/";
    private static final String BASE_URL = "www.tokopedia.com";
    private static final String BASE_MOBILE_URL = "m.tokopedia.com";

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
            CommonUtils.dumper("DEEPLINK " + errorCode + "  " + description + " " + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
        }

    }

    void webViewLoadComplete( WebView wv ){
        wv.clearAnimation();
        wv.clearDisappearingChildren();
        wv.destroyDrawingCache();
        wv.freeMemory();
    }


    public FragmentTopPicksWebView() {
    }

    public static FragmentTopPicksWebView createInstance(String url) {
        FragmentTopPicksWebView fragment = new FragmentTopPicksWebView();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    private boolean overrideUrl(String url) {

            if (((Uri.parse(url).getHost().contains(BASE_URL))
                    || Uri.parse(url).getHost().contains(BASE_MOBILE_URL))
                    && !url.endsWith(".pl")) {
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
                    default:
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
        String url = getArguments().getString("url", TOP_PICKS_URL);
        webview = (TkpdWebView) view.findViewById(R.id.webview);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        clearCache(webview);
        webview.loadAuthUrlWithFlags(url);
        webview.setWebViewClient(new FragmentTopPicksWebView.MyWebClient());
        webview.setWebChromeClient(new FragmentTopPicksWebView.MyWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
            webview.setWebContentsDebuggingEnabled(true);
            CommonUtils.dumper("webviewconf debugging = true");
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
