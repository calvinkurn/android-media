package com.tokopedia.common_digital.cart.data.repository;

import com.tokopedia.common_digital.cart.data.datasource.DigitalAddToCartDataSource;
import com.tokopedia.common_digital.cart.data.datasource.DigitalCheckoutDataSource;
import com.tokopedia.common_digital.cart.data.datasource.DigitalGetCartDataSource;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.domain.IDigitalCartRepository;
import com.tokopedia.common_digital.cart.view.model.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.CheckoutDigitalData;

import rx.Observable;

/**
 * Created by Rizky on 27/08/18.
 */
public class DigitalCartRepository implements IDigitalCartRepository {

    private DigitalAddToCartDataSource digitalAddToCartDataSource;
    private DigitalGetCartDataSource digitalGetCartUseCase;
    private DigitalCheckoutDataSource digitalCheckoutDataSource;

    public DigitalCartRepository(DigitalAddToCartDataSource digitalAddToCartDataSource,
                                 DigitalGetCartDataSource digitalGetCartDataSource,
                                 DigitalCheckoutDataSource digitalCheckoutDataSource) {
        this.digitalAddToCartDataSource = digitalAddToCartDataSource;
        this.digitalGetCartUseCase = digitalGetCartDataSource;
        this.digitalCheckoutDataSource = digitalCheckoutDataSource;
    }

    @Override
    public Observable<CartDigitalInfoData> addToCart(RequestBodyAtcDigital requestBodyAtcDigital, String idemPotencyKeyHeader) {
        return digitalAddToCartDataSource.addToCart(requestBodyAtcDigital, idemPotencyKeyHeader);
    }

    @Override
    public Observable<CartDigitalInfoData> getCart(String categoryId) {
        return digitalGetCartUseCase.getCart(categoryId);
    }

    @Override
    public Observable<CheckoutDigitalData> checkoutCart(RequestBodyCheckout requestBodyCheckout) {
        return digitalCheckoutDataSource.checkout(requestBodyCheckout);
    }

}
