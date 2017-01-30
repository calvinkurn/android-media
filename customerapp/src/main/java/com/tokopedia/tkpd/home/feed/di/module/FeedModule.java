package com.tokopedia.tkpd.home.feed.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.AceService;
import com.tokopedia.core.base.di.qualifier.AceQualifier;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.tkpd.home.feed.data.factory.FeedDataSourceFactory;
import com.tokopedia.tkpd.home.feed.data.mapper.FeedMapperResult;
import com.tokopedia.tkpd.home.feed.data.source.local.dbManager.FeedDbManager;
import com.tokopedia.tkpd.home.feed.di.scope.DataFeedScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author Kulomady on 1/9/17.
 */

@DataFeedScope
@Module
public class FeedModule {

    @DataFeedScope
    @Provides
    AceService provideFeedService(@AceQualifier Retrofit retrofit) {
        return retrofit.create(AceService.class);
    }

    @DataFeedScope
    @Provides
    FeedMapperResult provideFeedMapper(Gson gson) {
        return new FeedMapperResult(gson);
    }

    @DataFeedScope
    @Provides
    FeedDbManager provideFeedDbManager() {
        return new FeedDbManager();
    }

    @DataFeedScope
    @Provides
    FeedDataSourceFactory provideFeedDataStoreFactory(@ActivityContext Context context,
                                                      AceService aceService,
                                                      FeedMapperResult feedMapperResult,
                                                      FeedDbManager feedDbManager) {

        return new FeedDataSourceFactory(context, aceService, feedMapperResult, feedDbManager);
    }

}
