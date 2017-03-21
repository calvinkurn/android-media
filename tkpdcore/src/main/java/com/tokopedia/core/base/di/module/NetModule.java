package com.tokopedia.core.base.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.base.di.qualifier.AceQualifier;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.qualifier.BaseDomainQualifier;
import com.tokopedia.core.base.di.qualifier.MojitoQualifier;
import com.tokopedia.core.base.di.qualifier.NoAuthInterceptor;
import com.tokopedia.core.base.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.base.di.qualifier.WithAuthInterceptor;
import com.tokopedia.core.base.di.qualifier.WithGlobalAuthInterceptor;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.GlobalTkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author kulomady on 1/9/17.
 */
@Module
public class NetModule {

    @AceQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideAceRetrofit(@NoAuthInterceptor OkHttpClient okHttpClient) {

        return RetrofitFactory.createRetrofitDefaultConfig(TkpdBaseURL.ACE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @TopAdsQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideTopAdsRetrofit(@NoAuthInterceptor OkHttpClient okHttpClient) {

        return RetrofitFactory.createRetrofitDefaultConfig(TkpdBaseURL.TOPADS_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @BaseDomainQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideBaseDomainRetrofit(@WithAuthInterceptor OkHttpClient okHttpClient) {

        return RetrofitFactory.createRetrofitDefaultConfig(TkpdBaseURL.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @MojitoQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideMojitoRetrofit(@WithGlobalAuthInterceptor OkHttpClient okHttpClient) {

        return RetrofitFactory.createRetrofitDefaultConfig(TkpdBaseURL.MOJITO_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @ApplicationScope
    @Provides
    public Gson provideGsonFactory() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }

    @WithAuthInterceptor
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkhttpClient(Cache cache,
                                            OkHttpRetryPolicy okHttpRetryPolicy) {

        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(okHttpRetryPolicy)
                .buildClientDefaultAuth();
    }

    @WithGlobalAuthInterceptor
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkhttpClientGlobalAuth(Cache cache,
                                                      OkHttpRetryPolicy okHttpRetryPolicy) {

        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(okHttpRetryPolicy)
                .buildClientAuth(AuthUtil.KEY.KEY_MOJITO);
    }

    @NoAuthInterceptor
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkhttpClientNoAuth(Cache cache,
                                                  OkHttpRetryPolicy okHttpRetryPolicy) {

        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(okHttpRetryPolicy)
                .buildClientNoAuth();
    }

    @ApplicationScope
    @Provides
    public Cache provideHttpCache(@ApplicationContext Context context) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(context.getCacheDir(), cacheSize);
    }

    @ApplicationScope
    @Provides
    Interceptor provideAuthInterceptor() {
        return new TkpdAuthInterceptor();
    }

    @ApplicationScope
    @Provides
    GeneratedHostConverter provideHostConverted() {
        return new GeneratedHostConverter();
    }

    @ApplicationScope
    @Provides
    TkpdResponseConverter provideResponseConverted() {
        return new TkpdResponseConverter();
    }

    @ApplicationScope
    @Provides
    StringResponseConverter provideStringConverter() {
        return new StringResponseConverter();
    }

    @ApplicationScope
    @Provides
    public HttpLoggingInterceptor provideHttpLogingInterceptor() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @ApplicationScope
    @Provides
    public GlobalTkpdAuthInterceptor provideGlobalAuthInterceptor() {
        return new GlobalTkpdAuthInterceptor(AuthUtil.KEY.KEY_MOJITO);
    }

    @ApplicationScope
    @Provides
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }


}
