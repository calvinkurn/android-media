package com.tokopedia.digital.cart.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.domain.CartDigitalRepository;
import com.tokopedia.digital.cart.domain.ICartDigitalRepository;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class CartDigitalInteractor implements ICartDigitalInteractor {

    private final ICartDigitalRepository cartDigitalRepository;

    public CartDigitalInteractor() {
        cartDigitalRepository = new CartDigitalRepository();
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
    public void addToCart(TKPDMapParam<String, String> paramNetwork,
                          Subscriber<CartDigitalInfoData> subscriber) {
        cartDigitalRepository.addToCart(paramNetwork)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }
}
