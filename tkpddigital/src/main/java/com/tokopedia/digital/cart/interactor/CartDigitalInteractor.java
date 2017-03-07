package com.tokopedia.digital.cart.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.digital.cart.domain.CartDigitalRepository;
import com.tokopedia.digital.cart.domain.ICartDigitalRepository;
import com.tokopedia.digital.cart.domain.IVoucherDigitalRepository;
import com.tokopedia.digital.cart.domain.VoucherDigitalRepository;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class CartDigitalInteractor implements ICartDigitalInteractor {

    private final ICartDigitalRepository cartDigitalRepository;
    private final IVoucherDigitalRepository voucherDigitalRepository;

    public CartDigitalInteractor() {
        cartDigitalRepository = new CartDigitalRepository();
        voucherDigitalRepository = new VoucherDigitalRepository();
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
            TKPDMapParam<String, String> paramNetwork, Subscriber<String> subscriber
    ) {
        voucherDigitalRepository.checkVoucher(paramNetwork)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }
}
