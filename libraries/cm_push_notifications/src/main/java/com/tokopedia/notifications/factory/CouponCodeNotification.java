package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.receiver.CMBroadcastReceiver;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public class CouponCodeNotification extends BaseNotification {

    CouponCodeNotification(Context context, BaseNotificationModel baseNotificationModel) {
        super(context, baseNotificationModel);
    }

    @Override
    public Notification createNotification() {
        NotificationCompat.Builder builder = getBuilder();
        builder.setContentTitle(baseNotificationModel.getTitle());
        builder.setContentText(baseNotificationModel.getMessage());
        builder.setSmallIcon(getDrawableIcon());
        builder.setContentIntent(createCouponCodePendingIntent(baseNotificationModel.getCustomValues().optString(CMConstant.CustomValuesKeys.COUPON_CODE), baseNotificationModel.getAppLink()));
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.getNotificationId(), getRequestCode()));
        builder.setAutoCancel(true);
        if (!baseNotificationModel.getDetailMessage().isEmpty())
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(baseNotificationModel.getDetailMessage()));
        return builder.build();
    }

    private PendingIntent createCouponCodePendingIntent(String couponCode, String appLinks) {
        PendingIntent resultPendingIntent;
        Intent intent = new Intent(context, CMBroadcastReceiver.class);
        intent.setAction(CMConstant.ReceiverAction.ACTION_ON_COPY_COUPON_CODE);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.getNotificationId());
        intent.putExtra(CMConstant.CouponCodeExtra.COUPON_CODE, couponCode);
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_APP_LINK, appLinks);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    getRequestCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        } else {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    getRequestCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
        return resultPendingIntent;
    }

}
