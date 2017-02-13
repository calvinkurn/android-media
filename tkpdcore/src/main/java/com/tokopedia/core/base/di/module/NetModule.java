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
    public Retrofit provideAceRetrofit(Gson gson,
                                       @WithAuthInterceptor OkHttpClient okHttpClient,
                                       GeneratedHostConverter generatedHostConverter,
                                       TkpdResponseConverter tkpdResponseConverter,
                                       StringResponseConverter stringResponseConverter) {
        return new Retrofit.Builder()
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(generatedHostConverter)
                .addConverterFactory(tkpdResponseConverter)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(TkpdBaseURL.ACE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @TopAdsQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideTopAdsRetrofit(Gson gson,
                                          @NoAuthInterceptor OkHttpClient okHttpClient,
                                          GeneratedHostConverter generatedHostConverter,
                                          TkpdResponseConverter tkpdResponseConverter,
                                          StringResponseConverter stringResponseConverter) {
        return new Retrofit.Builder()
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(generatedHostConverter)
                .addConverterFactory(tkpdResponseConverter)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(TkpdBaseURL.TOPADS_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @BaseDomainQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideBaseDomainRetrofit(Gson gson,
                                              @WithAuthInterceptor OkHttpClient okHttpClient,
                                              GeneratedHostConverter generatedHostConverter,
                                              TkpdResponseConverter tkpdResponseConverter,
                                              StringResponseConverter stringResponseConverter) {
        return new Retrofit.Builder()
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(generatedHostConverter)
                .addConverterFactory(tkpdResponseConverter)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(TkpdBaseURL.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @MojitoQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideMojitoRetrofit(Gson gson,
                                          @WithGlobalAuthInterceptor OkHttpClient okHttpClient,
                                          GeneratedHostConverter generatedHostConverter,
                                          TkpdResponseConverter tkpdResponseConverter,
                                          StringResponseConverter stringResponseConverter) {
        return new Retrofit.Builder()
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(generatedHostConverter)
                .addConverterFactory(tkpdResponseConverter)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(TkpdBaseURL.MOJITO_DOMAIN)
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
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            Interceptor authInterceptor) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        client.connectTimeout(45, TimeUnit.SECONDS);
        client.readTimeout(45, TimeUnit.SECONDS);
        client.writeTimeout(45, TimeUnit.SECONDS);
        client.addInterceptor(authInterceptor);
        client.addInterceptor(httpLoggingInterceptor);
        client.cache(cache);
        return client.build();
    }

    @WithGlobalAuthInterceptor
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkhttpClientGlobalAuth(Cache cache,
                                                      HttpLoggingInterceptor httpLoggingInterceptor,
                                                      GlobalTkpdAuthInterceptor globalTkpdAuthInterceptor) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        client.connectTimeout(45, TimeUnit.SECONDS);
        client.readTimeout(45, TimeUnit.SECONDS);
        client.writeTimeout(45, TimeUnit.SECONDS);
        client.addInterceptor(globalTkpdAuthInterceptor);
        client.addInterceptor(httpLoggingInterceptor);
        client.cache(cache);
        return client.build();
    }

    @NoAuthInterceptor
    @ApplicationScope
    @Provides
    public OkHttpClient provideOkhttpClientNoAuth(Cache cache,
                                                  HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        client.connectTimeout(45, TimeUnit.SECONDS);
        client.readTimeout(45, TimeUnit.SECONDS);
        client.writeTimeout(45, TimeUnit.SECONDS);
        client.addInterceptor(httpLoggingInterceptor);
        client.cache(cache);
        return client.build();
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


}
