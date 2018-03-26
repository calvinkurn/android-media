package com.tokopedia.posapp.di.module;

import com.tokopedia.core.network.di.qualifier.PosGatewayAuth;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.di.scope.ProductScope;
import com.tokopedia.posapp.product.productdetail.data.source.cloud.api.ProductApi;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;

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
}
