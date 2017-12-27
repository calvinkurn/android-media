package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.digital.tokocash.domain.IHistoryTokoCashRepository;
import com.tokopedia.digital.tokocash.entity.ResponseHelpHistoryEntity;
import com.tokopedia.digital.tokocash.mapper.HelpHistoryDataMapper;
import com.tokopedia.digital.tokocash.mapper.TokoCashHistoryMapper;
import com.tokopedia.digital.tokocash.mapper.WithdrawSaldoMapper;
import com.tokopedia.digital.tokocash.model.HelpHistoryTokoCash;
import com.tokopedia.digital.tokocash.model.ParamsActionHistory;
import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;
import com.tokopedia.digital.tokocash.model.WithdrawSaldo;

import java.util.List;

import rx.Subscriber;
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
    private WithdrawSaldoMapper withdrawSaldoMapper;
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;


    public TokoCashHistoryInteractor(IHistoryTokoCashRepository repository,
                                     CompositeSubscription compositeSubscription,
                                     JobExecutor jobExecutor,
                                     PostExecutionThread postExecutionThread) {
        this.repository = repository;
        this.compositeSubscription = compositeSubscription;
        tokoCashHistoryMapper = new TokoCashHistoryMapper();
        helpHistoryDataMapper = new HelpHistoryDataMapper();
        withdrawSaldoMapper = new WithdrawSaldoMapper();
        this.threadExecutor = jobExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    @Override
    public void getHistoryTokoCash(Subscriber<TokoCashHistoryData> subscriber, String type,
                                   String startDate, String endDate, int page) {
        compositeSubscription.add(
                repository.getTokoCashHistoryData(type, startDate, endDate, page)
                        .map(tokoCashHistoryMapper)
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }

    @Override
    public void getHelpListCategory(Subscriber<List<HelpHistoryTokoCash>> subscriber) {
        compositeSubscription.add(
                repository.getHelpHistoryData()
                        .map(helpHistoryDataMapper)
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
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
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }

    @Override
    public void postMoveToSaldo(Subscriber<WithdrawSaldo> subscriber, String url, ParamsActionHistory paramsActionHistory) {
        compositeSubscription.add(
                repository.moveToSaldo(url, paramsActionHistory)
                        .map(withdrawSaldoMapper)
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }
}
