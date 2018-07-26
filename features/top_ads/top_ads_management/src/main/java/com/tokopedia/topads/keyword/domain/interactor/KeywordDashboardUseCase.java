package com.tokopedia.topads.keyword.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.domain.model.KeywordDashboardDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class KeywordDashboardUseCase extends UseCase<KeywordDashboardDomain> {
    private TopAdsKeywordRepository topAdsKeywordRepository;

    @Inject
    public KeywordDashboardUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            TopAdsKeywordRepository topAdsKeywordRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.topAdsKeywordRepository = topAdsKeywordRepository;
    }

    @Override
    public Observable<KeywordDashboardDomain> createObservable(RequestParams requestParams) {
        return topAdsKeywordRepository.getDashboardKeyword(requestParams);
    }
}
