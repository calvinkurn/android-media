package com.tokopedia.topads.keyword.di.module;

import com.tokopedia.topads.common.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.domain.interactor.KeywordAddUseCase;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordAddPresenter;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordAddPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hendry on 5/18/2017.
 */

@TopAdsKeywordScope
@Module
public class TopAdsKeywordAddModule extends TopAdsKeywordModule {

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordAddPresenter provideTopAdsKeywordAddPresenter(
            KeywordAddUseCase keywordAddUseCase, TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase) {
        return new TopAdsKeywordAddPresenterImpl(keywordAddUseCase, topAdsGetSourceTaggingUseCase);
    }

}
