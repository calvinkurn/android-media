package com.tokopedia.topads.dashboard.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.product.edit.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.product.edit.common.data.source.cloud.ShopApi;
import com.tokopedia.product.edit.common.domain.repository.ShopInfoRepository;
import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.common.data.api.TopAdsManagementApi;
import com.tokopedia.topads.common.data.interceptor.TopAdsAuthInterceptor;
import com.tokopedia.topads.common.data.interceptor.TopAdsResponseError;
import com.tokopedia.topads.common.data.util.CacheApiTKPDResponseValidator;
import com.tokopedia.topads.dashboard.data.repository.GetDepositTopAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.topads.dashboard.data.source.GetDepositTopadsDataSource;
import com.tokopedia.topads.dashboard.data.source.ShopInfoDataSource;
import com.tokopedia.topads.dashboard.data.source.cloud.ShopInfoCloud;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsOldManagementApi;
import com.tokopedia.topads.dashboard.di.qualifier.ShopWsQualifier;
import com.tokopedia.topads.dashboard.di.qualifier.TopAdsManagementQualifier;
import com.tokopedia.topads.dashboard.di.scope.TopAdsScope;
import com.tokopedia.topads.dashboard.domain.GetDepositTopAdsRepository;
import com.tokopedia.topads.dashboard.domain.interactor.GetDepositTopAdsUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

@TopAdsScope
@Module
public class TopAdsModule {

    @TopAdsScope
    @Provides
    public ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @TopAdsScope
    @Provides
    public PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @TopAdsScope
    @Provides
    public TopAdsAuthInterceptor provideTopAdsAuthTempInterceptor(@ApplicationContext Context context,
                                                                  AbstractionRouter abstractionRouter,
                                                                  UserSession userSession){
        return new TopAdsAuthInterceptor(context, abstractionRouter, userSession);
    }

    @TopAdsScope
    @Provides
    public CacheApiInterceptor provideApiCacheInterceptor() {
        return new CacheApiInterceptor(new CacheApiTKPDResponseValidator<>(TopAdsResponseError.class));
    }

    @TopAdsManagementQualifier
    @TopAdsScope
    @Provides
    public ErrorResponseInterceptor provideErrorInterceptor(){
        return new ErrorResponseInterceptor(TopAdsResponseError.class);
    }

    @TopAdsManagementQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(TopAdsAuthInterceptor topAdsAuthInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            @TopAdsManagementQualifier ErrorResponseInterceptor errorResponseInterceptor,
                                            CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @TopAdsManagementQualifier
    @TopAdsScope
    @Provides
    public Retrofit provideRetrofit(@TopAdsManagementQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TopAdsCommonConstant.BASE_DOMAIN_URL).client(okHttpClient).build();
    }


    @TopAdsScope
    @Provides
    public GetDepositTopAdsUseCase provideGetDepositTopAdsUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                                  GetDepositTopAdsRepository getDepositTopAdsRepository) {
        return new GetDepositTopAdsUseCase(threadExecutor, postExecutionThread, getDepositTopAdsRepository);
    }

    @TopAdsScope
    @Provides
    public GetDepositTopAdsRepository provideGetDepositTopAdsRepository(GetDepositTopadsDataSource getDepositTopadsDataSource){
        return new GetDepositTopAdsRepositoryImpl(getDepositTopadsDataSource);
    }

    @TopAdsScope
    @Provides
    public TopAdsOldManagementApi provideTopAdsOldManagementApi(@TopAdsManagementQualifier Retrofit retrofit){
        return retrofit.create(TopAdsOldManagementApi.class);
    }

    @Provides
    @TopAdsScope
    public TopAdsManagementApi provideTopAdsManagementApi(@TopAdsManagementQualifier Retrofit retrofit){
        return retrofit.create(TopAdsManagementApi.class);
    }

    @TopAdsScope
    @Provides
    public ShopInfoCloud provideShopInfoCloud(@ApplicationContext Context context, ShopApi shopApi){
        return new ShopInfoCloud(context, shopApi);
    }

    @TopAdsScope
    @Provides
    public ShopInfoDataSource provideShopInfoDataSource(ShopInfoCloud shopInfoCloud, SimpleDataResponseMapper<ShopModel> mapper){
        return new ShopInfoDataSource(shopInfoCloud, mapper);
    }

    @TopAdsScope
    @Provides
    public ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @TopAdsScope
    @Provides
    public ShopApi provideShopApi(@ShopWsQualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }
}
