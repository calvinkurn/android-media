package com.tokopedia.common_digital.cart.data.repository;

import com.tokopedia.common_digital.cart.data.datasource.DigitalAddToCartDataSource;
import com.tokopedia.common_digital.cart.data.datasource.DigitalInstantCheckoutDataSource;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.domain.IDigitalCartRepository;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;

import rx.Observable;

/**
 * Created by Rizky on 27/08/18.
 */
public class DigitalCartRepository implements IDigitalCartRepository {

    private DigitalAddToCartDataSource digitalAddToCartDataSource;
    private DigitalInstantCheckoutDataSource digitalInstantCheckoutDataSource;

    public DigitalCartRepository(DigitalAddToCartDataSource digitalAddToCartDataSource,
                                 DigitalInstantCheckoutDataSource digitalInstantCheckoutDataSource) {
        this.digitalAddToCartDataSource = digitalAddToCartDataSource;
        this.digitalInstantCheckoutDataSource = digitalInstantCheckoutDataSource;
    }

    @Override
    public Observable<CartDigitalInfoData> addToCart(RequestBodyAtcDigital requestBodyAtcDigital, String idemPotencyKeyHeader) {
        return digitalAddToCartDataSource.addToCart(requestBodyAtcDigital, idemPotencyKeyHeader);
    }

    @Override
    public Observable<InstantCheckoutData> instantCheckout(RequestBodyCheckout requestBodyCheckout) {
        return digitalInstantCheckoutDataSource.instantCheckout(requestBodyCheckout);
    }

}
