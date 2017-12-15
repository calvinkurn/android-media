package com.tokopedia.core.shopinfo.fragment;

/**
 * Created by nakama on 02/12/16.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.adapter.OfficialStoreProductAdapter;
import com.tokopedia.core.shopinfo.listener.OsHomeFragmentView;
import com.tokopedia.core.shopinfo.models.GetShopProductParam;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;
import com.tokopedia.core.shopinfo.presenter.OsHomePresenter;
import com.tokopedia.core.shopinfo.presenter.OsHomePresenterImpl;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.widgets.NestedWebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by Erry Suprayogi on 18/11/16.
 *         modified by Mr. Alvarisi
 *         modified by brilliant.oka
 */

public class OfficialShopHomeFragment extends BasePresenterFragment<OsHomePresenter>
        implements OsHomeFragmentView {

    private static final String TAG = OfficialShopHomeFragment.class.getSimpleName();
    public static final String SHOP_URL = "SHOP_URL";
    public static final String TOKOPEDIA_HOST = "tokopedia";

    private static final String URL_QUERY_SORT = "sort";
    private static final String URL_QUERY_KEYWORD = "keyword";
    private static final String URL_QUERY_PAGE = "page";

    private static final String URL_PATH_ETALASE = "etalase";
    private static final String URL_PATH_PRODUCT = "product";
    private static final String URL_PATH_PAGE = "page";

    private static final String STATE_PRODUCT_MODEL = "STATE_PRODUCT_MODEL";
    private static final String PARAM_ALL = "all";
    private static final String SEAMLESS = "seamless";

    @BindView(R2.id.webview)
    NestedWebView webView;
    @BindView(R2.id.progress)
    ProgressBar progressBar;
    @BindView(R2.id.linear_product_list)
    LinearLayout linearProductList;
    @BindView(R2.id.recycler_product)
    RecyclerView recyclerProduct;
    @BindView(R2.id.button_view_all)
    Button buttonViewAll;

    private OfficialShopInteractionListener mOfficialShopInteractionListener;

    OfficialStoreProductAdapter adapter;

    private ProductModel productModel;

    public interface OfficialShopInteractionListener {
        void OnProductListPageRedirected(GetShopProductParam productParam);

        void OnProductInfoPageRedirected(String productId);

        void OnWebViewPageRedirected(String url);
    }

    @Override
    public void showLoading() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            getActivity().setProgressBarIndeterminateVisibility(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {

    }

    @Override
    public void finishLoading() {
        try {
            progressBar.setVisibility(View.GONE);
            getActivity().setProgressBarIndeterminateVisibility(false);
            linearProductList.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetProduct(ProductModel model) {
        if (model != null && model.list != null) {
            productModel.list.clear();
            productModel.list.addAll(model.list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showError(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener) {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    public void onDestroyView() {
        getActivity().setProgressBarIndeterminateVisibility(false);
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
        if (productModel != null) {
            state.putParcelable(STATE_PRODUCT_MODEL, productModel);
        }
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        if (!savedState.isEmpty()) {
            onGetProduct((ProductModel) savedState.getParcelable(STATE_PRODUCT_MODEL));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            if (getActivity() != null &&
                    getActivity() instanceof ShopInfoActivity) {
                ((ShopInfoActivity) getActivity()).swipeAble(false);
            }
        }
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new OsHomePresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        if (activity instanceof OfficialShopInteractionListener) {
            mOfficialShopInteractionListener = (OfficialShopInteractionListener) activity;
        } else {
            throw new RuntimeException("must implement OfficialShopInteractionListener");
        }
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_official_shop_home;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void setViewListener() {
        initWebView();
        initModels();
        initAdapter();

        buttonViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetShopProductParam getShopProductParam = new GetShopProductParam();
                getShopProductParam.setEtalaseId(PARAM_ALL);
                mOfficialShopInteractionListener.OnProductListPageRedirected(getShopProductParam);
            }
        });
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
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

        if (isVisibleToUser) {
            if (getActivity() != null &&
                    getActivity() instanceof ShopInfoActivity) {
                ((ShopInfoActivity) getActivity()).swipeAble(false);
            }
        }
    }

    public static OfficialShopHomeFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(SHOP_URL, url);
        OfficialShopHomeFragment fragment = new OfficialShopHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initWebView() {
        progressBar.setIndeterminate(true);
        String url = getArguments().getString(SHOP_URL);

        loadUrl(url);
        webView.setWebViewClient(new OfficialStoreWebViewClient());
        webView.setWebChromeClient(new OfficialStoreWebChromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
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

    private void loadUrl(String url) {
        if (SessionHandler.isV4Login(getActivity())) {
            webView.loadAuthUrl(!url.contains(SEAMLESS) ?
                    URLGenerator.generateURLSessionLogin(encodeUrl(url), getActivity()) : url);
        } else {
            webView.loadUrl(url);
        }
    }

    private String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return url;
    }

    private class OfficialStoreWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            if (newProgress == 100) {
                view.setVisibility(View.VISIBLE);
                finishLoading();
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    private class OfficialStoreWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoading();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            finishLoading();
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
            super.onReceivedError(view, errorCode, description, failingUrl);
            finishLoading();
        }

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
        if (uri.getScheme().equals(TOKOPEDIA_HOST)) {
            List<String> paths = uri.getPathSegments();
            if (paths.size() > 1) {
                switch (paths.get(1)) {
                    case URL_PATH_ETALASE:
                        String id = uri.getLastPathSegment();
                        GetShopProductParam getShopProductParam = buildProductListParameter(uri);
                        getShopProductParam.setEtalaseId(id);
                        mOfficialShopInteractionListener.OnProductListPageRedirected(getShopProductParam);
                        break;
                    case URL_PATH_PRODUCT:
                        String productId = uri.getLastPathSegment();
                        mOfficialShopInteractionListener.OnProductInfoPageRedirected(productId);
                        break;
                    case URL_PATH_PAGE:
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
        } else if (url.contains("shop-static")) {
            webView.loadUrl(url);
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
                case URL_QUERY_SORT:
                    getShopProductParam.setOrderBy(uri.getQueryParameter(parameterName));
                    break;
                case URL_QUERY_KEYWORD:
                    getShopProductParam.setKeyword(uri.getQueryParameter(parameterName));
                    break;
                case URL_QUERY_PAGE:
                    getShopProductParam.setPage(Integer.parseInt(uri.getQueryParameter(parameterName)));
                    break;
            }
        }
        return getShopProductParam;
    }

    private void initModels() {
        productModel = new ProductModel();
        productModel.list = new ArrayList<>();
    }

    private void initAdapter() {
        adapter = new OfficialStoreProductAdapter(productModel);
        adapter.setListener(new OfficialStoreProductAdapter.ProductListAdapterListener() {
            @Override
            public void onProductClick(int pos) {
                mOfficialShopInteractionListener.OnProductInfoPageRedirected(
                        String.valueOf(productModel.list.get(pos).productId)
                );
            }
        });
        recyclerProduct.setLayoutManager(adapter.getLayoutManager(getActivity()));
        recyclerProduct.setAdapter(adapter);
        recyclerProduct.setNestedScrollingEnabled(false);
        presenter.onRefresh();
    }

    public void onRefreshProductData() {
        presenter.onRefresh();
    }
}