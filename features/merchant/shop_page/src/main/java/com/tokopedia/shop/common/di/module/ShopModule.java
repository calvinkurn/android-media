package com.tokopedia.shop.common.di.module;

import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopWSApi;
import com.tokopedia.shop.common.di.ShopCommonModule;
import com.tokopedia.shop.common.di.ShopQualifier;
import com.tokopedia.shop.common.di.ShopWSQualifier;
import com.tokopedia.shop.common.di.scope.ShopScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ShopScope
@Module(includes = ShopCommonModule.class)
public class ShopModule {

    @ShopScope
    @Provides
    public ShopApi provideShopApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @ShopScope
    @Provides
    public ShopWSApi provideShopWsApi(@ShopWSQualifier Retrofit retrofit) {
        return retrofit.create(ShopWSApi.class);
    }
}