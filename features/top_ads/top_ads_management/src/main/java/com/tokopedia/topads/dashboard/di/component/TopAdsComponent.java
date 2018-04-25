package com.tokopedia.topads.dashboard.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;
import com.tokopedia.topads.common.data.api.TopAdsManagementApi;
import com.tokopedia.topads.dashboard.di.module.TopAdsModule;
import com.tokopedia.topads.dashboard.di.qualifier.TopAdsManagementQualifier;
import com.tokopedia.topads.dashboard.di.scope.TopAdsScope;
import com.tokopedia.topads.dashboard.domain.interactor.GetDepositTopAdsUseCase;

import dagger.Component;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

@TopAdsScope
@Component(modules = {TopAdsModule.class}, dependencies = AppComponent.class)
public interface TopAdsComponent {

    @ApplicationContext
    Context context();

    @TopAdsQualifier
    Retrofit topAdsRetrofit();

    @DefaultAuthWithErrorHandler
    OkHttpClient okHttpClient();

    Retrofit.Builder retrofitBuilder();

    Gson gson();

    @WsV4Qualifier
    Retrofit baseDomainRetrofit();

    @WsV4QualifierWithErrorHander
    Retrofit baseDomainWithErrorHandlerRetrofit();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    SessionHandler sessionHandler();

    GCMHandler gcmHandler();

    ImageHandler imageHandler();

    GetDepositTopAdsUseCase getDepositTopAdsUseCase();

    ShopInfoRepository shopInfoRepository();

    @AceQualifier
    Retrofit aceRetrofit();

    @MojitoQualifier
    Retrofit mojitoRetrofit();

    UserSession userSession();

    TopAdsManagementApi topAdsManagementApi();

    AbstractionRouter abstractionRouter();

    HttpLoggingInterceptor httpLoggingInterceptor();

    CacheApiInterceptor cacheApiInterceptor();

    @TopAdsManagementQualifier
    Retrofit topAdsManagementRetrofit();
}
