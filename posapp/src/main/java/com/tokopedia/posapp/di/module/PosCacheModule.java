package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.AceAuth;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.data.factory.ShopFactory;
import com.tokopedia.posapp.data.mapper.GetShopProductMapper;
import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.repository.ShopRepository;
import com.tokopedia.posapp.data.repository.ShopRepositoryImpl;
import com.tokopedia.posapp.data.source.cloud.api.AceApi;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;
import com.tokopedia.posapp.di.scope.PosCacheScope;
import com.tokopedia.posapp.domain.usecase.GetProductListUseCase;
import com.tokopedia.posapp.domain.usecase.StoreProductCacheUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 9/5/17.
 */
@Module
public class PosCacheModule {
    @PosCacheScope
    @Provides
    AceApi provideAceApi(@AceAuth Retrofit retrofit) {
        return retrofit.create(AceApi.class);
    }

    @PosCacheScope
    @Provides
    ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @PosCacheScope
    @Provides
    GetShopMapper provideGetShopMapper() {
        return new GetShopMapper();
    }

    @PosCacheScope
    @Provides
    GetShopProductMapper provideGetShopProductMapper() {
        return new GetShopProductMapper();
    }

    @PosCacheScope
    @Provides
    ShopFactory provideShopFactory(ShopApi shopApi,
                                   AceApi aceApi,
                                   GetShopMapper getShopMapper,
                                   GetShopProductMapper getShopProductMapper) {
        return new ShopFactory(shopApi, aceApi, getShopMapper, getShopProductMapper);
    }

    @PosCacheScope
    @Provides
    ShopRepository provideShopRepository(ShopFactory shopFactory) {
        return new ShopRepositoryImpl(shopFactory);
    }

    @PosCacheScope
    @Provides
    GetProductListUseCase provideGetProductListUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       ShopRepository shopRepository) {
        return new GetProductListUseCase(threadExecutor, postExecutionThread, shopRepository);
    }

    @PosCacheScope
    @Provides
    StoreProductCacheUseCase provideStoreProductCacheUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             ShopRepository shopRepository) {
        return new StoreProductCacheUseCase(threadExecutor, postExecutionThread, shopRepository);
    }
}
