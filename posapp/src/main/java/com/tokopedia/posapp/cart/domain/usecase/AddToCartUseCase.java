package com.tokopedia.posapp.cart.domain.usecase;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.cart.data.repository.CartLocalRepository;
import com.tokopedia.posapp.cart.data.repository.CartRepository;
import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by okasurya on 8/22/17.
 */

public class AddToCartUseCase extends UseCase<ATCStatusDomain> {
    private CartRepository cartRepository;

    @Inject
    public AddToCartUseCase(CartLocalRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Observable<ATCStatusDomain> createObservable(final RequestParams requestParams) {
        return cartRepository.getCartProduct(requestParams)
                .flatMap(new Func1<CartDomain, Observable<ATCStatusDomain>>() {
                    @Override
                    public Observable<ATCStatusDomain> call(CartDomain existingCart) {
                        if(existingCart != null) {
                            requestParams.putObject(CartRepository.EXISTING_CART, existingCart);
                            return cartRepository.updateCartProduct(requestParams);
                        } else {
                            return cartRepository.storeCartProduct(requestParams);
                        }
                    }
                });
    }
}
