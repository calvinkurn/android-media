package com.tokopedia.posapp.cart.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;

/**
 * Created by okasurya on 8/22/17.
 */

public interface AddToCart {
    interface Presenter extends CustomerPresenter<View> {
        void add(ProductDetailData productPass, int quantity);

        void addAndCheckout(ProductDetailData productPass, int quantity);
    }

    interface View extends CustomerView {
        void onErrorAddToCart(String message);

        void onSuccessAddToCart(String message);

        void onSuccessATCPayment(String message);
    }
}
