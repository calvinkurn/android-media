package com.tokopedia.topads.keyword.di.module;


import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsOldManagementApi;
import com.tokopedia.topads.keyword.data.repository.TopAdsKeywordRepositoryImpl;
import com.tokopedia.topads.keyword.data.source.KeywordDashboardDataSouce;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.domain.interactor.KeywordDashboardUseCase;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordListPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hendry on 5/18/2017.
 */

@TopAdsKeywordScope
@Module
public class TopAdsKeywordNewChooseModule {

    @TopAdsKeywordScope
    @Provides
    TopAdsOldManagementApi provideKeywordApi(@TopAdsQualifier Retrofit retrofit) {
        return retrofit.create(TopAdsOldManagementApi.class);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordRepository provideTopAdsKeywordRepository
            (KeywordDashboardDataSouce keywordDashboardDataSouce,
             ShopInfoRepository shopInfoRepository) {
        return new TopAdsKeywordRepositoryImpl(keywordDashboardDataSouce, shopInfoRepository);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordListPresenterImpl provideTopAdsKeywordListPresenter(
            KeywordDashboardUseCase keywordDashboardUseCase, TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase) {
        return new TopAdsKeywordListPresenterImpl(keywordDashboardUseCase, topAdsAddSourceTaggingUseCase);
    }
}