package com.tokopedia.topads.common.domain.interactor;

import com.tokopedia.topads.common.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.common.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nakama on 17/04/18.
 */

public class TopAdsGetSourceTaggingUseCase extends UseCase<TopAdsSourceTaggingModel> {

    private TopAdsSourceTaggingRepository topAdsSourceTaggingRepository;

    public TopAdsGetSourceTaggingUseCase(TopAdsSourceTaggingRepository topAdsSourceTaggingRepository) {
        this.topAdsSourceTaggingRepository = topAdsSourceTaggingRepository;
    }

    @Override
    public Observable<TopAdsSourceTaggingModel> createObservable(RequestParams requestParams) {
        return topAdsSourceTaggingRepository.getSource();
    }
}
