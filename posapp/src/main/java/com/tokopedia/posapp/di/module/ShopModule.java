package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.shop.data.factory.ShopFactory;
import com.tokopedia.posapp.shop.data.GetShopMapper;
import com.tokopedia.posapp.shop.data.repository.ShopRepository;
import com.tokopedia.posapp.shop.data.repository.ShopRepositoryImpl;
import com.tokopedia.posapp.shop.data.source.cloud.ShopApi;
import com.tokopedia.posapp.di.scope.ShopScope;
import com.tokopedia.posapp.shop.data.source.cloud.ShopCloudSource;
import com.tokopedia.posapp.shop.domain.usecase.GetShopUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 8/3/17.
 */
// TODO: 9/20/17 fix scope structure
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
    ShopRepository provideShopRepository(ShopCloudSource shopCloudSource) {
        return new ShopRepositoryImpl(shopCloudSource);
    }

    @ShopScope
    @Provides
    GetShopUseCase provideShopUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      ShopRepository shopRepository) {
        return new GetShopUseCase(threadExecutor, postExecutionThread, shopRepository);
    }
}
