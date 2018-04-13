package com.tokopedia.posapp.product.management.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;

/**
 * @author okasurya on 3/14/18.
 */

public interface EditProduct {
    interface Presenter extends CustomerPresenter<View> {
        void save(ProductViewModel productViewModel, String localPrice);
    }

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void onSuccessSave();

        void onErrorSave(String errorMessage);
    }
}
