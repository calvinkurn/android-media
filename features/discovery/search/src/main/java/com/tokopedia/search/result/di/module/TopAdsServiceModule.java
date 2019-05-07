package com.tokopedia.search.result.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.utils.TkpdOkHttpBuilder;
import com.tokopedia.search.di.qualifier.TopAdsQualifier;
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

@ApplicationScope
@Module
public class TopAdsServiceModule {

    @TopAdsQualifier
    @ApplicationScope
    @Provides
    public FingerprintInterceptor provideFingerprintInterceptor(@ApplicationContext Context context, UserSessionInterface userSession) {
        return new FingerprintInterceptor((NetworkRouter) context, userSession);
    }

    @TopAdsQualifier
    @ApplicationScope
    @Provides
    public TopAdsAuthInterceptor provideTopAdsAuthInterceptor(@ApplicationContext Context context, UserSessionInterface userSessionInterface) {
        return new TopAdsAuthInterceptor(context, (NetworkRouter)context, userSessionInterface);
    }

    @TopAdsQualifier
    @ApplicationScope
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

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingLevel = HttpLoggingInterceptor.Level.BODY;
        }
        return new HttpLoggingInterceptor().setLevel(loggingLevel);
    }

    @TopAdsQualifier
    @ApplicationScope
    @Provides
    public Retrofit provideTopAdsRetrofit(@TopAdsQualifier OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(SearchConstant.BaseUrl.TOPADS_DOMAIN).client(okHttpClient).build();
    }

    @TopAdsQualifier
    @SearchScope
    @Provides
    public TopAdsService proviceTopAdsService(@TopAdsQualifier Retrofit topAdsRetrofit) {
        return topAdsRetrofit.create(TopAdsService.class);
    }
}
