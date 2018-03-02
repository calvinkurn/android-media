package com.tokopedia.posapp.cart.domain.usecase;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.base.domain.UseCaseWithParams;
import com.tokopedia.posapp.cart.data.repository.CartRepository;
import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/22/17.
 */

public class AddToCartUseCase extends UseCaseWithParams<CartDomain, ATCStatusDomain> {
    CartRepository cartRepository;

    public AddToCartUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            CartRepository cartRepository) {
        super(threadExecutor, postExecutionThread);
        this.cartRepository = cartRepository;
    }

    @Override
    public Observable<ATCStatusDomain> createObservable(final CartDomain cartDomain) {
        return cartRepository.getCartProduct(cartDomain.getProductId())
                .flatMap(new Func1<CartDomain, Observable<ATCStatusDomain>>() {
                    @Override
                    public Observable<ATCStatusDomain> call(CartDomain result) {
                        if(result != null) {
                            result.setQuantity(result.getQuantity() + cartDomain.getQuantity());
                            return cartRepository.updateCartProduct(result);
                        } else {
                            return cartRepository.storeCartProduct(cartDomain);
                        }
                    }
                });
    }
}
