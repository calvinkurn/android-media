package com.tokopedia.digital_deals.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.repository.ProfileRepositoryImpl;
import com.tokopedia.core.drawer2.domain.ProfileRepository;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital_deals.data.DealsDataStoreFactory;
import com.tokopedia.digital_deals.data.DealsRepositoryData;
import com.tokopedia.digital_deals.data.source.DealsApi;
import com.tokopedia.digital_deals.di.scope.DealsScope;
import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.GetAllBrandsUseCase;
import com.tokopedia.digital_deals.domain.GetBrandDetailsUseCase;
import com.tokopedia.digital_deals.domain.GetCategoryDetailRequestUseCase;
import com.tokopedia.digital_deals.domain.GetDealDetailsUseCase;
import com.tokopedia.digital_deals.domain.GetDealsListRequestUseCase;
import com.tokopedia.digital_deals.domain.GetLocationListRequestUseCase;
import com.tokopedia.digital_deals.domain.GetNextBrandPageUseCase;
import com.tokopedia.digital_deals.domain.GetNextCategoryPageUseCase;
import com.tokopedia.digital_deals.domain.GetNextDealPageUseCase;
import com.tokopedia.digital_deals.domain.GetSearchDealsListRequestUseCase;
import com.tokopedia.digital_deals.domain.GetSearchNextUseCase;
import com.tokopedia.oms.di.OmsModule;
import com.tokopedia.oms.di.OmsQualifier;
import com.tokopedia.oms.domain.OmsRepository;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module(includes = OmsModule.class)
public class DealsModule {
    Context thisContext;

    public DealsModule(Context context) {
        thisContext = context;
    }

    @Provides
    @DealsScope
    DealsApi provideDealsApi(@DealsQualifier Retrofit retrofit) {
        return retrofit.create(DealsApi.class);
    }

    @Provides
    @DealsScope
    DealsDataStoreFactory provideDealsDataStoreFactory(DealsApi dealsApi) {
        return new DealsDataStoreFactory(dealsApi);
    }

    @Provides
    @DealsScope
    DealsRepository provideDealsRepository(DealsDataStoreFactory dealsDataStoreFactory) {
        return new DealsRepositoryData(dealsDataStoreFactory);
    }


    @Provides
    @DealsScope
    GetDealsListRequestUseCase provideGetDealsListRequestUseCase(DealsRepository dealsRepository) {
        return new GetDealsListRequestUseCase(dealsRepository);
    }

    @Provides
    @DealsScope
    GetSearchDealsListRequestUseCase provideGetSearchDealsListRequestUseCase(DealsRepository dealsRepository) {
        return new GetSearchDealsListRequestUseCase(dealsRepository);
    }

    @Provides
    @DealsScope
    GetSearchNextUseCase providesGetSearchNextUseCase(DealsRepository dealsRepository) {
        return new GetSearchNextUseCase(dealsRepository);
    }

    @Provides
    @DealsScope
    GetBrandDetailsUseCase provideGetBrandDetailsUseCase(DealsRepository dealsRepository) {
        return new GetBrandDetailsUseCase(dealsRepository);
    }

    @Provides
    @DealsScope
    GetDealDetailsUseCase provideGetDealDetailsUseCase(DealsRepository dealsRepository) {
        return new GetDealDetailsUseCase(dealsRepository);
    }

    @Provides
    @DealsScope
    GetLocationListRequestUseCase provideGetLocationListRequestUseCase(DealsRepository dealsRepository) {
        return new GetLocationListRequestUseCase(dealsRepository);
    }

    @Provides
    @DealsScope
    GetCategoryDetailRequestUseCase provideGetCategoryDetailRequestUseCase(DealsRepository dealsRepository) {
        return new GetCategoryDetailRequestUseCase(dealsRepository);
    }

    @Provides
    @DealsScope
    GetNextCategoryPageUseCase provideGetNextCategoryPageUseCase(DealsRepository dealsRepository) {
        return new GetNextCategoryPageUseCase(dealsRepository);
    }

    @Provides
    @DealsScope
    GetAllBrandsUseCase provideGetAllBrandsUseCase(DealsRepository dealsRepository) {
        return new GetAllBrandsUseCase(dealsRepository);
    }

    @Provides
    @DealsScope
    GetNextBrandPageUseCase provideGetNextBrandPageUseCase(DealsRepository dealsRepository) {
        return new GetNextBrandPageUseCase(dealsRepository);
    }

    @Provides
    @DealsScope
    GetNextDealPageUseCase provideGetNextDealPageUseCase(DealsRepository dealsRepository) {
        return new GetNextDealPageUseCase(dealsRepository);
    }

    @Provides
    @DealsScope
    ProfileSourceFactory providesProfileSourceFactory(Context context, SessionHandler sessionHandler) {
        return new ProfileSourceFactory(context,
                new PeopleService(),
                new ProfileMapper(),
                new GlobalCacheManager(),
                sessionHandler);
    }

    @Provides
    @DealsScope
    ProfileRepository providesProfileRepository(ProfileSourceFactory profileSourceFactory) {
        return new ProfileRepositoryImpl(profileSourceFactory);
    }

    @Provides
    @DealsScope
    ProfileUseCase providesProfileUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          ProfileRepository profileRepository) {
        return new ProfileUseCase(
                threadExecutor, postExecutionThread, profileRepository);
    }

    @Provides
    ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    @Provides
    PostExecutionThread providePostExecutionThread() {
        return new UIThread();
    }

    @Provides
    public SessionHandler provideSessionHandler(@DealsScope Context context){
        return new SessionHandler(context);
    }

    @Provides
    @DealsQualifier
    @DealsScope
    Retrofit provideDealsRetrofit(@DealsQualifier OkHttpClient okHttpClient,
                                  Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.DEALS_DOMAIN).client(okHttpClient).build();
    }

    @DealsQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor, TkpdAuthInterceptor authInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @DealsScope
    Context getActivityContext() {
        return thisContext;
    }
}
