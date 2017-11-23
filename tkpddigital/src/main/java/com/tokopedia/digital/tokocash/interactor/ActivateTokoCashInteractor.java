package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.digital.tokocash.domain.TokoCashRepository;
import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;

import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class ActivateTokoCashInteractor implements IActivateTokoCashInteractor {

    private final TokoCashRepository repository;
    private final CompositeSubscription compositeSubscription;
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;

    public ActivateTokoCashInteractor(CompositeSubscription compositeSubscription,
                                      TokoCashRepository repository,
                                      JobExecutor jobExecutor,
                                      PostExecutionThread postExecutionThread) {
        this.repository = repository;
        this.compositeSubscription = compositeSubscription;
        this.threadExecutor = jobExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    @Override
    public void requestOTPWallet(Subscriber<ActivateTokoCashData> subscriber) {
        compositeSubscription.add(
                repository.requestOTPWallet()
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void activateTokoCash(String otpCode, Subscriber<ActivateTokoCashData> subscriber) {
        compositeSubscription.add(
                repository.linkedWalletToTokoCash(otpCode)
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber)
        );
    }
}