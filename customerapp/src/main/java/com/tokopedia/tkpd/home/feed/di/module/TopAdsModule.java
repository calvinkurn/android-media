package com.tokopedia.tkpd.home.feed.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.tkpd.home.feed.data.factory.TopAdsDataSourceFactory;
import com.tokopedia.tkpd.home.feed.data.mapper.TopAdsMapper;
import com.tokopedia.core.base.common.dbManager.TopAdsDbManager;
import com.tokopedia.tkpd.home.feed.di.scope.DataFeedScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author Kulomady on 1/9/17.
 */

@Module
public class TopAdsModule {

    @DataFeedScope
    @Provides
    TopAdsService provideTopAdsService(@TopAdsQualifier Retrofit retrofit) {
        return retrofit.create(TopAdsService.class);
    }

    @DataFeedScope
    @Provides
    TopAdsMapper provideTopAdsMapperResult(Gson gson) {
        return new TopAdsMapper(gson);
    }

    @DataFeedScope
    @Provides
    TopAdsDbManager provideTopAdsDbManager() {
        return new TopAdsDbManager();
    }

    @DataFeedScope
    @Provides
    TopAdsDataSourceFactory provideTopAdsDataSourceFactory(TopAdsDbManager topAdsDbManager,
                                                           TopAdsService topAdsService,
                                                           TopAdsMapper topAdsMapper) {

        return new TopAdsDataSourceFactory(topAdsDbManager, topAdsService, topAdsMapper);
    }

}
