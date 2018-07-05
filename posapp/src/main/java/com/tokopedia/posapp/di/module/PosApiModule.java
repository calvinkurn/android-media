package com.tokopedia.posapp.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.common.PosAuthInterceptor;
import com.tokopedia.posapp.common.PosUrl;
import com.tokopedia.posapp.di.scope.PosApplicationScope;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author okasurya on 3/26/18.
 */

@Module
public class PosApiModule {
    @Provides
    @PosApplicationScope
    public FingerprintInterceptor provideFingerprintInterceptor() {
        return new FingerprintInterceptor();
    }

    @Provides
    @PosApplicationScope
    public OkHttpClient provideOkHttpClient(@ApplicationContext Context context,
                                            Gson gson,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            PosAuthInterceptor posAuthInterceptor,
                                            FingerprintInterceptor fingerprintInterceptor) {
        OkHttpRetryPolicy okHttpRetryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

//        CacheApiInterceptor cacheApiInterceptor = new CacheApiInterceptor();
//        cacheApiInterceptor.setResponseValidator(new PosCacheApiResponseValidator(gson));

        builder.readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(posAuthInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(httpLoggingInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(new ChuckInterceptor(context)).addInterceptor(new DebugInterceptor());
        }
        return builder.build();
    }

    @Provides
    @PosApplicationScope
    public Retrofit providePosAuthRetrofit(OkHttpClient okHttpClient,
                                           Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder
                .baseUrl(PosUrl.POS_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @PosApplicationScope
    PosSessionHandler providePosSessionHandler(@ApplicationContext Context context) {
        return new PosSessionHandler(context);
    }
}
