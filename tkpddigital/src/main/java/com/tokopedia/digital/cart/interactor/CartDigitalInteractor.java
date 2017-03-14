package com.tokopedia.digital.cart.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.cart.data.entity.requestbody.topcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.cart.domain.CartDigitalRepository;
import com.tokopedia.digital.cart.domain.CheckoutRepository;
import com.tokopedia.digital.cart.domain.ICartDigitalRepository;
import com.tokopedia.digital.cart.domain.ICheckoutRepository;
import com.tokopedia.digital.cart.domain.IVoucherDigitalRepository;
import com.tokopedia.digital.cart.domain.VoucherDigitalRepository;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;
import com.tokopedia.digital.cart.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.model.VoucherDigital;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class CartDigitalInteractor implements ICartDigitalInteractor {

    private final ICartDigitalRepository cartDigitalRepository;
    private final IVoucherDigitalRepository voucherDigitalRepository;
    private final ICheckoutRepository checkoutRepository;

    public CartDigitalInteractor(CartDigitalRepository cartDigitalRepository,
                                 VoucherDigitalRepository voucherDigitalRepository,
                                 CheckoutRepository checkoutRepository) {
        this.cartDigitalRepository = cartDigitalRepository;
        this.voucherDigitalRepository = voucherDigitalRepository;
        this.checkoutRepository = checkoutRepository;
    }

    @Override
    public void getCartInfoData(TKPDMapParam<String, String> paramNetwork,
                                Subscriber<CartDigitalInfoData> subscriber) {
        cartDigitalRepository.getCartInfoData(paramNetwork)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void addToCart(RequestBodyAtcDigital requestBodyAtcDigital, String idemPotencyKeyHeader,
                          Subscriber<CartDigitalInfoData> subscriber) {
        cartDigitalRepository.addToCart(requestBodyAtcDigital, idemPotencyKeyHeader)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void checkVoucher(
            TKPDMapParam<String, String> paramNetwork, Subscriber<VoucherDigital> subscriber
    ) {
        voucherDigitalRepository.checkVoucher(paramNetwork)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void checkout(RequestBodyCheckout requestBodyCheckout,
                         Subscriber<CheckoutDigitalData> subscriber) {
        checkoutRepository.checkoutCart(requestBodyCheckout)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Override
    public void patchCartOtp(
            RequestBodyOtpSuccess requestBodyOtpSuccess, TKPDMapParam<String, String> paramgetCart,
            Subscriber<CartDigitalInfoData> subscriber
    ) {
        cartDigitalRepository.patchOtpCart(requestBodyOtpSuccess, paramgetCart)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }
}
