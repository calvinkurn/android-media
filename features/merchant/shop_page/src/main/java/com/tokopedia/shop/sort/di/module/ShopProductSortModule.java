package com.tokopedia.shop.sort.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor;
import com.tokopedia.shop.sort.data.repository.ShopProductSortRepositoryImpl;
import com.tokopedia.shop.sort.data.source.cloud.ShopProductSortCloudDataSource;
import com.tokopedia.shop.sort.data.source.cloud.api.ShopAceApi;
import com.tokopedia.shop.sort.di.scope.ShopProductSortScope;
import com.tokopedia.shop.sort.domain.repository.ShopProductSortRepository;
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@ShopProductSortScope
@Module
public class ShopProductSortModule {

    @Provides
    public OkHttpClient provideOkHttpClient(ShopAuthInterceptor shopAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor,
                                            CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(shopAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopProductSortScope
    @Provides
    public Retrofit provideShopAceRetrofit(OkHttpClient okHttpClient,
                                           Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopUrl.BASE_ACE_URL).client(okHttpClient).build();
    }

    @ShopProductSortScope
    @Provides
    public ShopAceApi provideShopAceApi(Retrofit retrofit) {
        return retrofit.create(ShopAceApi.class);
    }

    @ShopProductSortScope
    @Provides
    public ShopProductSortRepository provideShopProductSortRepository(ShopProductSortCloudDataSource shopProductDataSource) {
        return new ShopProductSortRepositoryImpl(shopProductDataSource);
    }

    @ShopProductSortScope
    @Provides
    public ShopProductSortMapper provideShopProductSortMapper() {
        return new ShopProductSortMapper();
    }

    @ShopProductSortScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}

