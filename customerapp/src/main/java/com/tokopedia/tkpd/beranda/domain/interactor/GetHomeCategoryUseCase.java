package com.tokopedia.tkpd.beranda.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;
import com.tokopedia.tkpd.beranda.domain.model.category.HomeCategoryResponseModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class GetHomeCategoryUseCase extends UseCase<HomeCategoryResponseModel> {

    private final HomeRepository homeRepository;


    public GetHomeCategoryUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, HomeRepository homeRepository) {
        super(threadExecutor, postExecutionThread);
        this.homeRepository = homeRepository;
    }

    @Override
    public Observable<HomeCategoryResponseModel> createObservable(RequestParams requestParams) {
        return homeRepository.getHomeCategorys();
    }
}
