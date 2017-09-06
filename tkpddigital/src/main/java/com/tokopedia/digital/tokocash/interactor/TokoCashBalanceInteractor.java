package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.digital.tokocash.domain.ITokoCashRepository;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashData;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 9/4/17.
 */

public class TokoCashBalanceInteractor implements ITokoCashBalanceInteractor {

    private final ITokoCashRepository repository;

    private final CompositeSubscription compositeSubscription;

    public TokoCashBalanceInteractor(ITokoCashRepository repository, CompositeSubscription compositeSubscription) {
        this.repository = repository;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void getBalanceTokoCash(Subscriber<TokoCashData> subscriber) {
        compositeSubscription.add(
                repository.getBalanceTokoCash()
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }
}
