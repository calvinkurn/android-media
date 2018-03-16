package com.tokopedia.posapp.product.management.view.presenter;

import com.tokopedia.posapp.product.management.view.EditProduct;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;

/**
 * @author okasurya on 3/14/18.
 */

public class EditProductPresenter implements EditProduct.Presenter {
    private EditProduct.View view;

    @Override
    public void attachView(EditProduct.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void save(ProductViewModel productViewModel, String s) {
        view.onSuccessSave();
    }
}
