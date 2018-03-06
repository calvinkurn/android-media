package com.tokopedia.posapp.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.PosGatewayAuth;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.cache.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;
import com.tokopedia.posapp.product.ProductFactory;
import com.tokopedia.posapp.product.productdetail.data.mapper.GetProductMapper;
import com.tokopedia.posapp.product.ProductRepository;
import com.tokopedia.posapp.product.ProductRepositoryImpl;
import com.tokopedia.posapp.product.productdetail.data.source.cloud.api.ProductApi;
import com.tokopedia.posapp.di.scope.ProductScope;
import com.tokopedia.posapp.product.productdetail.domain.usecase.GetProductUseCase;
import com.tokopedia.posapp.etalase.StoreProductCacheUseCase;

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
    ProductListApi provideGatewayProductApi(@PosGatewayAuth Retrofit retrofit) {
        return retrofit.create(ProductListApi.class);
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
    GetProductListMapper provideGetGatewayProductListMapper(Gson gson) {
        return new GetProductListMapper(gson);
    }

    @Provides
    ProductFactory provideProductFactory(ProductListApi productListApi,
                                         ProductApi productApi,
                                         GetProductMapper getProductMapper,
                                         GetProductListMapper getProductListMapper) {
        return new ProductFactory(
                productListApi,
                productApi,
                getProductMapper,
                getProductListMapper
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
    StoreProductCacheUseCase provideStoreProductCacheUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             ProductRepository productRepository) {
        return new StoreProductCacheUseCase(threadExecutor, postExecutionThread, productRepository);
    }
}
