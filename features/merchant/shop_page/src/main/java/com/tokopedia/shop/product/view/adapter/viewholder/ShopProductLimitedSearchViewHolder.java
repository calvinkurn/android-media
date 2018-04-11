package com.tokopedia.shop.product.view.adapter.viewholder;

import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedSearchViewModel;
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductLimitedSearchViewHolder extends AbstractViewHolder<ShopProductLimitedSearchViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_limited_search_view;

    private final SearchInputView.Listener searchInputViewListener;
    private final View.OnClickListener onClickListener;

    private SearchInputView searchInputView;

    public ShopProductLimitedSearchViewHolder(View itemView, SearchInputView.Listener searchInputViewListener, View.OnClickListener onClickListener) {
        super(itemView);
        this.searchInputViewListener = searchInputViewListener;
        this.onClickListener = onClickListener;
        findViews(itemView);
    }

    private void findViews(View view) {
        searchInputView = view.findViewById(R.id.search_input_view);
        searchInputView.setListener(searchInputViewListener);
        searchInputView.setOnClickListener(onClickListener);
    }

    @Override
    public void bind(ShopProductLimitedSearchViewModel shopProductLimitedSearchViewModel) {

    }
}