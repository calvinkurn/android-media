package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.HistoryNotification;
import com.tokopedia.pushnotif.SummaryNotification;
import com.tokopedia.pushnotif.db.model.HistoryNotificationDB;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;
import com.tokopedia.pushnotif.model.SummaryNotificationModel;

import java.util.List;

/**
 * @author ricoharisin .
 */

public class SummaryNotificationFactory extends BaseNotificationFactory {

    private List<HistoryNotificationDB> listHistoryNotification;


    public SummaryNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public Notification createNotification(ApplinkNotificationModel applinkNotificationModel, int notificationType, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL);
        builder.setContentTitle(getTitleSummary(notificationType));
        builder.setSmallIcon(getDrawableIcon());

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        HistoryNotification.storeNotification(context, applinkNotificationModel.getFullName(),
                applinkNotificationModel.getSummary(), notificationType,
                ApplinkNotificationHelper.getNotificationId(applinkNotificationModel.getApplinks()));

        listHistoryNotification = HistoryNotification.getListHistoryNotification(context, notificationType);

        for (HistoryNotificationDB history : listHistoryNotification) {
            inboxStyle.addLine(genarateContentText(history));
        }

        inboxStyle.setSummaryText(SummaryNotification.generateSummaryText(notificationType, listHistoryNotification.size()));

        if (listHistoryNotification!= null && listHistoryNotification.size() > 0) {
            builder.setContentText(genarateContentText(listHistoryNotification.get(0)));
        }
        builder.setLargeIcon(getBitmapLargeIcon());
        builder.setStyle(inboxStyle);
        if (ApplinkNotificationHelper.allowGroup()) {
            builder.setGroupSummary(true);
            builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));
            builder.setGroupAlertBehavior(Notification.GROUP_ALERT_CHILDREN);
        }
        builder.setContentIntent(createPendingIntent(getGenericApplinks(notificationType), notificationType, 0));
        builder.setDeleteIntent(createDismissPendingIntent(notificationType, 0));
        builder.setAutoCancel(true);

        if (isAllowBell()) {
            builder.setSound(getRingtoneUri());
            if (isAllowVibrate()) builder.setVibrate(getVibratePattern());
        }

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

    public int getTotalSummary() {
        return listHistoryNotification.size();
    }

    public String genarateContentText(HistoryNotificationDB historyNotificationDB) {
        return historyNotificationDB.getSenderName() + " : " + historyNotificationDB.getMessage();

    }
}
