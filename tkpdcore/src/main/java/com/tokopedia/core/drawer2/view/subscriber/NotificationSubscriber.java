package com.tokopedia.core.drawer2.view.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.R;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationData;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;

import java.net.UnknownHostException;

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
        if (e instanceof UnknownHostException) {
            viewListener.onErrorGetNotificationDrawer(
                    viewListener.getString(R.string.msg_no_connection));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    viewListener.onErrorGetNotificationDrawer(
                            viewListener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    viewListener.onErrorGetNotificationDrawer(
                            viewListener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    viewListener.onErrorGetNotificationDrawer(
                            viewListener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    viewListener.onErrorGetNotificationDrawer(
                            viewListener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    viewListener.onErrorGetNotificationDrawer(
                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof MessageErrorException
                && e.getLocalizedMessage() != null) {
            viewListener.onErrorGetNotificationDrawer(e.getLocalizedMessage());
        } else {
            viewListener.onErrorGetNotificationDrawer(
                    viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(NotificationModel notificationModel) {
        if(notificationModel.isSuccess())
            viewListener.onGetNotificationDrawer(convertToViewModel(notificationModel.getNotificationData()));
        else
            viewListener.onErrorGetNotificationDrawer(notificationModel.getErrorMessage());
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
