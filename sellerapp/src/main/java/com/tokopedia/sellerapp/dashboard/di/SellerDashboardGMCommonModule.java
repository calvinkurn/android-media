package com.tokopedia.sellerapp.dashboard.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.gm.common.data.interceptor.GMAuthInterceptor;
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl;
import com.tokopedia.gm.common.data.source.GMCommonDataSource;
import com.tokopedia.gm.common.data.source.cloud.GMCommonCloudDataSource;
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.seller.shop.common.interceptor.HeaderErrorResponseInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@SellerDashboardScope
@Module
public class SellerDashboardGMCommonModule {

    @Provides
    public CacheApiInterceptor provideCacheApiInterceptor() {
        return new CacheApiInterceptor();
    }

    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor();
    }

    @Provides
    public HeaderErrorResponseInterceptor provideHeaderErrorResponseInterceptor() {
        return new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class);
    }

    @Provides
    public GMAuthInterceptor provideGMAuthInterceptor(@ApplicationContext Context context,
                                                      AbstractionRouter abstractionRouter,
                                                      UserSession userSession) {
        return new GMAuthInterceptor(context, abstractionRouter, userSession);
    }

    @SellerDashboardGMQualifier
    @Provides
    public OkHttpClient provideGMOkHttpClient(GMAuthInterceptor gmAuthInterceptor,
                                              HttpLoggingInterceptor httpLoggingInterceptor,
                                              HeaderErrorResponseInterceptor errorResponseInterceptor,
                                              CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(gmAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @SellerDashboardGMQualifier
    @SellerDashboardScope
    @Provides
    public Retrofit provideGMRetrofit(@SellerDashboardGMQualifier OkHttpClient okHttpClient,
                                      Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @SellerDashboardScope
    @Provides
    public GMCommonApi provideGMCommonApi(@SellerDashboardGMQualifier Retrofit retrofit) {
        return retrofit.create(GMCommonApi.class);
    }

    @SellerDashboardScope
    @Provides
    public GMCommonCloudDataSource provideGMCommonCloudDataSource(GMCommonApi gmCommonApi) {
        return new GMCommonCloudDataSource(gmCommonApi);
    }

    @SellerDashboardScope
    @Provides
    public GMCommonDataSource provideGMCommonDataSource(GMCommonCloudDataSource gmCommonCloudDataSource) {
        return new GMCommonDataSource(gmCommonCloudDataSource);
    }

    @SellerDashboardScope
    @Provides
    public GMCommonRepository provideGMCommonRepository(GMCommonDataSource gmCommonDataSource) {
        return new GMCommonRepositoryImpl(gmCommonDataSource);
    }
}
