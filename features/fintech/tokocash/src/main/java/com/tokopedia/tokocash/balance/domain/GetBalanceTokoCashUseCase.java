package com.tokopedia.tokocash.balance.domain;

import com.tokopedia.tokocash.balance.data.repository.BalanceRepository;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class GetBalanceTokoCashUseCase extends UseCase<BalanceTokoCash> {

    private BalanceRepository repository;

    @Inject
    public GetBalanceTokoCashUseCase(BalanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<BalanceTokoCash> createObservable(RequestParams requestParams) {
        return repository.getLocalBalanceTokoCash()
                .onErrorResumeNext(new Func1<Throwable, Observable<BalanceTokoCash>>() {
                    @Override
                    public Observable<BalanceTokoCash> call(Throwable throwable) {
                        return repository.getBalanceTokoCash();
                    }
                });
    }
}
