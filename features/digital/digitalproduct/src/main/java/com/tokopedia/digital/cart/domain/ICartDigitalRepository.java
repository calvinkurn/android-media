package com.tokopedia.digital.cart.domain;

import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.cart.data.entity.requestbody.voucher.RequestBodyCancelVoucher;

import java.util.Map;

import rx.Observable;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public interface ICartDigitalRepository {

    Observable<CartDigitalInfoData> getCartInfoData(Map<String, String> param);

    Observable<CartDigitalInfoData> patchOtpCart(RequestBodyOtpSuccess requestBodyOtpSuccess,
                                                 Map<String, String> paramGetCart);

    Observable<String> cancelVoucher(RequestBodyCancelVoucher requestBodyCancelVoucher);

}
