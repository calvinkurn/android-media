package com.tokopedia.tkpd.home.feed.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.qualifier.MojitoQualifier;
import com.tokopedia.tkpd.home.feed.data.factory.RecentProductSourceFactory;
import com.tokopedia.tkpd.home.feed.data.mapper.RecentProductMapperResult;
import com.tokopedia.tkpd.home.feed.data.source.local.dbManager.RecentProductDbManager;
import com.tokopedia.tkpd.home.feed.di.scope.DataFeedScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author Kulomady on 1/9/17.
 */

@DataFeedScope
@Module
public class RecentProductModule {

    @DataFeedScope
    @Provides
    MojitoService provideRecentProductService(@MojitoQualifier Retrofit retrofit) {
        return retrofit.create(MojitoService.class);
    }

    @DataFeedScope
    @Provides
    RecentProductMapperResult provideRecentProductMapper(Gson gson) {
        return new RecentProductMapperResult(gson);
    }

    @DataFeedScope
    @Provides
    RecentProductDbManager provideDbManager() {
        return new RecentProductDbManager();
    }

    @DataFeedScope
    @Provides
    RecentProductSourceFactory provideDataSourceFactory(@ActivityContext Context context,
                                                        MojitoService mojitoService,
                                                        RecentProductDbManager recentDbManager,
                                                        RecentProductMapperResult mapperResult) {

        return new RecentProductSourceFactory(
                context,
                mojitoService,
                recentDbManager,
                mapperResult
        );
    }


}
