package com.tokopedia.tkpd.beranda.domain.interactor;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class GetHomeDataUseCase extends UseCase<List<Visitable>> {
    private final HomeRepository repository;

    public GetHomeDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, HomeRepository homeRepository) {
        super(threadExecutor, postExecutionThread);
        this.repository = homeRepository;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return repository.getAllHomeData();
    }
}
