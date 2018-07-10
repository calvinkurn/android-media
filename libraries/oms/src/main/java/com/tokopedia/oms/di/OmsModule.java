package com.tokopedia.oms.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.OmsInterceptor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.oms.OmsModuleRouter;
import com.tokopedia.oms.data.CloudOmsDataStore;
import com.tokopedia.oms.data.OmsRepositoryData;
import com.tokopedia.oms.data.source.OmsApi;
import com.tokopedia.oms.data.source.OmsBaseURL;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module
public class OmsModule {

    @Provides
    OmsApi provideOmsApi(@OmsQualifier Retrofit retrofit) {
        return retrofit.create(OmsApi.class);
    }


    @Provides
    @OmsQualifier
    Retrofit provideOmsRetrofit(@OmsQualifier OkHttpClient okHttpClient,
                                Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(OmsBaseURL.OMS_DOMAIN).client(okHttpClient).build();
    }

    @OmsQualifier
    @Provides
    OmsInterceptor provideRideInterCeptor(@ApplicationContext Context context) {
        String oAuthString = "Bearer " + SessionHandler.getAccessToken();
        return new OmsInterceptor(oAuthString, context);
    }

    @OmsQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor, @OmsQualifier OmsInterceptor authInterceptor, @ApplicationContext Context context) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(((OmsModuleRouter) context).getChuckInterceptor())
                .build();
    }

    @Provides
    OmsRepositoryData providesOmsRepository(OmsApi api) {
        return new OmsRepositoryData(new CloudOmsDataStore(api));
    }

    @Provides
    PostVerifyCartUseCase providesPostVerifyCartUseCase(
            OmsRepositoryData omsRepositoryData) {
        return new PostVerifyCartUseCase(omsRepositoryData);

    }
}