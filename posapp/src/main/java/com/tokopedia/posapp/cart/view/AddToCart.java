package com.tokopedia.posapp.cart.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;

/**
 * Created by okasurya on 8/22/17.
 */

public interface AddToCart {
    interface Presenter extends CustomerPresenter<View> {
        void add(ProductPass productPass, int quantity);

        void addAndCheckout(ProductPass productPass, int quantity);
    }

    interface View extends CustomerView {
        void onErrorAddToCart(String message);

        void onSuccessAddToCart(String message);

        void onSuccessATCPayment(String message);
    }
}
