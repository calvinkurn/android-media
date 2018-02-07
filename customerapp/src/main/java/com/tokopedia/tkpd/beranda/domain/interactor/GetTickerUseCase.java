package com.tokopedia.tkpd.beranda.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class GetTickerUseCase extends UseCase<Ticker> {

    private final HomeRepository repository;

    public GetTickerUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, HomeRepository homeRepository) {
        super(threadExecutor, postExecutionThread);
        this.repository = homeRepository;
    }

    @Override
    public Observable<Ticker> createObservable(RequestParams requestParams) {
        return repository.getTickers();
    }
}
