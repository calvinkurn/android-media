package com.tokopedia.loyalty.view.viewholder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.abstraction.base.view.webview.TkpdWebView;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.adapter.PromoDetailAdapter.OnAdapterActionListener;
import com.tokopedia.loyalty.view.data.PromoDetailTncHolderData;

/**
 * @author Aghny A. Putra on 26/03/18
 */

public class PromoDetailTnCViewHolder extends RecyclerView.ViewHolder {

    public static final int ITEM_VIEW_TNC = R.layout.item_view_promo_tnc_layout;

    private static final String QUERY_FLAG_APP = "flag_app";
    private static final String DEFAULT_VALUE_QUERY_FLAG_APP = "1";

    private OnAdapterActionListener adapterActionListener;

    private TkpdWebView webView;

    public PromoDetailTnCViewHolder(View itemView,
                                    OnAdapterActionListener adapterActionListener) {
        super(itemView);

        this.webView = itemView.findViewById(com.tokopedia.abstraction.R.id.webview);
        this.adapterActionListener = adapterActionListener;
    }

    public void bind(PromoDetailTncHolderData holderData) {
        loadWeb(holderData.getTermAndConditions().get(0));
    }

    private void loadWeb(String content) {
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                adapterActionListener.onWebViewLinkClicked(getUrlWithFlag(url));
                return true;
            }
        });

        webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
    }

    private String getUrlWithFlag(String url) {
        return Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(QUERY_FLAG_APP, DEFAULT_VALUE_QUERY_FLAG_APP)
                .build().toString();
    }
}
