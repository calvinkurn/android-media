package com.tokopedia.core.drawer2.view.subscriber;

import com.tokopedia.core2.R;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationData;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;

import rx.Subscriber;

/**
 * Created by nisie on 5/5/17.
 */

public class NotificationSubscriber extends Subscriber<NotificationModel> {

    private final DrawerDataListener viewListener;

    public NotificationSubscriber(DrawerDataListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetNotificationDrawer(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(NotificationModel notificationModel) {
        if (notificationModel.isSuccess())
            viewListener.onGetNotificationDrawer(
                    convertToViewModel(
                            notificationModel.getNotificationData()));
        else
            viewListener.onErrorGetNotificationDrawer(
                    viewListener.getString(R.string.default_request_error_unknown));
    }

    private DrawerNotification convertToViewModel(NotificationData notificationData) {
        DrawerNotification drawerNotification = new DrawerNotification();
        drawerNotification.setInboxMessage(notificationData.getInbox().getInboxMessage());
        drawerNotification.setInboxResCenter(notificationData.getResolution());
        drawerNotification.setInboxReview(notificationData.getInbox().getInboxReputation());
        drawerNotification.setInboxTalk(notificationData.getInbox().getInboxTalk());
        drawerNotification.setInboxTicket(notificationData.getInbox().getInboxTicket());

        drawerNotification.setPurchaseDeliveryConfirm(notificationData.getPurchase().getPurchaseDeliveryConfirm());
        drawerNotification.setPurchaseOrderStatus(notificationData.getPurchase().getPurchaseOrderStatus());
        drawerNotification.setPurchasePaymentConfirm(notificationData.getPurchase().getPurchasePaymentConfirm());
        drawerNotification.setPurchaseReorder(notificationData.getPurchase().getPurchaseReorder());

        drawerNotification.setSellingNewOrder(notificationData.getSales().getSalesNewOrder());
        drawerNotification.setSellingShippingConfirmation(notificationData.getSales().getSalesShippingConfirm());
        drawerNotification.setSellingShippingStatus(notificationData.getSales().getSalesShippingStatus());
        drawerNotification.setTotalNotif(notificationData.getTotalNotif());
        drawerNotification.setIncrNotif(notificationData.getIncrNotif());
        drawerNotification.setTotalCart(notificationData.getTotalCart());
        return drawerNotification;
    }
}
