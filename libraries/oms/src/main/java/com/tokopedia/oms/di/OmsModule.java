package com.tokopedia.oms.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.interceptors.EventInerceptors;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.oms.data.source.OmsApi;

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
        return retrofitBuilder.baseUrl(TkpdBaseURL.OMS_DOMAIN).client(okHttpClient).build();
    }

    @OmsQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor, OmsInterceptor omsInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(omsInterceptor)
                .build();
    }

}