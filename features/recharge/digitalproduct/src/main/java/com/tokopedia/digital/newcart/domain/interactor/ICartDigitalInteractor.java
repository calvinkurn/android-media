package com.tokopedia.digital.newcart.domain.interactor;

import com.tokopedia.digital.newcart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.newcart.data.entity.requestbody.voucher.RequestBodyCancelVoucher;
import com.tokopedia.digital.newcart.domain.model.VoucherDigital;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;

import java.util.Map;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public interface ICartDigitalInteractor {

    void getCartInfoData(
            Map<String, String> paramNetwork, Subscriber<CartDigitalInfoData> subscriber
    );

    void checkVoucher(
            Map<String, String> paramNetwork, Subscriber<VoucherDigital> subscriber
    );

    void patchCartOtp(
            RequestBodyOtpSuccess requestBodyOtpSuccess, Map<String, String> paramgetCart,
            Subscriber<CartDigitalInfoData> subscriber
    );

    void cancelVoucher(RequestBodyCancelVoucher requestBodyCancelVoucher, Subscriber<Boolean> subscriber);

}
