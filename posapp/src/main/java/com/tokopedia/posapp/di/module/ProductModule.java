package com.tokopedia.posapp.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.PosGatewayAuth;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.data.factory.ProductFactory;
import com.tokopedia.posapp.data.mapper.GetGatewayProductListMapper;
import com.tokopedia.posapp.data.mapper.GetProductMapper;
import com.tokopedia.posapp.data.repository.ProductRepository;
import com.tokopedia.posapp.data.repository.ProductRepositoryImpl;
import com.tokopedia.posapp.data.source.cloud.api.GatewayProductApi;
import com.tokopedia.posapp.data.source.cloud.api.ProductApi;
import com.tokopedia.posapp.di.scope.ProductScope;
import com.tokopedia.posapp.domain.usecase.GetProductListUseCase;
import com.tokopedia.posapp.domain.usecase.GetProductUseCase;
import com.tokopedia.posapp.domain.usecase.StoreProductCacheUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 8/10/17.
 */
@ProductScope
@Module
public class ProductModule {
    @Provides
    GatewayProductApi provideGatewayProductApi(@PosGatewayAuth Retrofit retrofit) {
        return retrofit.create(GatewayProductApi.class);
    }

    @Provides
    ProductApi provideProductApi(@WsV4QualifierWithErrorHander Retrofit retrofit) {
        return retrofit.create(ProductApi.class);
    }

    @Provides
    GetProductMapper provideGetProductMapper() {
        return new GetProductMapper();
    }

    @Provides
    GetGatewayProductListMapper provideGetGatewayProductListMapper(Gson gson) {
        return new GetGatewayProductListMapper(gson);
    }

    @Provides
    ProductFactory provideProductFactory(GatewayProductApi gatewayProductApi,
                                         ProductApi productApi,
                                         GetProductMapper getProductMapper,
                                         GetGatewayProductListMapper getGatewayProductListMapper) {
        return new ProductFactory(
                gatewayProductApi,
                productApi,
                getProductMapper,
                getGatewayProductListMapper
        );
    }

    @Provides
    ProductRepository provideProductRepository(ProductFactory productFactory) {
        return new ProductRepositoryImpl(productFactory);
    }

    @Provides
    GetProductUseCase provideGetProductUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               ProductRepository productRepository) {
        return new GetProductUseCase(threadExecutor, postExecutionThread, productRepository);
    }

    @Provides
    GetProductListUseCase provideGetProductListUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       ProductRepository productRepository) {
        return new GetProductListUseCase(threadExecutor, postExecutionThread, productRepository);
    }

    @Provides
    StoreProductCacheUseCase provideStoreProductCacheUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             ProductRepository productRepository) {
        return new StoreProductCacheUseCase(threadExecutor, postExecutionThread, productRepository);
    }
}
