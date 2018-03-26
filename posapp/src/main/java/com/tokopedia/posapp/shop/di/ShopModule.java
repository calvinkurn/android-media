package com.tokopedia.posapp.shop.di;

import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.common.PosApiModule;
import com.tokopedia.posapp.shop.data.GetShopMapper;
import com.tokopedia.posapp.shop.data.repository.ShopRepository;
import com.tokopedia.posapp.shop.data.source.cloud.ShopApi;
import com.tokopedia.posapp.shop.domain.usecase.GetShopUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 8/3/17.
 */
@Module(includes = PosApiModule.class)
public class ShopModule {
    @ShopScope
    @Provides
    ShopApi provideShopApi(Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }
}
