package com.tokopedia.topads.dashboard.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor;
import com.tokopedia.topads.common.data.util.CacheApiTKPDResponseValidator;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module
@TopAdsDashboardScope
public class TopAdsDashboardShopModule {
    @ShopQualifier
    @Provides
    public CacheApiInterceptor provideApiCacheInterceptor() {
        return new CacheApiInterceptor(new CacheApiTKPDResponseValidator<>(TkpdV4ResponseError.class));
    }

    @ShopQualifier
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @Provides
    public ShopAuthInterceptor provideShopAuthInterceptor(@ApplicationContext Context context,
                                                          AbstractionRouter abstractionRouter,
                                                          UserSession userSession){

        return new ShopAuthInterceptor(context, abstractionRouter, userSession);
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
    @Provides
    public Retrofit provideRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_URL).client(okHttpClient).build();
    }
}
