package com.tokopedia.posapp.cart.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author okasurya on 3/27/18.
 */

public interface CartMenu {
    interface Presenter extends CustomerPresenter<View> {
        void checkCartItem();
    }

    interface View extends CustomerView {
        void onCartFilled(int cartCount);

        void onCartEmpty();
    }
}
