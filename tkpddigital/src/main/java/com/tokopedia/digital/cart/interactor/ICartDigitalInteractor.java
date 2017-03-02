package com.tokopedia.digital.cart.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public interface ICartDigitalInteractor {
    void getCartInfoData(
            TKPDMapParam<String, String> paramNetwork, Subscriber<CartDigitalInfoData> subscriber
    );

    void addToCart(
            TKPDMapParam<String, String> paramNetwork, Subscriber<CartDigitalInfoData> subscriber
    );
}
