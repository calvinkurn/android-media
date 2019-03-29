package com.tokopedia.posapp.product.management.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductHeaderViewModel;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;
import com.tokopedia.posapp.product.management.view.adapter.viewholder.ProductManagementHeaderViewHolder;
import com.tokopedia.posapp.product.management.view.adapter.viewholder.ProductManagementViewHolder;

/**
 * @author okasurya on 3/13/18.
 */

public class ProductManagementAdapterTypeFactory extends BaseAdapterTypeFactory implements ProductManagementTypeFactory {
    private Listener listener;

    public ProductManagementAdapterTypeFactory(Listener listener) {
        this.listener = listener;
    }

    @Override
    public int type(EmptyModel viewModel) {
        return super.type(viewModel);
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == ProductManagementViewHolder.LAYOUT) {
            return new ProductManagementViewHolder(parent, listener);
        } else if(type == ProductManagementHeaderViewHolder.LAYOUT) {
            return new ProductManagementHeaderViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public int type(ProductViewModel viewModel) {
        return ProductManagementViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductHeaderViewModel viewModel) {
        return ProductManagementHeaderViewHolder.LAYOUT;
    }
}
