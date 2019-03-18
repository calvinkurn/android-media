package com.tokopedia.kol.common.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.kol.KolRouter;
import com.tokopedia.kol.common.data.source.KolAuthInterceptor;
import com.tokopedia.kol.common.data.source.api.KolApi;
import com.tokopedia.kol.common.network.KolUrl;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by milhamj on 06/02/18.
 */

@Module
public class KolModule {
    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @KolScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @KolScope
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                                    httpLoggingInterceptor,
                                            KolAuthInterceptor kolAuthInterceptor,
                                            @KolQualifier OkHttpRetryPolicy retryPolicy,
                                            @KolChuckQualifier Interceptor chuckInterceptor,
                                            @ApplicationContext Context context) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(kolAuthInterceptor)
                .addInterceptor(new FingerprintInterceptor((NetworkRouter) context,
                        new UserSession(context)));

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor);
            clientBuilder.addInterceptor(chuckInterceptor);
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

    @KolScope
    @Provides
    @KolChuckQualifier
    public Interceptor provideChuckInterceptory(@ApplicationContext Context context) {
        if (context instanceof KolRouter) {
            return ((KolRouter) context).getChuckInterceptor();
        }
        throw new RuntimeException("App should implement " + KolRouter.class.getSimpleName());
    }

}
