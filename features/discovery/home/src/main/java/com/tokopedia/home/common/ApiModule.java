package com.tokopedia.home.common;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.home.constant.BerandaUrl;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.DebugInterceptor;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module
public class ApiModule {

    @Provides
    HomeDataApi homeDataApi(@HomeGraphQLQualifier Retrofit retrofit){
        return retrofit.create(HomeDataApi.class);
    }

    @Provides
    HomeAceApi homeAceApi(@HomeAceQualifier Retrofit retrofit){
        return retrofit.create(HomeAceApi.class);
    }

    @Provides
    @HomeGraphQLQualifier
    public OkHttpClient provideOkHttpClient() {
        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy())
                .buildClientDefaultAuth();
    }

    @Provides
    public CacheApiInterceptor provideCacheApiInterceptor(@ApplicationContext Context context) {
        return new CacheApiInterceptor(context);
    }

    @Provides
    public FingerprintInterceptor provideFingerprintInterceptor(@ApplicationContext Context context, UserSessionInterface userSession) {
        return new FingerprintInterceptor((NetworkRouter) context, userSession);
    }

    @Provides
    TkpdBaseInterceptor provideTkpdBaseInterceptor() {
        return new TkpdBaseInterceptor();
    }

    @Provides
    DebugInterceptor provideDebugInterceptor() {
        return new DebugInterceptor();
    }

    @Provides
    @HomeAceQualifier
    OkHttpClient provideOkHttpClientAceQualifier(@ApplicationContext Context context,
                                                 FingerprintInterceptor fingerprintInterceptor,
                                                 CacheApiInterceptor cacheApiInterceptor,
                                                 TkpdBaseInterceptor tkpdBaseInterceptor,
                                                 DebugInterceptor debugInterceptor,
                                                 HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient client = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder())
                .addInterceptor(httpLoggingInterceptor)
                .build();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(context, client.newBuilder())
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdBaseInterceptor)
                .addInterceptor(cacheApiInterceptor);

        if(GlobalConfig.isAllowDebuggingTools()) {
            tkpdOkHttpBuilder.addInterceptor(debugInterceptor);
        }

        return tkpdOkHttpBuilder.build();
    }

    @HomeGraphQLQualifier
    @Provides
    public Retrofit provideHomeGraphQlRetrofit(@HomeGraphQLQualifier OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return new Retrofit.Builder()
                .baseUrl(BerandaUrl.GRAPHQL_URL)
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @HomeAceQualifier
    @Provides
    public Retrofit provideAceRetrofit(@HomeAceQualifier OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return new Retrofit.Builder()
                .baseUrl(BerandaUrl.ACE_URL)
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
