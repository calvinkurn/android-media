package com.tokopedia.posapp.cart.domain.usecase;

import com.tokopedia.posapp.cart.data.repository.CartLocalRepository;
import com.tokopedia.posapp.cart.data.repository.CartRepository;
import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yoshua on 30/04/18.
 */

public class DeleteAllCartUseCase extends UseCase<ATCStatusDomain> {
    private CartRepository cartRepository;

    @Inject
    public DeleteAllCartUseCase(CartLocalRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Observable<ATCStatusDomain> createObservable(RequestParams requestParams) {
        return cartRepository.deleteCart();
    }
}
