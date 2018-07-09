package com.tokopedia.posapp.product.management.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductHeaderViewModel;

/**
 * @author okasurya on 3/13/18.
 */

public class ProductManagementHeaderViewHolder extends AbstractViewHolder<ProductHeaderViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_header_product_management;

    public ProductManagementHeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(ProductHeaderViewModel element) {

    }
}
