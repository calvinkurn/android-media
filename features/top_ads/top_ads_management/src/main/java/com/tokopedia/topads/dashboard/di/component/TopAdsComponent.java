package com.tokopedia.topads.dashboard.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.product.common.domain.repository.ShopInfoRepository;
import com.tokopedia.topads.common.data.api.TopAdsManagementApi;
import com.tokopedia.topads.dashboard.di.module.TopAdsModule;
import com.tokopedia.topads.dashboard.di.module.TopAdsShopModule;
import com.tokopedia.topads.dashboard.di.qualifier.ShopWsQualifier;
import com.tokopedia.topads.dashboard.di.qualifier.TopAdsManagementQualifier;
import com.tokopedia.topads.dashboard.di.scope.TopAdsScope;
import com.tokopedia.topads.dashboard.domain.interactor.GetDepositTopAdsUseCase;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

@TopAdsScope
@Component(modules = {TopAdsModule.class, TopAdsShopModule.class}, dependencies = BaseAppComponent.class)
public interface TopAdsComponent {

    @ApplicationContext
    Context context();

    Retrofit.Builder retrofitBuilder();

    Gson gson();

    GetDepositTopAdsUseCase getDepositTopAdsUseCase();

    TopAdsManagementApi topAdsManagementApi();

    CacheApiInterceptor cacheApiInterceptor();

    @TopAdsManagementQualifier
    Retrofit topAdsManagementRetrofit();

    @ShopWsQualifier
    Retrofit shopWsRetrofit();

    @ShopQualifier
    Retrofit shopRetrofit();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    AbstractionRouter abstractionRouter();

    UserSession userSession();

    HttpLoggingInterceptor httpLoggingInterceptor();

    ShopInfoRepository shopInfoRepository();
}
