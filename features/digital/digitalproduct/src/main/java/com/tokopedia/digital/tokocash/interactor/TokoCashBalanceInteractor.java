package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.digital.tokocash.domain.ITokoCashRepository;
import com.tokopedia.digital.tokocash.entity.WalletTokenEntity;
import com.tokopedia.digital.tokocash.model.WalletToken;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashBalanceData;

import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 9/4/17.
 */

public class TokoCashBalanceInteractor implements ITokoCashBalanceInteractor {

    private final ITokoCashRepository repository;

    private final CompositeSubscription compositeSubscription;

    private ThreadExecutor threadExecutor;

    private PostExecutionThread postExecutionThread;

    public TokoCashBalanceInteractor(ITokoCashRepository repository,
                                     CompositeSubscription compositeSubscription,
                                     JobExecutor jobExecutor,
                                     PostExecutionThread postExecutionThread) {
        this.repository = repository;
        this.compositeSubscription = compositeSubscription;
        this.threadExecutor = jobExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    @Override
    public void getBalanceTokoCash(Subscriber<TokoCashBalanceData> subscriber) {
        compositeSubscription.add(
                repository.getBalanceTokoCash()
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }

    @Override
    public void getTokenWallet(Subscriber<WalletToken> subscriber) {
        compositeSubscription.add(
                repository.getWalletToken()
                        .map(new Func1<WalletTokenEntity, WalletToken>() {
                            @Override
                            public WalletToken call(WalletTokenEntity walletTokenEntity) {
                                WalletToken walletToken = new WalletToken();
                                walletToken.setToken(walletTokenEntity.getToken());
                                return walletToken;
                            }
                        })
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }
}
