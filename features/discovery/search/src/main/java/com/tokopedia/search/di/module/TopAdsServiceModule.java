package com.tokopedia.search.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.search.di.qualifier.TopAdsQualifier;
import com.tokopedia.search.result.network.converterfactory.GeneratedHostConverter;
import com.tokopedia.search.result.network.converterfactory.StringResponseConverter;
import com.tokopedia.search.result.network.interceptor.TopAdsAuthInterceptor;
import com.tokopedia.search.result.network.response.TopAdsResponseError;
import com.tokopedia.search.result.network.service.TopAdsService;
import com.tokopedia.search.result.network.validator.CacheApiTKPDResponseValidator;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@SearchScope
@Module
public class TopAdsServiceModule {

    @TopAdsQualifier
    @SearchScope
    @Provides
    public TopAdsService proviceTopAdsService(@TopAdsQualifier Retrofit topAdsRetrofit) {
        return topAdsRetrofit.create(TopAdsService.class);
    }

    @TopAdsQualifier
    @SearchScope
    @Provides
    public Retrofit provideTopAdsRetrofit(@TopAdsQualifier OkHttpClient okHttpClient,
                                          @TopAdsQualifier Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(SearchConstant.BaseUrl.TOPADS_DOMAIN).client(okHttpClient).build();
    }

    @TopAdsQualifier
    @SearchScope
    @Provides
    public OkHttpClient provideOkHttpClientTopAdsAuth(@ApplicationContext Context context,
                                                      @TopAdsQualifier FingerprintInterceptor fingerprintInterceptor,
                                                      @TopAdsQualifier TopAdsAuthInterceptor topAdsAuthInterceptor,
                                                      @TopAdsQualifier ErrorResponseInterceptor errorResponseInterceptor,
                                                      @TopAdsQualifier CacheApiInterceptor cacheApiInterceptor) {

        cacheApiInterceptor.setResponseValidator(new CacheApiTKPDResponseValidator<>(TopAdsResponseError.class));

        OkHttpClient client = new TkpdOkHttpBuilder(context, new OkHttpClient.Builder())
                .addInterceptor(getHttpLoggingInterceptor())
                .build();

        return new TkpdOkHttpBuilder(context, client.newBuilder())
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .setOkHttpRetryPolicy()
                .build();
    }

    @TopAdsQualifier
    @SearchScope
    @Provides
    public FingerprintInterceptor provideFingerprintInterceptor(@ApplicationContext Context context, UserSessionInterface userSession) {
        return new FingerprintInterceptor((NetworkRouter) context, userSession);
    }

    @TopAdsQualifier
    @SearchScope
    @Provides
    public TopAdsAuthInterceptor provideTopAdsAuthInterceptor(@ApplicationContext Context context, UserSessionInterface userSessionInterface) {
        return new TopAdsAuthInterceptor(context, (NetworkRouter)context, userSessionInterface);
    }

    @TopAdsQualifier
    @SearchScope
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TopAdsResponseError.class);
    }

    @TopAdsQualifier
    @SearchScope
    @Provides
    public CacheApiInterceptor provideCacheApiInterceptor(@ApplicationContext Context context) {
        return new CacheApiInterceptor(context);
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingLevel = HttpLoggingInterceptor.Level.BODY;
        }
        return new HttpLoggingInterceptor().setLevel(loggingLevel);
    }

    @TopAdsQualifier
    @SearchScope
    @Provides
    public Retrofit.Builder provideTopAdsRetrofitBuilder() {
        Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create();

        return new Retrofit.Builder()
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }
}
