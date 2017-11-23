package com.tokopedia.sellerapp.dashboard.di;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.common.ticker.api.TickerApiSeller;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.repository.NotificationRepositoryImpl;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.apiservices.goldmerchant.apis.GoldMerchantApi;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoAuth;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.product.edit.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeApi;
import com.tokopedia.seller.shop.setting.data.datasource.UpdateShopScheduleDataSource;
import com.tokopedia.seller.shop.setting.data.datasource.cloud.ShopScheduleApi;
import com.tokopedia.seller.shop.setting.data.repository.UpdateShopScheduleRepositoryImpl;
import com.tokopedia.seller.shop.setting.domain.UpdateShopScheduleRepository;
import com.tokopedia.seller.shopscore.data.factory.ShopScoreFactory;
import com.tokopedia.seller.shopscore.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.seller.shopscore.data.repository.ShopScoreRepositoryImpl;
import com.tokopedia.seller.shopscore.domain.ShopScoreRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 5/8/17.
 */

@SellerDashboardScope
@Module
public class SellerDashboardModule {
    @SellerDashboardScope
    @Provides
    ShopScoreRepository provideShopScoreRepository (ShopScoreFactory shopScoreFactory){
        return new ShopScoreRepositoryImpl(shopScoreFactory);
    }

    @SellerDashboardScope
    @Provides
    GoldMerchantApi provideGoldMerchantApi(@GoldMerchantQualifier Retrofit retrofit){
        return retrofit.create(GoldMerchantApi.class);
    }

    // FOR SHOP_INFO
    @SellerDashboardScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @SellerDashboardScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @SellerDashboardScope
    @Provides
    TomeApi provideTomeApi(@TomeQualifier Retrofit retrofit){
        return retrofit.create(TomeApi.class);
    }

    @SellerDashboardScope
    @Provides
    SimpleDataResponseMapper<ShopModel> provideShopModelMapper(){
        return new SimpleDataResponseMapper<>();
    }

    @SellerDashboardScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager(){
        return new GlobalCacheManager();
    }

    @SellerDashboardScope
    @Provides
    ShopScoreDetailMapper provideShopScoreDetailMapper(@ApplicationContext Context context){
        return new ShopScoreDetailMapper(context);
    }

    @SellerDashboardScope
    @Provides
    TickerApiSeller provideTickerApiSeller(@MojitoQualifier Retrofit retrofit){
        return retrofit.create(TickerApiSeller.class);
    }

    @SellerDashboardScope
    @Provides
    NotificationRepository provideNotificationRepository(NotificationSourceFactory notificationSourceFactory){
        return new NotificationRepositoryImpl(notificationSourceFactory);
    }

    @SellerDashboardScope
    @Provides
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context){
        return new LocalCacheHandler(context, DrawerHelper.DRAWER_CACHE);
    }

    @SellerDashboardScope
    @Provides
    UpdateShopScheduleRepository provideShopScheduleRepository(UpdateShopScheduleDataSource updateShopScheduleDataSource){
        return new UpdateShopScheduleRepositoryImpl(updateShopScheduleDataSource);
    }

    @SellerDashboardScope
    @Provides
    ShopScheduleApi provideScheduleApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ShopScheduleApi.class);
    }
}
