package com.tokopedia.shop.open.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.logistic.GetOpenShopLocationPassUseCase;
import com.tokopedia.seller.logistic.GetOpenShopTokenUseCase;
import com.tokopedia.seller.logistic.data.source.cloud.api.WSLogisticApi;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.seller.shop.common.di.ShopScope;
import com.tokopedia.shop.open.analytic.ShopOpenTracking;
import com.tokopedia.shop.open.data.repository.ShopOpenRepository;
import com.tokopedia.shop.open.data.repository.ShopOpenRepositoryImpl;
import com.tokopedia.shop.open.data.source.ShopOpenDataSource;
import com.tokopedia.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.shop.open.di.scope.ShopOpenDomainScope;
import com.tokopedia.shop.open.data.source.cloud.api.OpenShopApi;
import com.tokopedia.seller.logistic.data.repository.DistrictLogisticDataRepositoryImpl;
import com.tokopedia.seller.logistic.data.source.LogisticDataSource;
import com.tokopedia.seller.logistic.domain.DistrictLogisticDataRepository;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@ShopOpenDomainScope
@Module
public class ShopOpenDomainModule {

    @ShopOpenDomainScope
    @Provides
    public ShopOpenRepository provideShopOpenRepository(ShopOpenDataSource shopOpenDataSource) {
        return new ShopOpenRepositoryImpl(shopOpenDataSource);
    }

    @ShopOpenDomainScope
    @Provides
    public DistrictLogisticDataRepository provideDistrictLogisticDataRepository(LogisticDataSource logisticDataSource) {
        return new DistrictLogisticDataRepositoryImpl(logisticDataSource);
    }

    @Provides
    @ShopOpenDomainScope
    public OpenShopApi provideOpenShopApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(OpenShopApi.class);
    }

    @Provides
    @ShopOpenDomainScope
    public WSLogisticApi provideWSLogisticApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(WSLogisticApi.class);
    }

    @Provides
    @ShopOpenDomainScope
    public GetOpenShopTokenUseCase provideGetOpenShopDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread){
        return new GetOpenShopTokenUseCase(threadExecutor,postExecutionThread);
    }


    @Provides
    @ShopOpenDomainScope
    public GetOpenShopLocationPassUseCase provideGetOpenShopLocationPassUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread){
        return new GetOpenShopLocationPassUseCase(threadExecutor,postExecutionThread);
    }

    @ShopQualifier
    @ShopOpenDomainScope
    @Provides
    public TomeApi provideTomeApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(TomeApi.class);
    }

    @ShopOpenDomainScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ShopOpenDomainScope
    @Provides
    public ShopOpenTracking provideTrackingOpenShop(@ApplicationContext Context context,
                                                    UserSessionInterface userSessionInterface){
        if(context instanceof SellerModuleRouter) {
            return new ShopOpenTracking((SellerModuleRouter)context, userSessionInterface);
        }else{
            return null;
        }
    }
}