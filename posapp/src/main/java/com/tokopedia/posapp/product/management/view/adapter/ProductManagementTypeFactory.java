package com.tokopedia.posapp.product.management.view.adapter;

import android.view.View;

import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductHeaderViewModel;

/**
 * @author okasurya on 3/13/18.
 */

public interface ProductManagementTypeFactory {
    int type(ProductViewModel viewModel);

    int type(ProductHeaderViewModel viewModel);

    interface Listener {
        void onClickEditProduct(View v, ProductViewModel productViewModel);

        void onShowProductCheckedChange(ProductViewModel element, boolean isChecked);
    }
}
