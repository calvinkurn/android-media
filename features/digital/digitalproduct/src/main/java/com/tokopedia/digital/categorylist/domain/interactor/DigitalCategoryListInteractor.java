package com.tokopedia.digital.categorylist.domain.interactor;

import com.tokopedia.digital.categorylist.data.cloud.entity.tokocash.TokoCashData;
import com.tokopedia.digital.categorylist.domain.IDigitalCategoryListRepository;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;
import com.tokopedia.digital.common.router.DigitalModuleRouter;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListInteractor implements IDigitalCategoryListInteractor {
    private final CompositeSubscription compositeSubscription;
    private final IDigitalCategoryListRepository digitalCategoryListRepository;
    private DigitalModuleRouter digitalModuleRouter;

    public DigitalCategoryListInteractor(CompositeSubscription compositeSubscription,
                                         IDigitalCategoryListRepository digitalCategoryListRepository,
                                         DigitalModuleRouter digitalModuleRouter) {
        this.compositeSubscription = compositeSubscription;
        this.digitalCategoryListRepository = digitalCategoryListRepository;
        this.digitalModuleRouter = digitalModuleRouter;
    }

    @Override
    public void getDigitalCategoryItemDataList(String deviceVersion, Subscriber<List<DigitalCategoryItemData>> subscriber) {
        compositeSubscription.add(digitalCategoryListRepository.getDigitalCategoryItemDataList(deviceVersion)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void getTokoCashData(Subscriber<TokoCashData> subscriber) {
        Observable<TokoCashData> observable = digitalModuleRouter.getDigitalTokoCashBalance();
        if (observable != null) {
            compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber));
        }
    }
}
