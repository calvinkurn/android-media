package com.tokopedia.core.referral.di;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.TokoCashSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.TokoCashMapper;
import com.tokopedia.core.drawer2.data.repository.TokoCashRepositoryImpl;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.referral.apis.ReferralApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.referral.data.ReferralDataRepository;
import com.tokopedia.core.referral.data.ReferralDataStoreFactory;
import com.tokopedia.core.referral.domain.GetReferralDataUseCase;
import com.tokopedia.core.referral.domain.ReferralRepository;
import com.tokopedia.core.util.SessionHandler;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by ashwanityagi on 22/01/18.
 */

@Module
public class ReferralModule {

    public ReferralModule() {

    }

    @Provides
    @ReferralScope
    ReferralApi provideReferralApi(@ReferralQualifier Retrofit retrofit) {
        return retrofit.create(ReferralApi.class);
    }

    @Provides
    @ReferralQualifier
    @ReferralScope
    Retrofit provideRideRetrofit(@ReferralQualifier OkHttpClient okHttpClient,
                                 Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.LIVE_DOMAIN).client(okHttpClient).build();
    }


    @ReferralQualifier
    @Provides
    @ReferralScope
    OkHttpClient provideOkHttpClient() {
        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy())
                .buildClientDefaultAuth();
    }


    @Provides
    @ReferralScope
    TokoCashSourceFactory provideTokoCashSourceFactory() {
        Bundle bundle = new Bundle();
        String authKey = SessionHandler.getAccessToken();
        authKey = "Bearer " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        AccountsService accountsService = new AccountsService(bundle);
        GlobalCacheManager walletCache = new GlobalCacheManager();

        return new TokoCashSourceFactory(
                MainApplication.getAppContext(),
                accountsService,
                new TokoCashMapper(),
                walletCache);

    }

    @Provides
    @ReferralScope
    GetReferralDataUseCase provideGetReferralDataUseCase(ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutionThread,
                                                         ReferralRepository referralRepository, @ApplicationContext Context context) {
        return new GetReferralDataUseCase(threadExecutor, postExecutionThread, referralRepository, context);
    }

    @Provides
    @ReferralScope
    ReferralDataStoreFactory provideReferralDataStoreFactory(ReferralApi referralApi) {
        return new ReferralDataStoreFactory(referralApi);
    }

    @Provides
    @ReferralScope
    ReferralRepository provideReferralRepository(ReferralDataStoreFactory referralDataStoreFactory) {
        return new ReferralDataRepository(referralDataStoreFactory);
    }


    @Provides
    @ReferralScope
    TokoCashRepository provideTokoCashRepository(TokoCashSourceFactory tokoCashSourceFactory) {
        return new TokoCashRepositoryImpl(tokoCashSourceFactory);
    }

    @Provides
    @ReferralScope
    TokoCashUseCase provideTokoCashUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread, TokoCashRepository tokoCashRepository, SessionHandler sessionHandler) {

        return new TokoCashUseCase(
                threadExecutor,
                postExecutionThread,
                tokoCashRepository
        );
    }


}
