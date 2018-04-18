package com.tokopedia.topads.common.domain.interactor;

import com.tokopedia.topads.common.constant.TopAdsConstant;
import com.tokopedia.topads.common.constant.TopAdsSourceOption;
import com.tokopedia.topads.common.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsAddSourceTaggingUseCase extends UseCase<Void>{
    private TopAdsSourceTaggingRepository topAdsSourceTaggingRepository;

    @Inject
    public TopAdsAddSourceTaggingUseCase(TopAdsSourceTaggingRepository topAdsSourceTaggingRepository) {
        this.topAdsSourceTaggingRepository = topAdsSourceTaggingRepository;
    }

    @Override
    public Observable<Void> createObservable(RequestParams requestParams) {
        return topAdsSourceTaggingRepository.saveSource(requestParams);
    }

    public static RequestParams createRequestParams(@TopAdsSourceOption String source, long timestamp){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsConstant.PARAM_KEY_SOURCE, source);
        requestParams.putLong(TopAdsConstant.PARAM_KEY_TIMESTAMP, timestamp);

        return requestParams;
    }
}
