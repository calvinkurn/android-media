package com.tokopedia.core.base.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.cacheapi.data.source.CacheApiDataSource;
import com.tokopedia.cacheapi.data.source.db.CacheApiDatabaseSource;
import com.tokopedia.cacheapi.domain.CacheApiRepository;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.cacheapi.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.di.module.UtilModule;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.CartQualifier;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.MerlinQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoGetWishlistQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoWishlistActionQualifier;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.di.qualifier.UploadWsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.di.qualifier.YoutubeQualifier;
import com.tokopedia.core.network.retrofit.interceptors.BearerInterceptor;
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
        UtilModule.class
})
public interface AppComponent {

    void inject(MainApplication mainApplication);

    void inject(BaseActivity baseActivity);

    void inject(TActivity baseActivity);

    @TopAdsQualifier
    Retrofit topAdsRetrofit();

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

    @MojitoGetWishlistQualifier
    Retrofit mojitoGetWishlistRetrofit();

    @MojitoWishlistActionQualifier
    Retrofit mojitoWishlistActionRetrofit();

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

    @UploadWsV4Qualifier
    Retrofit uploadWsV4Retrofit();

    @WsV4QualifierWithErrorHander
    Retrofit baseDomainWithErrorHandlerRetrofit();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    OkHttpRetryPolicy okHttpRetryPolicy();

    ChuckInterceptor chuckInterceptor();

    DebugInterceptor debugInterceptor();

    SessionHandler sessionHandler();

    GCMHandler gcmHandler();

    ImageHandler imageHandler();

    BearerInterceptor bearerInterceptor();

}
