package com.tokopedia.topads.sourcetagging.domain.interactor;

import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nakama on 17/04/18.
 */

public class TopAdsRemoveSourceTaggingUseCase extends UseCase<Void> {

    private TopAdsSourceTaggingRepository topAdsSourceTaggingRepository;

    @Inject
    public TopAdsRemoveSourceTaggingUseCase(TopAdsSourceTaggingRepository topAdsSourceTaggingRepository) {
        this.topAdsSourceTaggingRepository = topAdsSourceTaggingRepository;
    }

    @Override
    public Observable<Void> createObservable(RequestParams requestParams) {
        return topAdsSourceTaggingRepository.deleteSource();
    }
}
