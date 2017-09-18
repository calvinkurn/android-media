package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.data.factory.ProductFactory;
import com.tokopedia.posapp.data.mapper.GetProductMapper;
import com.tokopedia.posapp.data.repository.ProductRepository;
import com.tokopedia.posapp.data.repository.ProductRepositoryImpl;
import com.tokopedia.posapp.data.source.cloud.api.ProductApi;
import com.tokopedia.posapp.di.scope.ProductScope;
import com.tokopedia.posapp.domain.usecase.GetProductCampaignUseCase;
import com.tokopedia.posapp.domain.usecase.GetProductUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 8/10/17.
 */

@Module
public class ProductModule {
    @Provides
    @ProductScope
    ProductApi provideProductApi(@WsV4QualifierWithErrorHander Retrofit retrofit) {
        return retrofit.create(ProductApi.class);
    }

    @Provides
    @ProductScope
    MojitoApi provideMojitoApi(@MojitoQualifier Retrofit retrofit) {
        return retrofit.create(MojitoApi.class);
    }

    @Provides
    @ProductScope
    GetProductMapper provideGetProductMapper() {
        return new GetProductMapper();
    }

    @Provides
    @ProductScope
    ProductFactory provideProductFactory(ProductApi productApi,
                                         MojitoApi mojitoApi,
                                         GetProductMapper getProductMapper) {
        return new ProductFactory(productApi, mojitoApi, getProductMapper);
    }

    @Provides
    @ProductScope
    ProductRepository provideProductRepository(ProductFactory productFactory) {
        return new ProductRepositoryImpl(productFactory);
    }

    @Provides
    @ProductScope
    GetProductCampaignUseCase provideGetProductCampaignUsecase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              ProductRepository productRepository) {
        return new GetProductCampaignUseCase(threadExecutor, postExecutionThread, productRepository);
    }

    @Provides
    @ProductScope
    GetProductUseCase provideGetProductUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               ProductRepository productRepository) {
        return new GetProductUseCase(threadExecutor, postExecutionThread, productRepository);
    }
}
