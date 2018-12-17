package com.tokopedia.loyalty.view.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.CouponsDataWrapper;
import java.util.Map;
import com.tokopedia.transactiondata.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCouponInteractor implements IPromoCouponInteractor {
    private final CompositeSubscription compositeSubscription;
    private final ITokoPointRepository tokoplusRepository;

    @Inject
    public PromoCouponInteractor(CompositeSubscription compositeSubscription,
                                 ITokoPointRepository tokoplusRepository) {
        this.compositeSubscription = compositeSubscription;
        this.tokoplusRepository = tokoplusRepository;
    }


    @Override
    public void getCouponList(Map<String, String> param,
                              Subscriber<CouponsDataWrapper> subscriber) {
        compositeSubscription.add(
                tokoplusRepository.getCouponList(param)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void submitDigitalVoucher(String couponTitle, String voucherCode,
                                     Map<String, String> param,
                                     Subscriber<CouponViewModel> subscriber) {
        compositeSubscription.add(
                tokoplusRepository.checkDigitalCouponValidity(param, voucherCode, couponTitle)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void unsubscribe() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }
}
