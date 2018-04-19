package com.tokopedia.topads.common.sourcetagging.domain.interactor;

import com.tokopedia.topads.common.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.common.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsGetSourceTaggingUseCase extends UseCase<TopAdsSourceTaggingModel> {

    private TopAdsSourceTaggingRepository topAdsSourceTaggingRepository;

    @Inject
    public TopAdsGetSourceTaggingUseCase(TopAdsSourceTaggingRepository topAdsSourceTaggingRepository) {
        this.topAdsSourceTaggingRepository = topAdsSourceTaggingRepository;
    }

    @Override
    public Observable<TopAdsSourceTaggingModel> createObservable(RequestParams requestParams) {
        return topAdsSourceTaggingRepository.getSource();
    }
}
