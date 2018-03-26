package com.tokopedia.posapp.common;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;

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
    public OkHttpClient provideOkHttpClient(@ApplicationContext Context context,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            PosAuthInterceptor posAuthInterceptor) {
        OkHttpRetryPolicy okHttpRetryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(posAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(new ChuckInterceptor(context)).addInterceptor(new DebugInterceptor());
        }
        return builder.build();
    }

    @Provides
    public Retrofit providePosAuthRetrofit(OkHttpClient okHttpClient,
                                           Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder
                .baseUrl(PosUrl.POS_DOMAIN)
                .client(okHttpClient)
                .build();
    }
}
