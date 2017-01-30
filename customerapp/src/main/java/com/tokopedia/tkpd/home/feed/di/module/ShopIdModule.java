package com.tokopedia.tkpd.home.feed.di.module;

import android.content.Context;

import com.tokopedia.core.base.common.service.ServiceVersion4;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.qualifier.BaseDomainQualifier;
import com.tokopedia.tkpd.home.feed.data.factory.HomeDataSourceFactory;
import com.tokopedia.tkpd.home.feed.data.mapper.GetShopIdMapperResult;
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
    ServiceVersion4 provideHomeService(@BaseDomainQualifier Retrofit retrofit) {
        return retrofit.create(ServiceVersion4.class);
    }


    @DataFeedScope
    @Provides
    HomeDataSourceFactory provideHomeDataStoreFactory(@ActivityContext Context context,
                                                      ServiceVersion4 serviceVersion4,
                                                      GetShopIdMapperResult shopIdMapper) {

        return new HomeDataSourceFactory(
                context, serviceVersion4, shopIdMapper);
    }

    @DataFeedScope
    @Provides
    GetShopIdMapperResult provideShopIdMapper() {
        return new GetShopIdMapperResult();
    }

}
