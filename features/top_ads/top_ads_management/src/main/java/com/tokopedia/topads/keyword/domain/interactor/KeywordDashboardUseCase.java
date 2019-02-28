package com.tokopedia.topads.keyword.domain.interactor;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.domain.model.KeywordDashboardDomain;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class KeywordDashboardUseCase extends UseCase<KeywordDashboardDomain> {
    private TopAdsKeywordRepository topAdsKeywordRepository;

    @Inject
    public KeywordDashboardUseCase(TopAdsKeywordRepository topAdsKeywordRepository) {
        super();
        this.topAdsKeywordRepository = topAdsKeywordRepository;
    }

    @Override
    public Observable<KeywordDashboardDomain> createObservable(RequestParams requestParams) {
        return topAdsKeywordRepository.getDashboardKeyword(requestParams);
    }
}
