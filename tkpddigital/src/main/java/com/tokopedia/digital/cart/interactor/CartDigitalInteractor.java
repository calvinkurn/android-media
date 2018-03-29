package com.tokopedia.digital.cart.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.cart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.cart.domain.ICartDigitalRepository;
import com.tokopedia.digital.cart.domain.ICheckoutRepository;
import com.tokopedia.digital.cart.domain.IVoucherDigitalRepository;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;
import com.tokopedia.digital.cart.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.model.InstantCheckoutData;
import com.tokopedia.digital.cart.model.VoucherDigital;

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
    private final ICheckoutRepository checkoutRepository;

    public CartDigitalInteractor(CompositeSubscription compositeSubscription,
                                 ICartDigitalRepository cartDigitalRepository,
                                 IVoucherDigitalRepository voucherDigitalRepository,
                                 ICheckoutRepository checkoutRepository) {
        this.compositeSubscription = compositeSubscription;
        this.cartDigitalRepository = cartDigitalRepository;
        this.voucherDigitalRepository = voucherDigitalRepository;
        this.checkoutRepository = checkoutRepository;
    }

    @Override
    public void getCartInfoData(TKPDMapParam<String, String> paramNetwork,
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
    public void addToCart(RequestBodyAtcDigital requestBodyAtcDigital, String idemPotencyKeyHeader,
                          Subscriber<CartDigitalInfoData> subscriber) {
        compositeSubscription.add(
                cartDigitalRepository.addToCart(requestBodyAtcDigital, idemPotencyKeyHeader)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void checkVoucher(
            TKPDMapParam<String, String> paramNetwork, Subscriber<VoucherDigital> subscriber
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
    public void checkout(RequestBodyCheckout requestBodyCheckout,
                         Subscriber<CheckoutDigitalData> subscriber) {
        compositeSubscription.add(
                checkoutRepository.checkoutCart(requestBodyCheckout)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void instantCheckout(RequestBodyCheckout requestBodyCheckout,
                                Subscriber<InstantCheckoutData> subscriber) {
        compositeSubscription.add(
                checkoutRepository.instantCheckoutCart(requestBodyCheckout)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void patchCartOtp(
            RequestBodyOtpSuccess requestBodyOtpSuccess, TKPDMapParam<String, String> paramgetCart,
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
    public void cancelVoucher(Subscriber<String> subscriber) {
        compositeSubscription.add(
                cartDigitalRepository.cancelVoucher()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }
}
