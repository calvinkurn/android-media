package com.tokopedia.product.manage.item.common.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.retrofit.interceptors.BearerInterceptor;
import com.tokopedia.product.manage.item.common.data.source.ShopInfoDataSource;
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi;
import com.tokopedia.product.manage.item.common.data.source.cloud.TomeProductApi;
import com.tokopedia.product.manage.item.common.di.scope.ProductScope;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductScope
@Module
public class ProductModule {


    @ProductScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager(){
        return new GlobalCacheManager();
    }

    @ProductScope
    @Provides
    public ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @ProductScope
    @Provides
    public ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @ProductScope
    @Provides
    TomeProductApi provideTomeApi(@ProductTomeQualifier Retrofit retrofit){
        return retrofit.create(TomeProductApi.class);
    }

    @ProductTomeQualifier
    @ProductScope
    @Provides
    Retrofit provideRetrofit(Retrofit.Builder retrofitBuilder, @ProductTomeQualifier OkHttpClient okHttpClient){
        return retrofitBuilder.baseUrl(TkpdBaseURL.TOME_DOMAIN).client(okHttpClient).build();
    }

    @ProductTomeQualifier
    @ProductScope
    @Provides
    public OkHttpClient provideOkHttpClientTomeBearerAuth(@ProductTomeQualifier HttpLoggingInterceptor httpLoggingInterceptor,
                                                          BearerInterceptor bearerInterceptor,
                                                          @ProductTomeQualifier ErrorResponseInterceptor errorResponseInterceptor
    ) {
        return new OkHttpClient.Builder()
                .addInterceptor(bearerInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ProductTomeQualifier
    @ProductScope
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    @ProductTomeQualifier
    @ProductScope
    @Provides
    public ErrorResponseInterceptor provideResponseInterceptor() {
        return new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class);
    }

    @ProductScope
    @Provides
    public UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}