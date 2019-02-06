package com.tokopedia.core.drawer2.data.source;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.Notifications;
import com.tokopedia.core.drawer2.data.pojo.UserData;
import com.tokopedia.core.drawer2.data.pojo.Wallet;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.network.apiservices.drawer.DrawerService;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Herdi_WORK on 27.09.17.
 */

public class CloudAttrDataSource {
    public static final String KEY_TOKOCASH_BALANCE_CACHE = "TOKOCASH_BALANCE_CACHE";

    private DrawerService drawerService;
    private LocalCacheHandler drawerCache;

    private AnalyticsCacheHandler analyticsCacheHandler;
    private long DURATION_SAVE_TO_CACHE = 60;

    public CloudAttrDataSource(DrawerService drawerService, LocalCacheHandler drawerCache) {
        this.drawerService = drawerService;
        this.drawerCache = drawerCache;
        analyticsCacheHandler = new AnalyticsCacheHandler(new GlobalCacheManager());
    }

    public Observable<UserData> getConsumerUserAttributes(RequestParams requestParams) {
        return drawerService.getApi()
                .getConsumerDrawerData(requestParams.getParameters())
                .map(new Func1<Response<GraphqlResponse<UserData>>, UserData>() {
                    @Override
                    public UserData call(Response<GraphqlResponse<UserData>> graphqlResponseResponse) {

                        if (graphqlResponseResponse != null) {
                            if (graphqlResponseResponse.isSuccessful()) {
                                return graphqlResponseResponse.body().getData();

                            }
                        }
                        return null;
                    }
                })
                .doOnNext(setToCache());
    }

    public Observable<UserData> getSellerUserAttributes(RequestParams requestParams) {
        return drawerService.getApi()
                .getSellerDrawerData(requestParams.getParameters())
                .map(new Func1<Response<GraphqlResponse<UserData>>, UserData>() {
                    @Override
                    public UserData call(Response<GraphqlResponse<UserData>> graphqlResponseResponse) {

                        if (graphqlResponseResponse != null) {
                            if (graphqlResponseResponse.isSuccessful()) {
                                return graphqlResponseResponse.body().getData();

                            }
                        }
                        return null;
                    }
                });
    }

    private Action1<UserData> setToCache() {
        return new Action1<UserData>() {
            @Override
            public void call(UserData data) {
                if (data != null) {
                    analyticsCacheHandler.setUserDataGraphQLCache(data);
                }

                //add wallet data in cache
                if (data != null && data.getWallet() != null && data.getWallet().getLinked()) {
                    GlobalCacheManager cacheHandler = new GlobalCacheManager();
                    cacheHandler.save(KEY_TOKOCASH_BALANCE_CACHE,
                            CacheUtil.convertModelToString(data.getWallet(),
                                    new TypeToken<Wallet>() {
                                    }.getType()), DURATION_SAVE_TO_CACHE);
                }


                //add notifcation data in cache
                if (data.getNotifications() != null) {
                    Notifications notificationData = data.getNotifications();
                    int chatUnreads = notificationData.getChat() == null ? 0 : notificationData.getChat().getUnreads();
                    if (notificationData.getInbox() != null) {
                        drawerCache.putInt(DrawerNotification.CACHE_INBOX_TALK, notificationData.getInbox().getInboxTalk());
                        drawerCache.putInt(DrawerNotification.CACHE_INBOX_REVIEW, notificationData.getInbox().getInboxReputation());
                        drawerCache.putInt(DrawerNotification.CACHE_INBOX_TICKET, notificationData.getInbox().getInboxTicket());
                    }

                    drawerCache.putInt(DrawerNotification.CACHE_INBOX_MESSAGE, chatUnreads);

                    if (notificationData.getPurchase() != null) {
                        drawerCache.putInt(DrawerNotification.CACHE_PURCHASE_DELIVERY_CONFIRM, notificationData.getPurchase().getPurchaseDeliveryConfirm());
                        drawerCache.putInt(DrawerNotification.CACHE_PURCHASE_ORDER_STATUS, notificationData.getPurchase().getPurchaseOrderStatus());
                        drawerCache.putInt(DrawerNotification.CACHE_PURCHASE_PAYMENT_CONFIRM, notificationData.getPurchase().getPurchasePaymentConfirm());
                        drawerCache.putInt(DrawerNotification.CACHE_PURCHASE_REORDER, notificationData.getPurchase().getPurchaseReorder());
                        drawerCache.putInt(DrawerNotification.CACHE_PURCHASE_PAYMENT_CONF, notificationData.getPurchase().getPurchaseDeliveryConfirm());
                    }

                    if (notificationData.getSales() != null) {
                        drawerCache.putInt(DrawerNotification.CACHE_SELLING_NEW_ORDER, notificationData.getSales().getSalesNewOrder());
                        drawerCache.putInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION, notificationData.getSales().getSalesShippingConfirm());
                        drawerCache.putInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS, notificationData.getSales().getSalesShippingStatus());
                    }

                    drawerCache.putInt(DrawerNotification.CACHE_TOTAL_CART, notificationData.getTotalCart());
                    drawerCache.putInt(DrawerNotification.IS_HAS_CART, notificationData.getTotalCart() > 0 ? 1 : 0);
                    drawerCache.putInt(DrawerNotification.CACHE_TOTAL_NOTIF, notificationData
                            .getTotalNotif() - (notificationData.getInbox() == null ? 0 : notificationData.getInbox().getInboxMessage())
                            + chatUnreads);
                    drawerCache.putInt(DrawerNotification.CACHE_INCR_NOTIF, notificationData.getIncrNotif());

                    if (notificationData.getResolutionAs() != null) {
                        drawerCache.putInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_BUYER, notificationData.getResolutionAs().getResolutionAsBuyer());
                        drawerCache.putInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_SELLER, notificationData.getResolutionAs().getResolutionAsSeller());
                    }

                    drawerCache.applyEditor();
                }
            }
        };
    }
}
