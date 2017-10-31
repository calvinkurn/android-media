package com.tokopedia.core.base.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.di.module.UtilModule;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.source.ApiCacheDataSource;
import com.tokopedia.core.cache.di.module.CacheModule;
import com.tokopedia.core.cache.di.qualifier.ApiCacheQualifier;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.core.cache.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.di.qualifier.AccountsQualifier;
import com.tokopedia.core.network.di.qualifier.AceAuth;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.CartQualifier;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.MerlinQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.PaymentNoAuth;
import com.tokopedia.core.network.di.qualifier.PosGatewayAuth;
import com.tokopedia.core.network.di.qualifier.PosGatewayNoAuth;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.core.network.di.qualifier.ScroogeCreditCardRetrofit;
import com.tokopedia.core.network.di.qualifier.ScroogeNoAuth;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.di.qualifier.UploadWsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.di.qualifier.YoutubeQualifier;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.util.SessionHandler;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author kulomady on 1/9/17.
 */
@ApplicationScope
@Component(modules = {
        AppModule.class,
        UtilModule.class,
        CacheModule.class
})
public interface AppComponent {

    void inject(MainApplication mainApplication);

    void inject(BaseActivity baseActivity);

    void inject(TActivity baseActivity);

    @ApplicationContext
    Context context();

    @TopAdsQualifier
    Retrofit topAdsRetrofit();

    @AceQualifier
    Retrofit aceRetrofit();

    @AceAuth
    Retrofit aceAuthRetrofit();

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

    @AccountsQualifier
    Retrofit accountRetrofit();

    Retrofit.Builder retrofitBuilder();

    Gson gson();

    @WsV4Qualifier
    Retrofit baseDomainRetrofit();

    @UploadWsV4Qualifier
    Retrofit uploadWsV4Retrofit();

    @WsV4QualifierWithErrorHander
    Retrofit baseDomainWithErrorHandlerRetrofit();

    @ScroogeCreditCardRetrofit
    Retrofit scroogeCreditCardRetrofit();

    @ScroogeNoAuth
    Retrofit scroogeNoAuth();

    @PaymentNoAuth
    Retrofit paymentNoAuth();

    @PosGatewayNoAuth
    Retrofit posNoAuth();

    @PosGatewayAuth
    Retrofit posAuth();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    OkHttpRetryPolicy okHttpRetryPolicy();

    ChuckInterceptor chuckInterceptor();

    DebugInterceptor debugInterceptor();

    SessionHandler sessionHandler();

    GCMHandler gcmHandler();

    ImageHandler imageHandler();

    @ApiCacheQualifier
    LocalCacheHandler localCacheHandler();

    ApiCacheRepository apiCacheRepository();

    CacheApiWhiteListUseCase cacheApiWhiteListUseCase();

    ApiCacheDataSource cacheHelper();

    CacheApiClearAllUseCase cacheApiClearAllUseCase();
}
