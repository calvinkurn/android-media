package com.tokopedia.posapp.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.data.factory.ShopFactory;
import com.tokopedia.posapp.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.repository.ShopRepository;
import com.tokopedia.posapp.data.repository.ShopRepositoryImpl;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;
import com.tokopedia.posapp.domain.usecase.GetShopUseCase;
import com.tokopedia.posapp.di.scope.ShopScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 8/3/17.
 */
@Module
public class ShopModule {
    @ShopScope
    @Provides
    ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @ShopScope
    @Provides
    GetShopMapper provideGetShopMapper() {
        return new GetShopMapper();
    }

    @ShopScope
    @Provides
    GetProductListMapper provideGetProductListMapper() {
        return new GetProductListMapper();
    }

    @ShopScope
    @Provides
    ShopFactory provideShopFactory(ShopApi shopApi,
                                   GetShopMapper shopMapper,
                                   GetProductListMapper getProductListMapper) {
        return new ShopFactory(shopApi, shopMapper, getProductListMapper);
    }

    @ShopScope
    @Provides
    ShopRepository provideShopRepository(ShopFactory shopFactory) {
        return new ShopRepositoryImpl(shopFactory);
    }

    @ShopScope
    @Provides
    GetShopUseCase provideShopUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          ShopRepository shopRepository) {
        return new GetShopUseCase(threadExecutor, postExecutionThread, shopRepository);
    }
}
