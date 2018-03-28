package com.tokopedia.posapp.cart.view.presenter;

import com.tokopedia.posapp.cart.domain.usecase.GetAllCartUseCase;
import com.tokopedia.posapp.cart.view.CartMenu;
import com.tokopedia.posapp.cart.view.subscriber.CartMenuSubscriber;

import javax.inject.Inject;

/**
 * @author okasurya on 3/27/18.
 */

public class CartMenuPresenter implements CartMenu.Presenter {
    private GetAllCartUseCase getAllCartUseCase;
    private CartMenu.View view;

    @Inject
    public CartMenuPresenter(GetAllCartUseCase getAllCartUseCase) {
        this.getAllCartUseCase = getAllCartUseCase;
    }

    @Override
    public void attachView(CartMenu.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void checkCartItem() {
        getAllCartUseCase.execute(new CartMenuSubscriber(view));
    }
}
