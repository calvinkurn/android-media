package com.tokopedia.loyalty.view.interactor;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.repository.IPromoRepository;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoInteractor implements IPromoInteractor {
    private final CompositeSubscription compositeSubscription;
    private final IPromoRepository promoRepository;

    @Inject
    public PromoInteractor(CompositeSubscription compositeSubscription, IPromoRepository promoRepository) {
        this.compositeSubscription = compositeSubscription;
        this.promoRepository = promoRepository;
    }

    @Override
    public void getPromoMenuList(TKPDMapParam<String, String> param, Subscriber<List<PromoMenuData>> subscriber) {
        compositeSubscription.add(
                promoRepository.getPromoMenuDataList(param)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public Observable<List<PromoData>> getPromoList(TKPDMapParam<String, String> param, Subscriber<List<PromoData>> subscriber) {
        Observable<List<PromoData>> observable = promoRepository.getPromoDataList(param);
        compositeSubscription.add(observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
        return observable;
    }
}
