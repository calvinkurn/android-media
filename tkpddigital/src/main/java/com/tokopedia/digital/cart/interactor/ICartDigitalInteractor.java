package com.tokopedia.digital.cart.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.cart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;
import com.tokopedia.digital.cart.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.model.InstantCheckoutData;
import com.tokopedia.digital.cart.model.VoucherDigital;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public interface ICartDigitalInteractor {
    void getCartInfoData(
            TKPDMapParam<String, String> paramNetwork, Subscriber<CartDigitalInfoData> subscriber
    );

    void addToCart(
            RequestBodyAtcDigital requestBodyAtcDigital, String idemPotencyKeyHeader,
            Subscriber<CartDigitalInfoData> subscriber
    );

    void checkVoucher(
            TKPDMapParam<String, String> paramNetwork, Subscriber<VoucherDigital> subscriber
    );

    void checkout(
            RequestBodyCheckout requestBodyCheckout, Subscriber<CheckoutDigitalData> subscriber
    );

    void instantCheckout(
            RequestBodyCheckout requestBodyCheckout, Subscriber<InstantCheckoutData> subscriber
    );

    void patchCartOtp(
            RequestBodyOtpSuccess requestBodyOtpSuccess, TKPDMapParam<String, String> paramgetCart,
            Subscriber<CartDigitalInfoData> subscriber
    );

    void cancelVoucher(Subscriber<String> subscriber);
}
