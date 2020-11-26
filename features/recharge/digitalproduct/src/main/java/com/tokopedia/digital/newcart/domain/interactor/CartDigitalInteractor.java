package com.tokopedia.digital.newcart.domain.interactor;

import com.tokopedia.digital.newcart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.newcart.data.entity.requestbody.voucher.RequestBodyCancelVoucher;
import com.tokopedia.digital.newcart.domain.ICartDigitalRepository;
import com.tokopedia.digital.newcart.domain.IVoucherDigitalRepository;
import com.tokopedia.digital.newcart.domain.model.VoucherDigital;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class CartDigitalInteractor implements ICartDigitalInteractor {

    private final CompositeSubscription compositeSubscription;
    private final ICartDigitalRepository cartDigitalRepository;
    private final IVoucherDigitalRepository voucherDigitalRepository;

    public CartDigitalInteractor(CompositeSubscription compositeSubscription,
                                 ICartDigitalRepository cartDigitalRepository,
                                 IVoucherDigitalRepository voucherDigitalRepository) {
        this.compositeSubscription = compositeSubscription;
        this.cartDigitalRepository = cartDigitalRepository;
        this.voucherDigitalRepository = voucherDigitalRepository;
    }

    @Override
    public void getCartInfoData(Map<String, String> paramNetwork,
                                Subscriber<CartDigitalInfoData> subscriber) {
        compositeSubscription.add(
                cartDigitalRepository.getCartInfoData(paramNetwork)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void checkVoucher(
            Map<String, String> paramNetwork, Subscriber<VoucherDigital> subscriber
    ) {
        compositeSubscription.add(
                voucherDigitalRepository.checkVoucher(paramNetwork)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void patchCartOtp(
            RequestBodyOtpSuccess requestBodyOtpSuccess, Map<String, String> paramgetCart,
            Subscriber<CartDigitalInfoData> subscriber
    ) {
        compositeSubscription.add(
                cartDigitalRepository.patchOtpCart(requestBodyOtpSuccess, paramgetCart)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void cancelVoucher(RequestBodyCancelVoucher requestBodyCancelVoucher, Subscriber<Boolean> subscriber) {
        compositeSubscription.add(
                cartDigitalRepository.cancelVoucher(requestBodyCancelVoucher)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }
}
