package com.tokopedia.posapp.product.management.view.listener;

import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;

/**
 * @author okasurya on 4/13/18.
 */

public interface EditProductListener {
    void onDialogDismiss(ProductViewModel productViewModel, int position);
}
