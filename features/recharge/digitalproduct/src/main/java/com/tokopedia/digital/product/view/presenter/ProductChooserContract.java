package com.tokopedia.digital.product.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common_digital.product.presentation.model.Product;

import java.util.List;

/**
 * Created by Rizky on 29/08/18.
 */
public class ProductChooserContract {

    public interface View extends CustomerView {

        void showProducts(List<Product> products);

        void showInitialProgressLoading();

        void hideInitialProgressLoading();

    }

    public interface Presenter extends CustomerPresenter<View> {

        void getProductsByCategoryIdAndOperatorId(String categoryId, String operatorId);

    }

}
