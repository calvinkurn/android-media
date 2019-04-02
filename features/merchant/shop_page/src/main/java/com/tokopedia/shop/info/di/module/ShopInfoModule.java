package com.tokopedia.shop.info.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.reputation.common.constant.ReputationCommonUrl;
import com.tokopedia.reputation.common.data.interceptor.ReputationAuthInterceptor;
import com.tokopedia.reputation.common.data.repository.ReputationCommonRepositoryImpl;
import com.tokopedia.reputation.common.data.source.ReputationCommonDataSource;
import com.tokopedia.reputation.common.data.source.cloud.ReputationCommonCloudDataSource;
import com.tokopedia.reputation.common.data.source.cloud.api.ReputationCommonApi;
import com.tokopedia.reputation.common.domain.interactor.GetReputationSpeedDailyUseCase;
import com.tokopedia.reputation.common.domain.interactor.GetReputationSpeedUseCase;
import com.tokopedia.reputation.common.domain.repository.ReputationCommonRepository;
import com.tokopedia.shop.info.di.scope.ShopInfoScope;
import com.tokopedia.shop.note.data.repository.ShopNoteRepositoryImpl;
import com.tokopedia.shop.note.data.source.ShopNoteDataSource;
import com.tokopedia.shop.note.domain.repository.ShopNoteRepository;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;
import com.tokopedia.shop.page.di.ShopInfoReputationSpeedQualifier;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@ShopInfoScope
@Module
public class ShopInfoModule {
    @Provides
    public ReputationAuthInterceptor provideReputationAuthInterceptor(@ApplicationContext Context context,
                                                                      AbstractionRouter abstractionRouter) {
        return new ReputationAuthInterceptor(context, abstractionRouter);
    }

    @ShopInfoReputationSpeedQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(ReputationAuthInterceptor reputationAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor,
                                            CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(reputationAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopInfoReputationSpeedQualifier
    @ShopInfoScope
    @Provides
    public Retrofit provideRetrofit(@ShopInfoReputationSpeedQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ReputationCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @ShopInfoScope
    @Provides
    public ShopNoteRepository provideShopNoteRepository(ShopNoteDataSource shopNoteDataSource){
        return new ShopNoteRepositoryImpl(shopNoteDataSource);
    }

    @ShopInfoScope
    @Provides
    public ShopNoteViewModel provideShopNoteViewModel(){
        return new ShopNoteViewModel();
    }

    @ShopInfoScope
    @Provides
    public ReputationCommonApi provideReputationCommonApi(@ShopInfoReputationSpeedQualifier Retrofit retrofit) {
        return retrofit.create(ReputationCommonApi.class);
    }

    @ShopInfoScope
    @Provides
    public ReputationCommonCloudDataSource provideReputationCommonCloudDataSource(ReputationCommonApi reputationCommonApi) {
        return new ReputationCommonCloudDataSource(reputationCommonApi);
    }

    @ShopInfoScope
    @Provides
    public ReputationCommonDataSource provideReputationCommonDataSource(ReputationCommonCloudDataSource reputationCommonCloudDataSource) {
        return new ReputationCommonDataSource(reputationCommonCloudDataSource);
    }

    @ShopInfoScope
    @Provides
    public ReputationCommonRepository provideReputationCommonRepository(ReputationCommonDataSource reputationCommonDataSource) {
        return new ReputationCommonRepositoryImpl(reputationCommonDataSource);
    }

    @ShopInfoScope
    @Provides
    public GetReputationSpeedUseCase provideGetGetReputationSpeedUseCase(ReputationCommonRepository reputationCommonRepository) {
        return new GetReputationSpeedUseCase(reputationCommonRepository);
    }

    @ShopInfoScope
    @Provides
    public GetReputationSpeedDailyUseCase provideGetReputationSpeedDailyUseCase(ReputationCommonRepository reputationCommonRepository) {
        return new GetReputationSpeedDailyUseCase(reputationCommonRepository);
    }

    @ShopInfoScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}

