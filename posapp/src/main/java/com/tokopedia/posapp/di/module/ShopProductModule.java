package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.AceAuth;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.data.factory.ShopFactory;
import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.mapper.GetShopProductMapper;
import com.tokopedia.posapp.data.repository.ShopRepository;
import com.tokopedia.posapp.data.repository.ShopRepositoryImpl;
import com.tokopedia.posapp.data.source.cloud.api.AceApi;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;
import com.tokopedia.posapp.di.scope.ShopScope;
import com.tokopedia.posapp.domain.usecase.GetShopProductListUseCase;
import com.tokopedia.posapp.domain.usecase.StoreProductCacheUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 9/5/17.
 */

@ShopScope
@Module
public class ShopProductModule {

    @Provides
    AceApi provideAceApi(@AceAuth Retrofit retrofit) {
        return retrofit.create(AceApi.class);
    }

    @Provides
    ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @Provides
    GetShopMapper provideGetShopMapper() {
        return new GetShopMapper();
    }

    @Provides
    GetShopProductMapper provideGetShopProductMapper() {
        return new GetShopProductMapper();
    }

    @Provides
    ShopFactory provideShopFactory(ShopApi shopApi,
                                   AceApi aceApi,
                                   GetShopMapper getShopMapper,
                                   GetShopProductMapper getShopProductMapper) {
        return new ShopFactory(shopApi, aceApi, getShopMapper, getShopProductMapper);
    }

    @Provides
    ShopRepository provideShopRepository(ShopFactory shopFactory) {
        return new ShopRepositoryImpl(shopFactory);
    }

    @Provides
    GetShopProductListUseCase provideGetProductListUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           ShopRepository shopRepository) {
        return new GetShopProductListUseCase(threadExecutor, postExecutionThread, shopRepository);
    }

    @Provides
    StoreProductCacheUseCase provideStoreProductCacheUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             ShopRepository shopRepository) {
        return new StoreProductCacheUseCase(threadExecutor, postExecutionThread, shopRepository);
    }
}
