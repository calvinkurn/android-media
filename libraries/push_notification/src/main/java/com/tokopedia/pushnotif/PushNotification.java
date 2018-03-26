package com.tokopedia.pushnotif;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.PushNotificationGeneratedDatabaseHolder;
import com.tokopedia.pushnotif.factory.SummaryNotificationFactory;
import com.tokopedia.pushnotif.factory.TalkNotificationFactory;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;

/**
 * @author ricoharisin .
 */

public class PushNotification {

    public static void initDatabase(Context applicationContext) {
        try{
            FlowManager.getConfig();
        } catch (IllegalStateException e) {
            FlowManager.init(new FlowConfig.Builder(applicationContext).build());
        }
        FlowManager.initModule(PushNotificationGeneratedDatabaseHolder.class);
    }

    public static void notify(Context context, Bundle data) {
        ApplinkNotificationModel applinkNotificationModel = ApplinkNotificationHelper.convertToApplinkModel(data);

        if (ApplinkNotificationHelper.allowToShow(context, applinkNotificationModel.getToUserId())) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            int notificationId = ApplinkNotificationHelper.generateNotifictionId(applinkNotificationModel.getApplinks());

            if (notificationId == Constant.NotificationId.TALK) {
                Notification notifTalk = new TalkNotificationFactory(context)
                        .createTalkNotification(applinkNotificationModel, notificationId);

                Notification notifSummary = new SummaryNotificationFactory(context)
                        .createSummaryNotification(applinkNotificationModel, notificationId);

                notificationManagerCompat.notify(ApplinkNotificationHelper.getNotificationId(applinkNotificationModel.getApplinks()),
                        notifTalk);

                notificationManagerCompat.notify(notificationId, notifSummary);
            }
        }
    }
}
