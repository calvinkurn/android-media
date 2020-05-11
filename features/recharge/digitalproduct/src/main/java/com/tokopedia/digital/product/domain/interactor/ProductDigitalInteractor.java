package com.tokopedia.digital.product.domain.interactor;

import com.tokopedia.digital.product.data.entity.requestbody.pulsabalance.RequestBodyPulsaBalance;
import com.tokopedia.digital.product.domain.IUssdCheckBalanceRepository;
import com.tokopedia.digital.product.view.model.PulsaBalance;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalInteractor implements IProductDigitalInteractor {

    private final IUssdCheckBalanceRepository ussdCheckBalanceRepository;

    public ProductDigitalInteractor(IUssdCheckBalanceRepository ussdCheckBalanceRepository) {
        this.ussdCheckBalanceRepository = ussdCheckBalanceRepository;
    }

    @Override
    public void porcessPulsaUssdResponse(RequestBodyPulsaBalance requestBodyPulsaBalance, Subscriber<PulsaBalance> subscriber) {
        ussdCheckBalanceRepository.processPulsaBalanceUssdResponse(requestBodyPulsaBalance)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

}
