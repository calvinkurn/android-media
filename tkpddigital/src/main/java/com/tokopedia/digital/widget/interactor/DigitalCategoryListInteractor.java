package com.tokopedia.digital.widget.interactor;

import com.tokopedia.digital.widget.data.entity.DigitalCategoryItemData;
import com.tokopedia.digital.widget.domain.IDigitalCategoryListRepository;

import java.util.List;

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
}
