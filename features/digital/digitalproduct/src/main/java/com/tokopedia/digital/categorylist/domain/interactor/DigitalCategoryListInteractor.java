package com.tokopedia.digital.categorylist.domain.interactor;

import android.content.Context;

import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.digital.categorylist.domain.IDigitalCategoryListRepository;
import com.tokopedia.digital.categorylist.view.model.DigitalCategoryItemData;

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

    public DigitalCategoryListInteractor(CompositeSubscription compositeSubscription,
                                         IDigitalCategoryListRepository digitalCategoryListRepository) {
        this.compositeSubscription = compositeSubscription;
        this.digitalCategoryListRepository = digitalCategoryListRepository;
    }

    @Override
    public void getDigitalCategoryItemDataList(Subscriber<List<DigitalCategoryItemData>> subscriber) {
        compositeSubscription.add(digitalCategoryListRepository.getDigitalCategoryItemDataList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void getTokoCashData(Subscriber<TokoCashData> subscriber, Context context) {
        if (context instanceof TkpdCoreRouter) {
            Observable<TokoCashData> observable = ((TkpdCoreRouter) context).getTokoCashBalance();
            compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber));
        }
    }
}
