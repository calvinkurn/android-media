package com.tokopedia.posapp.cart.domain.usecase;

import com.tokopedia.posapp.cart.data.repository.CartLocalRepository;
import com.tokopedia.posapp.cart.data.repository.CartRepository;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 10/13/17.
 */

public class GetAllCartUseCase extends UseCase<List<CartDomain>> {
    private CartRepository cartRepository;

    @Inject
    public GetAllCartUseCase(CartLocalRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Observable<List<CartDomain>> createObservable(RequestParams requestParams) {
        return cartRepository.getAllCartProducts();
    }
}
