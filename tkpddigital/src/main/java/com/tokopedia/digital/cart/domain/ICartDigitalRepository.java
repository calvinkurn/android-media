package com.tokopedia.digital.cart.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.digital.cart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;

import rx.Observable;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public interface ICartDigitalRepository {
    Observable<CartDigitalInfoData> getCartInfoData(TKPDMapParam<String, String> param);

    Observable<CartDigitalInfoData> patchOtpCart(RequestBodyOtpSuccess requestBodyOtpSuccess,
                                                 TKPDMapParam<String, String> paramGetCart);

    Observable<Boolean> deleteCartData(TKPDMapParam<String, String> param);

    Observable<CartDigitalInfoData> addToCart(
            RequestBodyAtcDigital requestBodyAtcDigital, String idemPotencyKeyHeader
    );

    Observable<String> cancelVoucher();
}
