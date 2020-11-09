package com.tokopedia.core.base.di.component;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.Gson;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.di.module.UtilModule;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.CartQualifier;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.MerlinQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.di.qualifier.YoutubeQualifier;
import com.tokopedia.core.network.retrofit.interceptors.BearerInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author kulomady on 1/9/17.
 */
@ApplicationScope
@Component(modules = {
        AppModule.class,
        UtilModule.class
})
public interface AppComponent {

    void inject(MainApplication mainApplication);

    @ApplicationContext
    Context context();

    @AceQualifier
    Retrofit aceRetrofit();

    @MerlinQualifier
    Retrofit merlinRetrofit();

    @TomeQualifier
    Retrofit tomeRetrofit();

    @MojitoQualifier
    Retrofit mojitoRetrofit();

    @HadesQualifier
    Retrofit hadesRetrofit();

    @YoutubeQualifier
    Retrofit youtubeRetrofit();

    @DefaultAuthWithErrorHandler
    OkHttpClient okHttpClient();

    @ResolutionQualifier
    Retrofit resolutionRetrofit();

    @GoldMerchantQualifier
    Retrofit goldMerchantRetrofit();

    @CartQualifier
    Retrofit cartRetrofit();

    Retrofit.Builder retrofitBuilder();

    Gson gson();

    @WsV4Qualifier
    Retrofit baseDomainRetrofit();

    @WsV4QualifierWithErrorHander
    Retrofit baseDomainWithErrorHandlerRetrofit();

    ChuckerInterceptor ChuckerInterceptor();

    GCMHandler gcmHandler();

    BearerInterceptor bearerInterceptor();

    FingerprintInterceptor fingerprintInterceptor();

}
