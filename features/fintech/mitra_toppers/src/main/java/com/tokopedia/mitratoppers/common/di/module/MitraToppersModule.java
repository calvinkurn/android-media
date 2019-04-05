package com.tokopedia.mitratoppers.common.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.mitratoppers.common.constant.MitraToppersBaseURL;
import com.tokopedia.mitratoppers.common.data.interceptor.MitraToppersAuthInterceptor;
import com.tokopedia.mitratoppers.common.di.MitraToppersQualifier;
import com.tokopedia.mitratoppers.common.di.scope.MitraToppersScope;
import com.tokopedia.mitratoppers.preapprove.data.source.cloud.api.MitraToppersApi;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@MitraToppersScope
@Module
public class MitraToppersModule {

    @MitraToppersScope
    @Provides
    MitraToppersApi provideMitraToppersApi(@MitraToppersQualifier Retrofit retrofit){
        return retrofit.create(MitraToppersApi.class);
    }

    @MitraToppersQualifier
    @MitraToppersScope
    @Provides
    public Retrofit provideRetrofit(@MitraToppersQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.baseUrl(MitraToppersBaseURL.WEB_DOMAIN + MitraToppersBaseURL.PATH_MITRA_TOPPERS)
                .client(okHttpClient).build();
    }

    //TODO add api cache interceptor (not from tkpdcore) to cache the response
    @MitraToppersQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(MitraToppersAuthInterceptor mitraToppersAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(mitraToppersAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}

