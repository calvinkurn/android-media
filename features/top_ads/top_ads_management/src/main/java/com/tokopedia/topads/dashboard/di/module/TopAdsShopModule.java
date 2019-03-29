package com.tokopedia.topads.dashboard.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor;
import com.tokopedia.topads.common.data.util.CacheApiTKPDResponseValidator;
import com.tokopedia.topads.dashboard.di.qualifier.ShopWsQualifier;
import com.tokopedia.topads.dashboard.di.scope.TopAdsScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by hadi.putra on 24/04/18.
 */

@TopAdsScope
@Module
public class TopAdsShopModule {

    @ShopQualifier
    @TopAdsScope
    @Provides
    public CacheApiInterceptor provideApiCacheInterceptor() {
        return new CacheApiInterceptor(new CacheApiTKPDResponseValidator<>(TkpdV4ResponseError.class));
    }

    @ShopQualifier
    @TopAdsScope
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @TopAdsScope
    @Provides
    public ShopAuthInterceptor provideShopAuthInterceptor(@ApplicationContext Context context,
                                                          AbstractionRouter abstractionRouter){

        return new ShopAuthInterceptor(context, abstractionRouter);
    }

    @ShopQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(ShopAuthInterceptor shopAuthInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            @ShopQualifier ErrorResponseInterceptor errorResponseInterceptor,
                                            CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(shopAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopQualifier
    @TopAdsScope
    @Provides
    public Retrofit provideRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @ShopWsQualifier
    @TopAdsScope
    @Provides
    public Retrofit provideWSRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                      Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_WS_URL).client(okHttpClient).build();
    }

    @TopAdsScope
    @Provides
    public ShopApi provideShopApi(@ShopWsQualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }
}
