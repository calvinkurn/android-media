package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.bubbles.factory.BubblesFactory;
import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.pushnotif.data.constant.Constant;
import com.tokopedia.pushnotif.data.repository.HistoryRepository;
import com.tokopedia.pushnotif.data.db.model.HistoryNotification;
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel;
import com.tokopedia.pushnotif.util.BubbleChatNotificationHelper;

import java.util.List;

import androidx.core.app.NotificationCompat;

/**
 * @author ricoharisin .
 */

public class SummaryNotificationFactory extends BaseNotificationFactory {

    private List<HistoryNotification> listHistoryNotification;

    private BubblesFactory bubblesFactory;
    private BubbleChatNotificationHelper bubbleChatNotificationHelper;


    public SummaryNotificationFactory(Context context) {
        super(context);
        createNotificationInboxStyle();
    }

    @Override
    public Notification createNotification(ApplinkNotificationModel applinkNotificationModel, int notificationType, int notificationId) {
        storeToTransaction(context, notificationType, notificationId, applinkNotificationModel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL);
        builder.setContentTitle(getTitleSummary(notificationType));
        builder.setSmallIcon(getDrawableIcon());

        HistoryRepository.storeNotification(
                context,
                applinkNotificationModel,
                notificationType,
                ApplinkNotificationHelper.getNotificationId(applinkNotificationModel.getApplinks())
        );

        listHistoryNotification = HistoryRepository.getListHistoryNotification(context, notificationType);

        for (HistoryNotification history : listHistoryNotification) {
            inboxStyle.addLine(genarateContentText(history));
        }

        inboxStyle.setSummaryText(generateSummaryText(notificationType));

        if (listHistoryNotification!= null && listHistoryNotification.size() > 0) {
            builder.setContentText(genarateContentText(listHistoryNotification.get(0)));
        }
        builder.setLargeIcon(getBitmapLargeIcon());
        builder.setStyle(inboxStyle);
        if (ApplinkNotificationHelper.allowGroup()) {
            builder.setGroupSummary(true);
            builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));
            builder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN);
        }
        PendingIntent pendingContentIntent = createPendingIntent(getGenericApplinks(notificationType), notificationType, 0, applinkNotificationModel);
        builder.setContentIntent(pendingContentIntent);
        builder.setDeleteIntent(createDismissPendingIntent(notificationType, 0, applinkNotificationModel));
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

    public String genarateContentText(HistoryNotification historyNotification) {
        return historyNotification.getSenderName() + " : " + historyNotification.getMessage();

    }
}
