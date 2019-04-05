package com.tokopedia.home.beranda.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.beranda.di.HomeScope;
import com.tokopedia.home.beranda.di.ShopQualifier;
import com.tokopedia.home.beranda.di.ShopWSQualifier;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor;
import com.tokopedia.shop.common.data.repository.ShopCommonRepositoryImpl;
import com.tokopedia.shop.common.data.source.ShopCommonDataSource;
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonWSApi;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@HomeScope
@Module
public class ShopModule {


    @HomeScope
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @ShopQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(ShopAuthInterceptor shopAuthInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            ErrorResponseInterceptor errorResponseInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(shopAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopQualifier
    @HomeScope
    @Provides
    public Retrofit provideRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @HomeScope
    @Provides
    public ShopCommonApi provideShopCommonApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(ShopCommonApi.class);
    }

    @ShopWSQualifier
    @HomeScope
    @Provides
    public Retrofit provideWSRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                       Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_WS_URL).client(okHttpClient).build();
    }

    @HomeScope
    @Provides
    public ShopCommonWSApi provideShopCommonWsApi(@ShopWSQualifier Retrofit retrofit) {
        return retrofit.create(ShopCommonWSApi.class);
    }

    @HomeScope
    @Provides
    public ShopCommonCloudDataSource provideShopCommonCloudDataSource(ShopCommonApi shopCommonApi, ShopCommonWSApi shopCommonWS4Api, UserSessionInterface userSession) {
        return new ShopCommonCloudDataSource(shopCommonApi, shopCommonWS4Api, userSession);
    }

    @HomeScope
    @Provides
    public ShopCommonDataSource provideShopCommonDataSource(ShopCommonCloudDataSource shopInfoCloudDataSource) {
        return new ShopCommonDataSource(shopInfoCloudDataSource);
    }

    @HomeScope
    @Provides
    public ShopCommonRepository provideShopCommonRepository(ShopCommonDataSource shopInfoDataSource) {
        return new ShopCommonRepositoryImpl(shopInfoDataSource);
    }

    @HomeScope
    @Provides
    public GetShopInfoUseCase provideGetShopInfoUseCase(ShopCommonRepository shopCommonRepository) {
        return new GetShopInfoUseCase(shopCommonRepository);
    }

    @HomeScope
    @Provides
    public GetShopInfoByDomainUseCase provideGetShopInfoByDomainUseCase(ShopCommonRepository shopCommonRepository) {
        return new GetShopInfoByDomainUseCase(shopCommonRepository);
    }

    @HomeScope
    @Provides
    public ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase(@ApplicationContext Context context) {
        return new ToggleFavouriteShopUseCase(new GraphqlUseCase(), context.getResources());
    }
}