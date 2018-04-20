package com.tokopedia.posapp.shop.di;

import com.tokopedia.posapp.shop.data.source.cloud.ShopApi;

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
    ShopApi provideShopApi(Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }
}
