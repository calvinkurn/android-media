package com.tokopedia.tkpd.beranda.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;
import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerResponseModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class GetHomeBannerUseCase extends UseCase<HomeBannerResponseModel> {

    private final String PAGE_SIZE = "page[size]";
    private final String FILTER_STATE = "filter[state]";
    private final String FILTER_EXPIRED = "filter[expired]";
    private final String PAGE_NUMBER = "page[number]";
    private final String FILTER_DEVICE = "filter[device]";

    private final HomeRepository homeRepository;

    public GetHomeBannerUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, HomeRepository homeRepository) {
        super(threadExecutor, postExecutionThread);
        this.homeRepository = homeRepository;
    }

    @Override
    public Observable<HomeBannerResponseModel> createObservable(RequestParams requestParams) {
        return homeRepository.getBanners(requestParams);
    }

    public RequestParams getRequestParam() {
        RequestParams params = RequestParams.create();
        params.putString(PAGE_SIZE, CategoryApi.size);
        params.putString(FILTER_STATE, CategoryApi.state);
        params.putString(FILTER_EXPIRED, CategoryApi.expired);
        params.putString(PAGE_NUMBER, CategoryApi.number);
        params.putString(FILTER_DEVICE, CategoryApi.ANDROID_DEVICE);
        return params;
    }
}
