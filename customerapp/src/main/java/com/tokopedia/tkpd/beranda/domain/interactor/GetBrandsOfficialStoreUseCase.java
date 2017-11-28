package com.tokopedia.tkpd.beranda.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class GetBrandsOfficialStoreUseCase extends UseCase<BrandsOfficialStoreResponseModel> {

    private final HomeRepository homeRepository;

    public GetBrandsOfficialStoreUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, HomeRepository homeRepository) {
        super(threadExecutor, postExecutionThread);
        this.homeRepository = homeRepository;
    }

    @Override
    public Observable<BrandsOfficialStoreResponseModel> createObservable(RequestParams requestParams) {
        return homeRepository.getBrandsOfficialStore();
    }
}
