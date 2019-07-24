package com.tokopedia.gm.featured.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.gm.featured.data.GMFeaturedProductDataSource;
import com.tokopedia.gm.featured.data.cloud.api.GMFeaturedProductApi;
import com.tokopedia.gm.featured.di.scope.GMFeaturedProductScope;
import com.tokopedia.gm.featured.domain.mapper.GMFeaturedProductMapper;
import com.tokopedia.gm.featured.domain.mapper.GMFeaturedProductSubmitMapper;
import com.tokopedia.gm.featured.repository.GMFeaturedProductRepository;
import com.tokopedia.gm.featured.repository.GMFeaturedProductRepositoryImpl;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.product.manage.item.common.data.source.ShopInfoDataSource;
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by normansyahputa on 9/7/17.
 */
@Module
public class GMFeaturedProductModule {
    @GMFeaturedProductScope
    @Provides
    GMFeaturedProductApi provideFeaturedProductApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(GMFeaturedProductApi.class);
    }

    @GMFeaturedProductScope
    @Provides
    GMFeaturedProductRepository provideFeaturedProductRepository(
            GMFeaturedProductDataSource gmFeaturedProductDataSource,
            ShopInfoRepository shopInfoRepository,
            GMFeaturedProductMapper gmFeaturedProductMapper,
            GMFeaturedProductSubmitMapper gmFeaturedProductSubmitMapper) {
        return new GMFeaturedProductRepositoryImpl(gmFeaturedProductDataSource, shopInfoRepository, gmFeaturedProductMapper, gmFeaturedProductSubmitMapper);
    }

    // FOR SHOP_INFO
    @GMFeaturedProductScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource) {
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @GMFeaturedProductScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @GMFeaturedProductScope
    @Provides
    UserSessionInterface provideUserSession(UserSession userSession){
        return userSession;
    }
}
