package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.PosGatewayAuth;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.di.qualifier.CloudSource;
import com.tokopedia.posapp.di.qualifier.LocalSource;
import com.tokopedia.posapp.di.scope.ProductScope;
import com.tokopedia.posapp.product.common.data.repository.ProductCloudRepository;
import com.tokopedia.posapp.product.common.data.repository.ProductLocalRepository;
import com.tokopedia.posapp.product.common.data.repository.ProductRepository;
import com.tokopedia.posapp.product.common.data.source.cloud.ProductCloudSource;
import com.tokopedia.posapp.product.common.data.source.local.ProductLocalSource;
import com.tokopedia.posapp.product.productdetail.data.source.cloud.api.ProductApi;
import com.tokopedia.posapp.product.productdetail.domain.usecase.GetProductUseCase;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;
import com.tokopedia.posapp.product.productlist.domain.usecase.GetAllProductUseCase;
import com.tokopedia.posapp.product.productlist.domain.usecase.GetProductListUseCase;

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
    @CloudSource
    ProductRepository provideProductCloudRepository(ProductCloudSource productCloudSource) {
        return new ProductCloudRepository(productCloudSource);
    }

    @Provides
    @LocalSource
    ProductRepository provideProductLocalRepository(ProductLocalSource source) {
        return new ProductLocalRepository(source);
    }
}
