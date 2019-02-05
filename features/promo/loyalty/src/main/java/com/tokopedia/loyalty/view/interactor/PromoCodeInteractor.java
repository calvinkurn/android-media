package com.tokopedia.loyalty.view.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import java.util.Map;
import com.tokopedia.transactiondata.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCodeInteractor implements IPromoCodeInteractor {

    private final CompositeSubscription compositeSubscription;
    private final ITokoPointRepository tokoplusRepositoy;

    @Inject
    public PromoCodeInteractor(CompositeSubscription compositeSubscription,
                               ITokoPointRepository repository) {
        this.compositeSubscription = compositeSubscription;
        this.tokoplusRepositoy = repository;
    }

    @Override
    public void submitDigitalVoucher(String voucherCode,
                                     Map<String, String> param, Subscriber<VoucherViewModel> subscriber) {
        compositeSubscription.add(
                tokoplusRepositoy.checkDigitalVoucherValidity(param, voucherCode)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }
}
