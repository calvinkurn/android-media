package com.tokopedia.topads.common.domain.interactor;

import com.tokopedia.topads.common.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nakama on 17/04/18.
 */

public class TopAdsRemoveSourceTaggingUseCase extends UseCase<Void> {

    private TopAdsSourceTaggingRepository topAdsSourceTaggingRepository;

    public TopAdsRemoveSourceTaggingUseCase(TopAdsSourceTaggingRepository topAdsSourceTaggingRepository) {
        this.topAdsSourceTaggingRepository = topAdsSourceTaggingRepository;
    }

    @Override
    public Observable<Void> createObservable(RequestParams requestParams) {
        return topAdsSourceTaggingRepository.deleteSource();
    }
}
