package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.HistoryNotification;
import com.tokopedia.pushnotif.SummaryNotification;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;
import com.tokopedia.pushnotif.model.SummaryNotificationModel;

/**
 * @author ricoharisin .
 */

public class SummaryNotificationFactory extends BaseNotificationFactory {


    public SummaryNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public Notification createNotification(ApplinkNotificationModel applinkNotificationModel, int notificationType) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL);
        builder.setContentTitle(getTitleSummary(notificationType));
        builder.setSmallIcon(getDrawableIcon());

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        HistoryNotification.storeNotification(applinkNotificationModel.getFullName(),
                applinkNotificationModel.getSummary(), notificationType);

        SummaryNotificationModel summaryNotificationModel = SummaryNotification.generateSummaryNotificationModel(context, notificationType);

        if (summaryNotificationModel.getHistoryString().size() == 1) return null;

        for (String s : summaryNotificationModel.getHistoryString()) {
            inboxStyle.addLine(s);
        }

        inboxStyle.setSummaryText(summaryNotificationModel.getSummaryText());

        builder.setContentText(summaryNotificationModel.getHistoryString().get(0));
        builder.setLargeIcon(getBitmapLargeIcon());
        builder.setStyle(inboxStyle);
        builder.setGroupSummary(true);
        builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));
        builder.setContentIntent(createPendingIntent(getGenericApplinks(notificationType), notificationType));
        builder.setDeleteIntent(createDismissPendingIntent(notificationType));

        return builder.build();
    }

    private String getGenericApplinks(int notficationType) {
        if (notficationType == Constant.NotificationId.TALK) {
            return ApplinkConst.TALK;
        } else {
            return ApplinkConst.TOPCHAT_IDLESS;
        }
    }

    private String getTitleSummary(int notficationType) {
        if (notficationType == Constant.NotificationId.TALK) {
            return "Tokopedia - Diskusi";
        } else {
            return "Tokopedia - Chat";
        }
    }
}
