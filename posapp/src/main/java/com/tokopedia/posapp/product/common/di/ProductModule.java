package com.tokopedia.posapp.product.common.di;

import com.tokopedia.posapp.product.common.data.source.cloud.ProductApi;

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
    ProductApi provideProductApi(Retrofit retrofit) {
        return retrofit.create(ProductApi.class);
    }
}
