package com.tokopedia.sellerapp.dashboard.di;

import android.content.Context;
import android.content.res.Resources;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.common.ticker.api.TickerApiSeller;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.TopChatNotificationMapper;
import com.tokopedia.core.drawer2.data.repository.NotificationRepositoryImpl;
import com.tokopedia.core.drawer2.data.source.TopChatNotificationSource;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.domain.interactor.NewNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TopChatNotificationUseCase;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.apiservices.goldmerchant.apis.GoldMerchantApi;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.gm.common.data.interceptor.GMAuthInterceptor;
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl;
import com.tokopedia.gm.common.data.source.GMCommonDataSource;
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.product.manage.item.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.product.manage.item.common.data.source.ShopInfoDataSource;
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi;
import com.tokopedia.product.manage.item.common.data.source.cloud.TomeProductApi;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.product.manage.list.di.GMProductManageQualifier;
import com.tokopedia.product.manage.list.di.ProductManageScope;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.picker.data.api.GetProductListSellerApi;
import com.tokopedia.seller.product.picker.data.repository.GetProductListSellingRepositoryImpl;
import com.tokopedia.seller.product.picker.data.source.GetProductListSellingDataSource;
import com.tokopedia.seller.product.picker.domain.GetProductListSellingRepository;
import com.tokopedia.seller.shop.common.di.scope.DeleteCacheScope;
import com.tokopedia.seller.shop.common.domain.interactor.DeleteShopInfoTomeUseCase;
import com.tokopedia.seller.shop.common.domain.interactor.DeleteShopInfoUseCase;
import com.tokopedia.seller.shop.setting.data.datasource.UpdateShopScheduleDataSource;
import com.tokopedia.seller.shop.setting.data.datasource.cloud.ShopScheduleApi;
import com.tokopedia.seller.shop.setting.data.repository.UpdateShopScheduleRepositoryImpl;
import com.tokopedia.seller.shop.setting.domain.UpdateShopScheduleRepository;
import com.tokopedia.seller.shopscore.data.factory.ShopScoreFactory;
import com.tokopedia.seller.shopscore.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.seller.shopscore.data.repository.ShopScoreRepositoryImpl;
import com.tokopedia.seller.shopscore.domain.ShopScoreRepository;
import com.tokopedia.user.session.UserSessionInterface;

import javax.annotation.Resource;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 5/8/17.
 */

@SellerDashboardScope
@Module(includes = {SellerDashboardGMCommonModule.class})
public class SellerDashboardModule {
    @SellerDashboardScope
    @Provides
    ShopScoreRepository provideShopScoreRepository(ShopScoreFactory shopScoreFactory) {
        return new ShopScoreRepositoryImpl(shopScoreFactory);
    }

    @SellerDashboardScope
    @Provides
    GetProductListSellingRepository productListSellingRepository(GetProductListSellingDataSource getProductListSellingDataSource){
        return new GetProductListSellingRepositoryImpl(getProductListSellingDataSource);
    }

    @SellerDashboardScope
    @Provides
    GetProductListSellerApi provideGetProductListApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(GetProductListSellerApi.class);
    }

    @Provides
    @SellerDashboardScope
    public SellerModuleRouter provideSellerModuleRouter(@ApplicationContext Context context){
        if(context instanceof SellerModuleRouter){
            return ((SellerModuleRouter)context);
        }else{
            return null;
        }
    }

    @SellerDashboardScope
    @Provides
    GoldMerchantApi provideGoldMerchantApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(GoldMerchantApi.class);
    }

    // FOR SHOP_INFO
    @SellerDashboardScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource) {
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @SellerDashboardScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @SellerDashboardScope
    @Provides
    TomeProductApi provideTomeApi(@TomeQualifier Retrofit retrofit) {
        return retrofit.create(TomeProductApi.class);
    }

    @SellerDashboardScope
    @Provides
    SimpleDataResponseMapper<ShopModel> provideShopModelMapper() {
        return new SimpleDataResponseMapper<>();
    }

    @SellerDashboardScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @SellerDashboardScope
    @Provides
    ShopScoreDetailMapper provideShopScoreDetailMapper(@ApplicationContext Context context) {
        return new ShopScoreDetailMapper(context);
    }

    @SellerDashboardScope
    @Provides
    TickerApiSeller provideTickerApiSeller(@MojitoQualifier Retrofit retrofit) {
        return retrofit.create(TickerApiSeller.class);
    }

    @SellerDashboardScope
    @Provides
    ChatService provideChatService() {
        return new ChatService();
    }

    @SellerDashboardScope
    @Provides
    TopChatNotificationMapper provideTopChatNotificationMapper() {
        return new TopChatNotificationMapper();
    }

    @SellerDashboardScope
    @Provides
    TopChatNotificationSource provideTopChatNotificationSource(ChatService chatService,
                                                               TopChatNotificationMapper
                                                                       topChatNotificationMapper,
                                                               LocalCacheHandler drawerCache) {
        return new TopChatNotificationSource(chatService, topChatNotificationMapper, drawerCache);
    }

    @SellerDashboardScope
    @Provides
    NotificationRepository provideNotificationRepository(NotificationSourceFactory
                                                                 notificationSourceFactory,
                                                         TopChatNotificationSource topChatNotificationSource) {
        return new NotificationRepositoryImpl(notificationSourceFactory, topChatNotificationSource);
    }

    @SellerDashboardScope
    @Provides
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, DrawerHelper.DRAWER_CACHE);
    }

    @SellerDashboardScope
    @Provides
    UpdateShopScheduleRepository provideShopScheduleRepository(UpdateShopScheduleDataSource updateShopScheduleDataSource) {
        return new UpdateShopScheduleRepositoryImpl(updateShopScheduleDataSource);
    }

    @SellerDashboardScope
    @Provides
    ShopScheduleApi provideScheduleApi(@WsV4QualifierWithErrorHander Retrofit retrofit) {
        return retrofit.create(ShopScheduleApi.class);
    }

    @SellerDashboardScope
    @Provides
    TopChatNotificationUseCase provideTopChatNotificationUseCase(ThreadExecutor threadExecutor,
                                                                 PostExecutionThread postExecutionThread,

                                                                 NotificationRepository notificationRepository) {
        return new TopChatNotificationUseCase(threadExecutor, postExecutionThread,
                notificationRepository);
    }

    @SellerDashboardScope
    @Provides
    NewNotificationUseCase provideNewNotificationUseCase(ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutionThread,
                                                         NotificationUseCase
                                                                 notificationUseCase,
                                                         TopChatNotificationUseCase
                                                                 topChatNotificationUseCase) {
        return new NewNotificationUseCase(threadExecutor, postExecutionThread,
                notificationUseCase, topChatNotificationUseCase);
    }

    @DeleteCacheScope
    @Provides
    DeleteShopInfoTomeUseCase provideDeleteShopInfoTomeUseCase() {
        return new DeleteShopInfoTomeUseCase();
    }

    @SellerDashboardScope
    @Provides
    DeleteShopInfoUseCase provideDeleteShopInfoUseCase(DeleteShopInfoTomeUseCase deleteShopInfoTomeUseCase) {
        return new DeleteShopInfoUseCase(deleteShopInfoTomeUseCase);
    }

    @SellerDashboardScope
    @Provides
    CacheApiClearAllUseCase provideCacheApiClearAllUseCase() {
        return new CacheApiClearAllUseCase();
    }

    @SellerDashboardScope
    @Provides
    Resources provideResources(@ApplicationContext Context context){
        return context.getResources();
    }

    @SellerDashboardScope
    @Provides
    public AbstractionRouter provideAbstractionRouter(@ApplicationContext Context context) {
        if(context instanceof AbstractionRouter){
            return ((AbstractionRouter)context);
        }else{
            return null;
        }
    }

    @SellerDashboardScope
    @Provides
    public UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new com.tokopedia.user.session.UserSession(context);
    }
}
