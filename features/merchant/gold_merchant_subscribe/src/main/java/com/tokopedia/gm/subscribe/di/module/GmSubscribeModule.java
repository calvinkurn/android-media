package com.tokopedia.gm.subscribe.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.di.qualifier.CartQualifier;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.gm.subscribe.data.factory.GmSubscribeCartFactory;
import com.tokopedia.gm.subscribe.data.factory.GmSubscribeProductFactory;
import com.tokopedia.gm.subscribe.data.repository.GmSubscribeCartRepositoryImpl;
import com.tokopedia.gm.subscribe.data.repository.GmSubscribeProductRepositoryImpl;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.api.GmSubscribeCartApi;
import com.tokopedia.gm.subscribe.data.source.product.cloud.api.GoldMerchantApi;
import com.tokopedia.gm.subscribe.di.scope.GmSubscribeScope;
import com.tokopedia.gm.subscribe.domain.cart.GmSubscribeCartRepository;
import com.tokopedia.gm.subscribe.domain.product.GmSubscribeProductRepository;
import com.tokopedia.product.manage.item.common.data.source.ShopInfoDataSource;
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 5/2/17.
 */
@GmSubscribeScope
@Module
public class GmSubscribeModule {

    @GmSubscribeScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @GmSubscribeScope
    @Provides
    GmSubscribeProductRepository provideGmSubscribeProductRepository(GmSubscribeProductFactory gmSubscribeProductFactory){
        return new GmSubscribeProductRepositoryImpl(gmSubscribeProductFactory);
    }

    @GmSubscribeScope
    @Provides
    GmSubscribeCartRepository provideGmSubscribeCartRepository(GmSubscribeCartFactory gmSubscribeCartFactory){
        return new GmSubscribeCartRepositoryImpl(gmSubscribeCartFactory);
    }

    @GmSubscribeScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager(){
        return new GlobalCacheManager();
    }

    @GmSubscribeScope
    @Provides
    GmSubscribeCartApi provideGmSubscribeCartApi(@CartQualifier Retrofit retrofit){
        return retrofit.create(GmSubscribeCartApi.class);
    }

    @GmSubscribeScope
    @Provides
    GoldMerchantApi provideGoldMerchantApi(@GoldMerchantQualifier Retrofit retrofit){
        return retrofit.create(GoldMerchantApi.class);
    }

    @GmSubscribeScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @GmSubscribeScope
    @Provides
    DeleteShopInfoCacheUseCase provideDeleteShopInfoUseCase() {
        return new DeleteShopInfoCacheUseCase();
    }

}
