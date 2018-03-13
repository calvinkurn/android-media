package com.tokopedia.posapp.product.management.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.posapp.product.common.view.viewmodel.ProductViewModel;
import com.tokopedia.posapp.product.management.view.adapter.viewholder.ProductManagementViewHolder;

/**
 * @author okasurya on 3/13/18.
 */

public class ProductManagementAdapterTypeFactory extends BaseAdapterTypeFactory {
    private Listener listener;

    public ProductManagementAdapterTypeFactory(Listener listener) {
        this.listener = listener;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == ProductManagementViewHolder.LAYOUT) {
            return new ProductManagementViewHolder(parent, listener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    public int type(ProductViewModel productViewModel) {
        return ProductManagementViewHolder.LAYOUT;
    }

    public interface Listener {
        void onClickEditProduct(View v, ProductViewModel productViewModel);

        void onShowProductCheckedChange(ProductViewModel element, boolean isChecked);
    }
}
