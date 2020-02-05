package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;

/**
 * @author ricoharisin .
 */

public class ChatNotificationFactory extends BaseNotificationFactory {

    private static String REPLY_KEY = "replay_chat_key";
    private static String REPLY_LABEL = "Replay";

    public ChatNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public Notification createNotification(ApplinkNotificationModel applinkNotificationModel, int notifcationType, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL);
        builder.setContentTitle(applinkNotificationModel.getDesc());
        builder.setContentText(applinkNotificationModel.getSummary());
        builder.setSmallIcon(getDrawableIcon());
        builder.setLargeIcon(getBitmap(applinkNotificationModel.getThumbnail()));
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(applinkNotificationModel.getSummary()));
        if (ApplinkNotificationHelper.allowGroup()) {
            builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));
        }
        builder.setContentIntent(createPendingIntent(applinkNotificationModel.getApplinks(), notifcationType, notificationId));
        builder.setDeleteIntent(createDismissPendingIntent(notifcationType, notificationId));
        builder.setAutoCancel(true);

        if (isAllowBell()) {
            builder.setSound(getRingtoneUri());
            if (isAllowVibrate()) builder.setVibrate(getVibratePattern());
        }

        builder.setShowWhen(true);
        builder.addAction(replyAction(applinkNotificationModel.getApplinks(), notificationId));

        return builder.build();
    }

    private NotificationCompat.Action replyAction(String appLinks, int notificationId) {

        return new NotificationCompat.Action.Builder(
                android.R.drawable.stat_notify_chat, REPLY_LABEL,
                getReplyChatPendingIntent(getMessageId(appLinks), notificationId))
                .addRemoteInput(remoteInput())
                .setAllowGeneratedReplies(true)
                .build();
    }

    private RemoteInput remoteInput() {
        return new RemoteInput.Builder(REPLY_KEY).setLabel(REPLY_LABEL).build();
    }

    private PendingIntent getReplyChatPendingIntent(String mMessageId, int notificationId) {

        Intent intent = new Intent("com.tokopedia.topchat.chatroom.service.NotificationChatService.REPLY_CHAT");
        intent.setPackage(context.getPackageName());
        intent.putExtra("message_chat_id", mMessageId);
        intent.putExtra("notification_id", notificationId);

        return PendingIntent.getService(context, 100, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private String getMessageId(String appLinks) {
        String startString = "tokopedia://topchat/";
        int lengthStart = startString.length();
        int lastIndex = appLinks.lastIndexOf("?");
        return appLinks.substring(lengthStart, lastIndex);
    }

}
