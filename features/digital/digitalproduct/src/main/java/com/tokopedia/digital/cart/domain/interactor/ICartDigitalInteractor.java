package com.tokopedia.digital.cart.domain.interactor;

import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.cart.data.entity.requestbody.voucher.RequestBodyCancelVoucher;
import com.tokopedia.digital.cart.presentation.model.VoucherDigital;

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

    void cancelVoucher(RequestBodyCancelVoucher requestBodyCancelVoucher, Subscriber<String> subscriber);

}
