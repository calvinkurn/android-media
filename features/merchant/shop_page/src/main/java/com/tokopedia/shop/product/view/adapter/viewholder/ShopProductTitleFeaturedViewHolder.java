package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductTitleFeaturedViewModel;

/**
 * Created by zulfikarrahman on 3/30/18.
 */

public class ShopProductTitleFeaturedViewHolder extends AbstractViewHolder<ShopProductTitleFeaturedViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_featured_title_view;

    public ShopProductTitleFeaturedViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(ShopProductTitleFeaturedViewModel element) {

    }
}
