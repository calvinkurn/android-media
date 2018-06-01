package com.tokopedia.instantloan.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.instantloan.data.soruce.cloud.api.InstantLoanApi;
import com.tokopedia.instantloan.di.ChuckQualifier;
import com.tokopedia.instantloan.di.InstantLoanQualifier;
import com.tokopedia.instantloan.di.scope.InstantLoanScope;
import com.tokopedia.instantloan.network.InstantLoanAuthInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tokopedia.instantloan.network.InstantLoanUrl.BaseUrl.WEB_DOMAIN;

/**
 * Created by lavekush on 19/03/18.
 */
@InstantLoanScope
@Module
public class InstantLoanModule {

    @InstantLoanScope
    @Provides
    InstantLoanApi provideInstantLoanApi(@InstantLoanQualifier Retrofit retrofit) {
        return retrofit.create(InstantLoanApi.class);
    }

    @InstantLoanQualifier
    @InstantLoanScope
    @Provides
    public Retrofit provideRetrofit(@InstantLoanQualifier OkHttpClient okHttpClient) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        return retrofitBuilder.baseUrl(WEB_DOMAIN)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();
    }

    //TODO add api cache interceptor (not from tkpdcore) to cache the response
    @InstantLoanQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            HeaderErrorResponseInterceptor errorResponseInterceptor,
                                            @ChuckQualifier Interceptor chuckInterceptor,
                                            InstantLoanAuthInterceptor authInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chuckInterceptor)
                .build();
    }

    @Provides
    public InstantLoanRouter provideInstantLoanRouter(@ApplicationContext Context context) {
        if (context instanceof InstantLoanRouter) {
            return ((InstantLoanRouter) context);
        }
        throw new RuntimeException("App should implement " + InstantLoanRouter.class.getSimpleName());
    }

    @Provides
    @ChuckQualifier
    public Interceptor provideChuckInterceptor(InstantLoanRouter router) {
        return router.getChuckInterceptor();
    }
}

