package com.tokopedia.tkpd.deeplink.di.module;

import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.tkpd.deeplink.data.factory.ShopInfoSourceFactory;
import com.tokopedia.tkpd.deeplink.data.repository.ShopInfoRepository;
import com.tokopedia.tkpd.deeplink.data.repository.ShopInfoRepositoryImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 1/5/18.
 */
@Module
public class GetShopInfoModule {
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @Provides
    ShopInfoRepository provideShopInfoRepository(ShopInfoSourceFactory factory) {
        return new ShopInfoRepositoryImpl(factory);
    }
}
