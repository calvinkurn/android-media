package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.digital.tokocash.domain.IHistoryTokoCashRepository;
import com.tokopedia.digital.tokocash.entity.ResponseHelpHistoryEntity;
import com.tokopedia.digital.tokocash.mapper.HelpHistoryDataMapper;
import com.tokopedia.digital.tokocash.mapper.TokoCashHistoryMapper;
import com.tokopedia.digital.tokocash.model.HelpHistoryTokoCash;
import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public class TokoCashHistoryInteractor implements ITokoCashHistoryInteractor {

    private final IHistoryTokoCashRepository repository;
    private CompositeSubscription compositeSubscription;
    private TokoCashHistoryMapper tokoCashHistoryMapper;
    private HelpHistoryDataMapper helpHistoryDataMapper;

    public TokoCashHistoryInteractor(IHistoryTokoCashRepository repository,
                                     CompositeSubscription compositeSubscription) {
        this.repository = repository;
        this.compositeSubscription = compositeSubscription;
        tokoCashHistoryMapper = new TokoCashHistoryMapper();
        helpHistoryDataMapper = new HelpHistoryDataMapper();
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
    public void getHelpListCategory(Subscriber<List<HelpHistoryTokoCash>> subscriber) {
        compositeSubscription.add(
                repository.getHelpHistoryData()
                        .map(helpHistoryDataMapper)
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }

    @Override
    public void postHelpHistory(Subscriber<Boolean> subscriber, String subject, String message, String category, String transactionId) {
        compositeSubscription.add(
                repository.submitHelpHistory(subject, message, category, transactionId)
                        .map(new Func1<ResponseHelpHistoryEntity, Boolean>() {
                            @Override
                            public Boolean call(ResponseHelpHistoryEntity responseHelpHistoryEntity) {
                                return responseHelpHistoryEntity != null;
                            }
                        })
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
