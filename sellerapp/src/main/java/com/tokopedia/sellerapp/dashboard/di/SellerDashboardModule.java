package com.tokopedia.sellerapp.dashboard.di;

import android.content.Context;
import android.content.res.Resources;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
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
import com.tokopedia.core.drawer2.domain.interactor.DeleteNotificationCacheUseCase;
import com.tokopedia.core.drawer2.domain.interactor.GetChatNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NewNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.apiservices.goldmerchant.apis.GoldMerchantApi;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.gm.common.data.source.cloud.GetShopScoreCloudSource;
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi;
import com.tokopedia.gm.common.di.GmCommonModule;
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.product.manage.item.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.product.manage.item.common.data.source.ShopInfoDataSource;
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi;
import com.tokopedia.product.manage.item.common.data.source.cloud.TomeProductApi;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.SellerModuleRouter;
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
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.dashboard.view.presenter.SellerDashboardDrawerPresenter;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

import static com.tokopedia.core.drawer2.domain.GqlQueryConstant.GET_CHAT_NOTIFICATION_QUERY;

/**
 * @author sebastianuskh on 5/8/17.
 */

@SellerDashboardScope
@Module(includes = {SellerDashboardGMCommonModule.class, GmCommonModule.class})
public class SellerDashboardModule {
    @SellerDashboardScope
    @Provides
    ShopScoreRepository provideShopScoreRepository(ShopScoreFactory shopScoreFactory) {
        return new ShopScoreRepositoryImpl(shopScoreFactory);
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
    GetChatNotificationUseCase provideTopChatNotificationUseCase(
            @Named(GET_CHAT_NOTIFICATION_QUERY) String queryString,
            GraphqlUseCase graphqlUseCase,
            LocalCacheHandler localCacheHandler) {
        return new GetChatNotificationUseCase(queryString, graphqlUseCase, localCacheHandler);
    }

    @SellerDashboardScope
    @Provides
    @Named(GET_CHAT_NOTIFICATION_QUERY)
    String provideGetChatNotificationQuery(@ApplicationContext Context context){
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.query_get_chat_notification);
    }

    @SellerDashboardScope
    @Provides
    NewNotificationUseCase provideNewNotificationUseCase(ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutionThread,
                                                         NotificationUseCase
                                                                 notificationUseCase,
                                                         DeleteNotificationCacheUseCase
                                                                 deleteNotificationCacheUseCase,
                                                         GetChatNotificationUseCase
                                                                 getChatNotificationUseCase,
                                                         LocalCacheHandler localCacheHandler) {
        return new NewNotificationUseCase(
                threadExecutor,
                postExecutionThread,
                notificationUseCase,
                deleteNotificationCacheUseCase,
                getChatNotificationUseCase,
                localCacheHandler
        );
    }

    @DeleteCacheScope
    @Provides
    DeleteShopInfoTomeUseCase provideDeleteShopInfoTomeUseCase(@ApplicationContext Context context) {
        return new DeleteShopInfoTomeUseCase(context);
    }

    @SellerDashboardScope
    @Provides
    DeleteShopInfoUseCase provideDeleteShopInfoUseCase(@ApplicationContext Context context,
                                                       DeleteShopInfoTomeUseCase deleteShopInfoTomeUseCase) {
        return new DeleteShopInfoUseCase(context, deleteShopInfoTomeUseCase);
    }

    @SellerDashboardScope
    @Provides
    CacheApiClearAllUseCase provideCacheApiClearAllUseCase(@ApplicationContext Context context) {
        return new CacheApiClearAllUseCase(context);
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
    public UserSessionInterface provideUserSession(@com.tokopedia.abstraction.common.di.qualifier.ApplicationContext Context context) {
        return new com.tokopedia.user.session.UserSession(context);
    }

    @SellerDashboardScope
    @Provides
    @com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
    public Context provideApplicationContext(@ApplicationContext Context context) {
        return context;
    }

    @SellerDashboardScope
    @Provides
    public TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context) {
        return new TkpdAuthInterceptor(context, (AbstractionRouter) context.getApplicationContext());
    }

    @SellerDashboardScope
    @Provides
    public GetShopScoreCloudSource provideGetShopScoreCloudSource(GMCommonApi gmCommonApi) {
        return new GetShopScoreCloudSource(gmCommonApi);
    }

    @SellerDashboardScope
    @Provides
    SellerDashboardDrawerPresenter provideSellerDashboardDrawerPresenter(GetShopStatusUseCase getShopStatusUseCase,
                                                                         UserSessionInterface userSessionInterface) {
        return new SellerDashboardDrawerPresenter(getShopStatusUseCase, userSessionInterface);
    }
}
