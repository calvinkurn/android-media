package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.base.domain.UseCaseWithParams;
import com.tokopedia.posapp.data.repository.CartRepository;
import com.tokopedia.posapp.domain.model.cart.AddToCartStatusDomain;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/22/17.
 */

public class AddToCartUseCase extends UseCaseWithParams<CartDomain, AddToCartStatusDomain> {
    CartRepository cartRepository;

    public AddToCartUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            CartRepository cartRepository) {
        super(threadExecutor, postExecutionThread);
        this.cartRepository = cartRepository;
    }

    @Override
    public Observable<AddToCartStatusDomain> createObservable(CartDomain requestParams) {
        return cartRepository.addToCart(requestParams);
    }
}
