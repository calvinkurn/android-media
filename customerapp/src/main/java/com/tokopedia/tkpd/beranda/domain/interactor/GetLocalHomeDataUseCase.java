package com.tokopedia.tkpd.beranda.domain.interactor;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.beranda.data.mapper.HomeMapper;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by henrypriyono on 01/02/18.
 */

public class GetLocalHomeDataUseCase extends UseCase<List<Visitable>> {

    private final HomeRepository homeRepository;

    public GetLocalHomeDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                   HomeRepository homeRepository) {
        super(threadExecutor, postExecutionThread);
        this.homeRepository = homeRepository;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return homeRepository.getHomeDataCache();
    }
}

