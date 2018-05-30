package com.tokopedia.topads.sourcetagging.domain.interactor;

import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsCheckTimeAndSaveSourceTaggingUseCase extends UseCase<Void> {
    private TopAdsSourceTaggingRepository topAdsSourceTaggingRepository;

    @Inject
    public TopAdsCheckTimeAndSaveSourceTaggingUseCase(TopAdsSourceTaggingRepository topAdsSourceTaggingRepository) {
        this.topAdsSourceTaggingRepository = topAdsSourceTaggingRepository;
    }

    @Override
    public Observable<Void> createObservable(RequestParams requestParams) {
        return topAdsSourceTaggingRepository.checkTimeAndSaveSource(requestParams);
    }

    public static RequestParams createRequestParams(@TopAdsSourceOption String source){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE, source);

        return requestParams;
    }
}
