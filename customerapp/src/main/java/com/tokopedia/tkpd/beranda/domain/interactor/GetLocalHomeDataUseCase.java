package com.tokopedia.tkpd.beranda.domain.interactor;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.beranda.data.mapper.HomeDataMapper;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by errysuprayogion 12/8/17.
 */

public class GetLocalHomeDataUseCase extends UseCase<List<Visitable>> {

    private final HomeRepository homeRepository;
    private final HomeDataMapper homeDataMapper;

    public GetLocalHomeDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                   HomeRepository homeRepository, HomeDataMapper homeDataMapper) {
        super(threadExecutor, postExecutionThread);
        this.homeRepository = homeRepository;
        this.homeDataMapper = homeDataMapper;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return Observable.zip(
                homeRepository.getBannersCache(),
                homeRepository.getTickersCache(),
                homeRepository.getBrandsOfficialStoreCache(),
                homeRepository.getTopPicksCache(),
                homeRepository.getHomeCategorysCache(), homeDataMapper);
    }

}
