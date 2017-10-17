package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.AceAuth;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.PosNoAuth;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.data.factory.ProductFactory;
import com.tokopedia.posapp.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.data.mapper.GetProductMapper;
import com.tokopedia.posapp.data.repository.ProductRepository;
import com.tokopedia.posapp.data.repository.ProductRepositoryImpl;
import com.tokopedia.posapp.data.source.cloud.api.AceApi;
import com.tokopedia.posapp.data.source.cloud.api.ProductApi;
import com.tokopedia.posapp.data.source.cloud.api.pos.PosProductApi;
import com.tokopedia.posapp.di.scope.ProductScope;
import com.tokopedia.posapp.domain.usecase.GetProductCampaignUseCase;
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
    PosProductApi providePosProductApi(@PosNoAuth Retrofit retrofit) {
        return retrofit.create(PosProductApi.class);
    }

    @Provides
    ProductApi provideProductApi(@WsV4QualifierWithErrorHander Retrofit retrofit) {
        return retrofit.create(ProductApi.class);
    }

    @Provides
    MojitoApi provideMojitoApi(@MojitoQualifier Retrofit retrofit) {
        return retrofit.create(MojitoApi.class);
    }

    @Provides
    AceApi provideAceApi(@AceAuth Retrofit retrofit) {
        return retrofit.create(AceApi.class);
    }

    @Provides
    GetProductMapper provideGetProductMapper() {
        return new GetProductMapper();
    }

    @Provides
    GetProductListMapper provideGetProductListMapper() {
        return new GetProductListMapper();
    }

    @Provides
    ProductFactory provideProductFactory(PosProductApi posProductApi,
                                         ProductApi productApi,
                                         MojitoApi mojitoApi,
                                         AceApi aceApi,
                                         GetProductMapper getProductMapper,
                                         GetProductListMapper getProductListMapper) {
        return new ProductFactory(posProductApi, productApi, mojitoApi, aceApi, getProductMapper, getProductListMapper);
    }

    @Provides
    ProductRepository provideProductRepository(ProductFactory productFactory) {
        return new ProductRepositoryImpl(productFactory);
    }

    @Provides
    GetProductCampaignUseCase provideGetProductCampaignUsecase(ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread,
                                                               ProductRepository productRepository) {
        return new GetProductCampaignUseCase(threadExecutor, postExecutionThread, productRepository);
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
