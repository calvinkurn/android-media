package com.tokopedia.shop.product.view.adapter.newadapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductFeaturedViewModel;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductFeaturedViewHolder extends AbstractViewHolder<ShopProductFeaturedViewModel> {

    //TODO feature model

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_feature_new;

    public ShopProductFeaturedViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    @Override
    public void bind(ShopProductFeaturedViewModel element) {

    }

    private void findViews(View view) {
    }

}
