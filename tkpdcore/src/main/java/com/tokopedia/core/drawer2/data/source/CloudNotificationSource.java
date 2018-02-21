package com.tokopedia.core.drawer2.data.source;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.data.mapper.NotificationMapper;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationData;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nisie on 5/5/17.
 */

public class CloudNotificationSource {

    private final Context context;
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final LocalCacheHandler drawerCache;

    public CloudNotificationSource(Context context,
                                   NotificationService notificationService,
                                   NotificationMapper notificationMapper,
                                   LocalCacheHandler drawerCache) {
        this.context = context;
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
        this.drawerCache = drawerCache;
    }

    public Observable<NotificationModel> getNotification(TKPDMapParam<String, Object> params) {
        return notificationService.getApi()
                .getNotification2(AuthUtil.generateParamsNetwork2(context, params))
                .map(notificationMapper)
                .doOnNext(saveToCache());
    }

    private Action1<NotificationModel> saveToCache() {
        return new Action1<NotificationModel>() {
            @Override
            public void call(NotificationModel notificationModel) {
                if (notificationModel != null && notificationModel.isSuccess()) {
                    NotificationData notificationData = notificationModel.getNotificationData();
                    drawerCache.putInt(DrawerNotification.CACHE_INBOX_TALK, notificationData.getInbox().getInboxTalk());
                    drawerCache.putInt(DrawerNotification.CACHE_INBOX_REVIEW, notificationData.getInbox().getInboxReputation());
                    drawerCache.putInt(DrawerNotification.CACHE_INBOX_TICKET, notificationData.getInbox().getInboxTicket());

                    drawerCache.putInt(DrawerNotification.CACHE_PURCHASE_DELIVERY_CONFIRM, notificationData.getPurchase().getPurchaseDeliveryConfirm());
                    drawerCache.putInt(DrawerNotification.CACHE_PURCHASE_ORDER_STATUS, notificationData.getPurchase().getPurchaseOrderStatus());
                    drawerCache.putInt(DrawerNotification.CACHE_PURCHASE_PAYMENT_CONFIRM, notificationData.getPurchase().getPurchasePaymentConfirm());
                    drawerCache.putInt(DrawerNotification.CACHE_PURCHASE_REORDER, notificationData.getPurchase().getPurchaseReorder());
                    drawerCache.putInt(DrawerNotification.CACHE_PURCHASE_PAYMENT_CONF, notificationData.getPurchase().getPurchasePaymentConf());


                    drawerCache.putInt(DrawerNotification.CACHE_SELLING_NEW_ORDER, notificationData.getSales().getSalesNewOrder());
                    drawerCache.putInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION, notificationData.getSales().getSalesShippingConfirm());
                    drawerCache.putInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS, notificationData.getSales().getSalesShippingStatus());

                    drawerCache.putInt(DrawerNotification.CACHE_TOTAL_CART, notificationData.getTotalCart());
                    drawerCache.putInt(DrawerNotification.IS_HAS_CART, notificationData.getTotalCart() > 0 ? 1 : 0);
                    drawerCache.putInt(DrawerNotification.CACHE_TOTAL_NOTIF, notificationData
                            .getTotalNotif() - notificationData.getInbox().getInboxMessage());
                    drawerCache.putInt(DrawerNotification.CACHE_INCR_NOTIF, notificationData.getIncrNotif());

                    drawerCache.putInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_BUYER, notificationData.getResolutionModel().getResolutionBuyer());
                    drawerCache.putInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_SELLER, notificationData.getResolutionModel().getResolutionSeller());

                    drawerCache.applyEditor();
                }
            }
        };
    }
}
