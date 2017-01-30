package com.tokopedia.core.shopinfo.fragment;

/**
 * Created by nakama on 02/12/16.
 */

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.tokopedia.core.R2;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.models.GetShopProductParam;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by Erry Suprayogi on 18/11/16.
 *         modified by Mr. Alvarisi
 */

public class OfficialShopHomeFragment extends Fragment {

    private static final String TAG = OfficialShopHomeFragment.class.getSimpleName();
    public static final String SHOP_URL = "SHOP_URL";

    private OfficialShopInteractionListener mOfficialShopInteractionListener;

    public interface OfficialShopInteractionListener {
        void OnProductListPageRedirected(GetShopProductParam productParam);

        void OnProductInfoPageRedirected(String productId);

        void OnWebViewPageRedirected(String url);
    }

    private class OfficialStoreWebChromeClient extends WebChromeClient {
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

    private class OfficialStoreWebViewClient extends WebViewClient {
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
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return overrideUrl(url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            Log.d(TAG, "URL " + url);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            CommonUtils.dumper("DEEPLINK " + errorCode + "  " + description + " " + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
        }

    }

    public static OfficialShopHomeFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(SHOP_URL, url);
        OfficialShopHomeFragment fragment = new OfficialShopHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R2.id.webview)
    WebView webView;
    @BindView(R2.id.progress)
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        getActivity().setProgressBarIndeterminateVisibility(false);
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_official_shop_home, container, false);
        ButterKnife.bind(this, parentView);
        return parentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setIndeterminate(true);
        String url = getArguments().getString(SHOP_URL);
        CommonUtils.dumper("webviewconf URL address " + url);
        webView.loadUrl(url);
        webView.setWebViewClient(new OfficialStoreWebViewClient());
        webView.setWebChromeClient(new OfficialStoreWebChromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
            CommonUtils.dumper("webviewconf debugging = true");
        }
        getActivity().setProgressBarIndeterminateVisibility(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        optimizeWebView();
        CookieManager.getInstance().setAcceptCookie(true);
    }

    private void clearCache(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    private void optimizeWebView() {
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private boolean overrideUrl(String url) {
        Uri uri = Uri.parse(url);
        if (uri.getScheme().equals("tokopedia")) {
            List<String> paths = uri.getPathSegments();
            if (paths.size() > 1) {
                switch (paths.get(1)) {
                    case "etalase":
                        String id = uri.getLastPathSegment();
                        GetShopProductParam getShopProductParam = buildProductListParameter(uri);
                        getShopProductParam.setEtalaseId(id);
                        mOfficialShopInteractionListener.OnProductListPageRedirected(getShopProductParam);
                        break;
                    case "product":
                        String productId = uri.getLastPathSegment();
                        mOfficialShopInteractionListener.OnProductInfoPageRedirected(productId);
                        break;
                    case "page":
                        String page = uri.getLastPathSegment();
                        GetShopProductParam getShopProductParam1 = buildProductListParameter(uri);
                        if (page != null) {
                            getShopProductParam1.setPage(Integer.parseInt(page));
                        }
                        mOfficialShopInteractionListener.OnProductListPageRedirected(getShopProductParam1);
                        break;
                }
            } else {
                GetShopProductParam getShopProductParam = buildProductListParameter(uri);
                mOfficialShopInteractionListener.OnProductListPageRedirected(getShopProductParam);
            }
        } else if (uri.getScheme().startsWith("http")) {
            mOfficialShopInteractionListener.OnWebViewPageRedirected(url);
        }
        return true;
    }

    @NonNull
    private GetShopProductParam buildProductListParameter(Uri uri) {
        Set<String> parameterNames = uri.getQueryParameterNames();
        GetShopProductParam getShopProductParam = new GetShopProductParam();
        for (String parameterName : parameterNames) {
            switch (parameterName) {
                case "sort":
                    getShopProductParam.setOrderBy(uri.getQueryParameter(parameterName));
                    break;
                case "keyword":
                    getShopProductParam.setKeyword(uri.getQueryParameter(parameterName));
                    break;
                case "page":
                    getShopProductParam.setPage(Integer.parseInt(uri.getQueryParameter(parameterName)));
                    break;
            }
        }
        return getShopProductParam;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null)
            webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null)
            webView.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (webView != null) {
            if (!isVisibleToUser) {
                webView.onPause();
            } else {
                webView.onResume();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OfficialShopInteractionListener) {
            mOfficialShopInteractionListener = (OfficialShopInteractionListener) context;
        } else {
            throw new RuntimeException("must implement OfficialShopInteractionListener");
        }
    }
}