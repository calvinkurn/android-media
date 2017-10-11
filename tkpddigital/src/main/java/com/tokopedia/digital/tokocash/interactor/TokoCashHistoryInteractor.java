package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.digital.tokocash.domain.IHistoryTokoCashRepository;
import com.tokopedia.digital.tokocash.mapper.TokoCashHistoryMapper;
import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public class TokoCashHistoryInteractor implements ITokoCashHistoryInteractor {

    private final IHistoryTokoCashRepository repository;
    private CompositeSubscription compositeSubscription;
    private TokoCashHistoryMapper tokoCashHistoryMapper;

    public TokoCashHistoryInteractor(IHistoryTokoCashRepository repository,
                                     CompositeSubscription compositeSubscription) {
        this.repository = repository;
        this.compositeSubscription = compositeSubscription;
        tokoCashHistoryMapper = new TokoCashHistoryMapper();
    }

    @Override
    public void getHistoryTokoCash(Subscriber<TokoCashHistoryData> subscriber, String type,
                                   String startDate, String endDate, String afterId) {
        compositeSubscription.add(
                repository.getTokoCashHistoryData(type, startDate, endDate, afterId)
                        .map(tokoCashHistoryMapper)
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }
}
