package com.tokopedia.tkpdcontent.common.di;

import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.tkpdcontent.common.data.source.api.KolApi;
import com.tokopedia.tkpdcontent.common.network.KolUrl;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by milhamj on 06/02/18.
 */

@Module
public class KolModule {
    @KolScope
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                                        httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .build();
    }

    @KolScope
    @Provides
    @KolQualifier
    public Retrofit provideFlightRetrofit(OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(KolUrl.BASE_URL).client(okHttpClient).build();
    }

    @KolScope
    @Provides
    public KolApi provideFlightAirportApi(@KolQualifier Retrofit retrofit) {
        return retrofit.create(KolApi.class);
    }
}
