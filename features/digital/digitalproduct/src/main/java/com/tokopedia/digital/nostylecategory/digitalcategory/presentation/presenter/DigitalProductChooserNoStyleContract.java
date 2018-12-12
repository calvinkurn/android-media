package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common_digital.product.presentation.model.Product;

import java.util.List;

/**
 * Created by Rizky on 10/09/18.
 */
public class DigitalProductChooserNoStyleContract {

    public interface View extends CustomerView {

        void renderProducts(List<Product> productList);

    }

    interface Presenter extends CustomerPresenter<View> {

        void getProducts(int categoryId, String operatorId);

    }

}
