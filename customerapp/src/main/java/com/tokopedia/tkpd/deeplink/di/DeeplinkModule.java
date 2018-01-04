package com.tokopedia.tkpd.deeplink.di;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.product.edit.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenter;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 1/4/18.
 */

@Module
public class DeeplinkModule {

    @Provides
    @DeeplinkScope
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @Provides
    @DeeplinkScope
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context,
                                                 ShopInfoDataSource shopInfoDataSource) {
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }
}
