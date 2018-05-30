package com.tokopedia.oms.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.interceptors.EventInerceptors;
import com.tokopedia.core.network.retrofit.interceptors.RideInterceptor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.oms.data.source.OmsApi;
import com.tokopedia.oms.data.source.OmsBaseURL;

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
    EventInerceptors provideRideInterCeptor(@ApplicationContext Context context) {
        String oAuthString = "Bearer " + SessionHandler.getAccessToken();
        return new EventInerceptors(oAuthString, context);
    }

    @OmsQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor,@OmsQualifier EventInerceptors authInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

}