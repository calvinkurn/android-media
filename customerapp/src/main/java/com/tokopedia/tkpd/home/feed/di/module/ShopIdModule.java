package com.tokopedia.tkpd.home.feed.di.module;

import android.content.Context;

import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.tkpd.home.feed.data.factory.HomeDataSourceFactory;
import com.tokopedia.tkpd.home.feed.data.mapper.GetShopIdMapper;
import com.tokopedia.tkpd.home.feed.di.scope.DataFeedScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author Kulomady on 1/9/17.
 */

@DataFeedScope
@Module
public class ShopIdModule {

    @DataFeedScope
    @Provides
    ServiceV4 provideHomeService(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(ServiceV4.class);
    }


    @DataFeedScope
    @Provides
    HomeDataSourceFactory provideHomeDataStoreFactory(@ActivityContext Context context,
                                                      ServiceV4 serviceV4,
                                                      GetShopIdMapper shopIdMapper) {

        return new HomeDataSourceFactory(
                context, serviceV4, shopIdMapper);
    }

    @DataFeedScope
    @Provides
    GetShopIdMapper provideShopIdMapper() {
        return new GetShopIdMapper();
    }

}
