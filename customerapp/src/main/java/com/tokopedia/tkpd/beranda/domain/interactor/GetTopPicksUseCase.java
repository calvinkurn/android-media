package com.tokopedia.tkpd.beranda.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksResponseModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class GetTopPicksUseCase extends UseCase<TopPicksResponseModel> {

    private final String KEY_RANDOM = "random";
    private final String KEY_DEVICE = "device";
    private final String KEY_SOURCE = "source";
    private final String DEFAULT_KEY_SOURCE = "home";
    private final String DEFAULT_KEY_DEVICE = "android";
    private final boolean DEFAULT_KEY_RANDOM = true;
    private final HomeRepository homeRepository;

    public GetTopPicksUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, HomeRepository homeRepository) {
        super(threadExecutor, postExecutionThread);
        this.homeRepository = homeRepository;
    }

    @Override
    public Observable<TopPicksResponseModel> createObservable(RequestParams requestParams) {
        return homeRepository.getTopPicks(requestParams);
    }

    public RequestParams getRequestParam() {
        RequestParams params = RequestParams.create();
        params.putBoolean(KEY_RANDOM, DEFAULT_KEY_RANDOM);
        params.putString(KEY_DEVICE, DEFAULT_KEY_DEVICE);
        params.putString(KEY_SOURCE, DEFAULT_KEY_SOURCE);
        return params;
    }
}
