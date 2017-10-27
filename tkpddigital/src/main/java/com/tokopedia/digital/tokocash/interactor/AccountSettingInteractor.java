package com.tokopedia.digital.tokocash.interactor;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.digital.tokocash.domain.HistoryTokoCashRepository;
import com.tokopedia.digital.tokocash.mapper.AccountTokoCashMapper;
import com.tokopedia.digital.tokocash.mapper.OAuthInfoMapper;
import com.tokopedia.digital.tokocash.model.AccountTokoCash;
import com.tokopedia.digital.tokocash.model.OAuthInfo;

import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 10/26/17.
 */

public class AccountSettingInteractor implements IAccountSettingInteractor {

    private final HistoryTokoCashRepository repository;
    private final CompositeSubscription compositeSubscription;
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;
    private OAuthInfoMapper oAuthInfomapper;
    private AccountTokoCashMapper accountTokoCashMapper;

    public AccountSettingInteractor(HistoryTokoCashRepository repository,
                                    CompositeSubscription compositeSubscription,
                                    ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        this.repository = repository;
        this.compositeSubscription = compositeSubscription;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        oAuthInfomapper = new OAuthInfoMapper();
        accountTokoCashMapper = new AccountTokoCashMapper();
    }

    @Override
    public void getOAuthInfo(Subscriber<OAuthInfo> subscriber) {
        compositeSubscription.add(
                repository.getOAuthInfo()
                        .map(oAuthInfomapper)
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }

    @Override
    public void getLinkedAccountList(Subscriber<List<AccountTokoCash>> subscriber) {
        compositeSubscription.add(
                repository.getLinkedAccountList()
                        .map(accountTokoCashMapper)
                        .unsubscribeOn(Schedulers.from(threadExecutor))
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(subscriber));
    }

    @Override
    public void unlinkAccountTokoCash(Subscriber<Boolean> isSuccess, String userId) {

    }
}
