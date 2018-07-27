package com.tokopedia.shop.product.view.adapter.viewholder;

import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.model.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.widget.NestedWebView;

public class ShopProductPromoViewHolder extends AbstractViewHolder<ShopProductPromoViewModel> {

    private static final String SHOP_STATIC_URL = "shop-static";
    private static final int MIN_SHOW_WEB_VIEW_PROGRESS = 100;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_limited_promo;

    private final PromoViewHolderListener promoViewHolderListener;

    private View loadingLayout;
    private NestedWebView shopPagePromoWebView;

    private boolean isLogin;
    private boolean isBind;

    public ShopProductPromoViewHolder(View itemView, PromoViewHolderListener promoViewHolderListener) {
        super(itemView);
        this.promoViewHolderListener = promoViewHolderListener;
        findViews(itemView);
    }

    private void findViews(View view) {
        loadingLayout = view.findViewById(R.id.layout_loading);
        shopPagePromoWebView = view.findViewById(R.id.web_view);
        shopPagePromoWebView.setWebViewClient(new OfficialStoreWebViewClient());
        shopPagePromoWebView.setWebChromeClient(new OfficialStoreWebChromeClient());

        WebSettings webSettings = shopPagePromoWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        optimizeWebView();
        CookieManager.getInstance().setAcceptCookie(true);
    }

    private void optimizeWebView() {
        if (Build.VERSION.SDK_INT >= 19) {
            shopPagePromoWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            shopPagePromoWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void clearCache(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    @Override
    public void bind(ShopProductPromoViewModel shopProductPromoViewModel) {
        if (isBind && isLogin == shopProductPromoViewModel.isLogin()) {
            return;
        }
        clearCache(shopPagePromoWebView);
        if (shopProductPromoViewModel.isLogin()) {
            shopPagePromoWebView.loadAuthUrl(shopProductPromoViewModel.getUrl(),
                    shopProductPromoViewModel.getUserId(),
                    shopProductPromoViewModel.getAccessToken());
        } else {
            shopPagePromoWebView.loadUrl(shopProductPromoViewModel.getUrl());
        }

        isLogin = shopProductPromoViewModel.isLogin();
        isBind = true;
    }

    private void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        shopPagePromoWebView.setVisibility(View.GONE);
    }

    private void finishLoading() {
        loadingLayout.setVisibility(View.GONE);
        shopPagePromoWebView.setVisibility(View.VISIBLE);
    }

    private class OfficialStoreWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.i("PromoViewHolder", "progress: " + newProgress);
            if (newProgress >= MIN_SHOW_WEB_VIEW_PROGRESS) {
                view.setVisibility(View.VISIBLE);
                finishLoading();
                Log.i("PromoViewHolder", "finish progress");
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
            Log.i("PromoViewHolder", "onReceivedSslError");
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            Log.i("PromoViewHolder", "onLoadResource url: " + url);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            finishLoading();
            Log.i("PromoViewHolder", "onReceivedError: " + errorCode + description + failingUrl);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (url.contains(SHOP_STATIC_URL)) {
                view.loadUrl(url);
            } else if (uri.getScheme().equals(ShopProductOfficialStoreUtils.TOKOPEDIA_HOST) || uri.getScheme().startsWith(ShopProductOfficialStoreUtils.HTTP)) {
                if (promoViewHolderListener != null) {
                    promoViewHolderListener.promoClicked(url);
                }
            }
            return true;
        }

    }

    public interface PromoViewHolderListener {

        void promoClicked(String url);
    }
}