package com.tokopedia.shop.open.di.component;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.open.analytic.ShopOpenTracking;
import com.tokopedia.shop.open.data.repository.ShopOpenRepository;
import com.tokopedia.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.shop.open.di.scope.ShopOpenDomainScope;
import com.tokopedia.shop.open.view.fragment.ShopOpenCreateReadyFragment;
import com.tokopedia.shop.open.view.fragment.ShopOpenCreateSuccessFragment;
import com.tokopedia.shop.open.view.fragment.ShopOpenReserveDomainFragment;
import com.tokopedia.shop.open.view.fragment.ShopOpenMandatoryLogisticFragment;
import com.tokopedia.shop.open.view.fragment.ShopOpenRoutingFragment;
import com.tokopedia.shop.open.di.module.ShopSettingInfoModule;
import com.tokopedia.shop.open.view.presenter.ShopOpenInfoPresenter;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by sebastianuskh on 3/17/17.
 */
@ShopOpenDomainScope
@Component(modules = {ShopOpenDomainModule.class, ShopSettingInfoModule.class}, dependencies = ShopComponent.class)
public interface ShopOpenDomainComponent {

	void inject(ShopOpenRoutingFragment shopOpenRoutingFragment);

    void inject(ShopOpenReserveDomainFragment shopOpenDomainFragment);

    void inject(ShopOpenMandatoryLogisticFragment shopOpenMandatoryLogisticFragment);

    void inject(ShopOpenCreateSuccessFragment shopOpenCreateSuccessFragment);

    void inject(ShopOpenCreateReadyFragment shopOpenCreateReadyFragment);

    ThreadExecutor getThreadExecutor();

    PostExecutionThread getPostExecutionThread();

    @ShopQualifier
    TomeApi getTomeApi();

    Retrofit.Builder retrofitBuilder();

    @DefaultAuthWithErrorHandler
    OkHttpClient okHttpClient();

    @ApplicationContext
    Context context();

    @WsV4QualifierWithErrorHander
    Retrofit retrofitWsV4();

    ShopOpenRepository provideShopOpenRepository();

    ShopOpenInfoPresenter shopSettingInfoPresenter();

    ShopOpenTracking trackingOpenShop();

    SessionHandler sessionHandler();

    GlobalCacheManager globalCacheManager();
}
