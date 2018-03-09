package com.tokopedia.tkpdcontent.common.di;

import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.tkpdcontent.common.data.source.KolAuthInterceptor;
import com.tokopedia.tkpdcontent.common.data.source.api.KolApi;
import com.tokopedia.tkpdcontent.common.network.KolUrl;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by milhamj on 06/02/18.
 */

@KolScope
@Module
public class KolModule {
    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @KolScope
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                                        httpLoggingInterceptor,
                                            KolAuthInterceptor kolAuthInterceptor,
                                            OkHttpRetryPolicy retryPolicy) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(kolAuthInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor);
        }

        return clientBuilder.build();
    }

    @KolScope
    @Provides
    @KolQualifier
    public Retrofit provideKolRetrofit(OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(KolUrl.BASE_URL).client(okHttpClient).build();
    }

    @KolScope
    @Provides
    public KolApi provideKolApi(@KolQualifier Retrofit retrofit) {
        return retrofit.create(KolApi.class);
    }

    @KolScope
    @KolQualifier
    @Provides
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }
}
