package com.tokopedia.digital.newcart.domain;

import com.tokopedia.digital.newcart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.newcart.data.entity.requestbody.voucher.RequestBodyCancelVoucher;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;

import java.util.Map;

import rx.Observable;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public interface ICartDigitalRepository {

    Observable<CartDigitalInfoData> getCartInfoData(Map<String, String> param);

    Observable<CartDigitalInfoData> patchOtpCart(RequestBodyOtpSuccess requestBodyOtpSuccess,
                                                 Map<String, String> paramGetCart);

    Observable<Boolean> cancelVoucher(RequestBodyCancelVoucher requestBodyCancelVoucher);

}
