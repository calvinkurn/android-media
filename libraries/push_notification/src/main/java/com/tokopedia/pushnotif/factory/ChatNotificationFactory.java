package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.tokopedia.bubbles.data.model.BubbleHistoryItemModel;
import com.tokopedia.bubbles.data.model.BubbleNotificationModel;
import com.tokopedia.bubbles.factory.BubblesFactory;
import com.tokopedia.bubbles.factory.BubblesFactoryImpl;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.pushnotif.data.constant.Constant;
import com.tokopedia.pushnotif.data.db.model.HistoryNotification;
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel;
import com.tokopedia.pushnotif.data.repository.HistoryRepository;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ricoharisin .
 */

public class ChatNotificationFactory extends BaseNotificationFactory {

    private static String INTENT_ACTION_REPLY = "NotificationChatServiceReceiver.REPLY_CHAT";

    private static String REPLY_KEY = "reply_chat_key";
    private static String REPLY_LABEL = "Reply";
    private static String MESSAGE_ID = "message_chat_id";
    private static String NOTIFICATION_ID = "notification_id";
    private static String USER_ID = "user_id";

    private RemoteConfig remoteConfig;
    private BubblesFactory bubblesFactory;

    private List<HistoryNotification> listHistoryNotification;

    public ChatNotificationFactory(Context context) {
        super(context);
        remoteConfig = new FirebaseRemoteConfigImpl(context);
        bubblesFactory = new BubblesFactoryImpl(context);
    }

    @Override
    public Notification createNotification(ApplinkNotificationModel applinkNotificationModel, int notificationType, int notificationId) {
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
        builder.setContentIntent(createPendingIntent(applinkNotificationModel.getApplinks(), notificationType, notificationId));
        builder.setDeleteIntent(createDismissPendingIntent(notificationType, notificationId));
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        if (isAllowBell()) {
            builder.setSound(getRingtoneUri());
            if (isAllowVibrate()) builder.setVibrate(getVibratePattern());
        }

        if(isEnableReplyChatNotification()) {
            if (GlobalConfig.isSellerApp()) {
                builder.setShowWhen(true);
                builder.addAction(replyAction(applinkNotificationModel.getApplinks(), notificationId));
            }
        }

        setupBubble(builder, applinkNotificationModel, notificationType, notificationId);

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
        return new RemoteInput.Builder(REPLY_KEY).setLabel(REPLY_LABEL)
                .build();
    }

    private PendingIntent getReplyChatPendingIntent(String mMessageId, int notificationId) {
        UserSession userSession = new UserSession(context);

        Intent intent = new Intent(INTENT_ACTION_REPLY);
        intent.setPackage(context.getPackageName());
        intent.putExtra(MESSAGE_ID, mMessageId);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        intent.putExtra(USER_ID, userSession.getUserId());

        return PendingIntent.getBroadcast(context, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private String getMessageId(String appLinks) {
        try {
            return Uri.parse(appLinks).getLastPathSegment();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "0";
        }
    }

    private Boolean isEnableReplyChatNotification() {
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PUSH_NOTIFICATION_CHAT_SELLER, false);
    }

    private void setupBubble(NotificationCompat.Builder builder, ApplinkNotificationModel applinkNotificationModel, int notificationType, int notificationId) {
        updateBubblesShortcuts(notificationType);
        updateBubblesBuilder(builder, applinkNotificationModel, notificationType, notificationId);
    }

    private void updateBubblesShortcuts(int notificationType) {
        listHistoryNotification = HistoryRepository.getListHistoryNotification(context, notificationType);
        List<BubbleHistoryItemModel> historyItemModels = getBubbleHistoryItems(listHistoryNotification);
        bubblesFactory.updateShorcuts(historyItemModels);
    }

    private void updateBubblesBuilder(NotificationCompat.Builder builder, ApplinkNotificationModel applinkNotificationModel, int notificationType, int notificationId) {
        BubbleNotificationModel bubbleNotificationModel = getBubbleNotificationModel(applinkNotificationModel, notificationType, notificationId);
        bubblesFactory.setupBubble(builder, bubbleNotificationModel);
    }

    private List<BubbleHistoryItemModel> getBubbleHistoryItems(List<HistoryNotification> historyNotificationList) {
        List<BubbleHistoryItemModel> mappedResult = new ArrayList<>();
        for(HistoryNotification item: historyNotificationList) {
            String applink = item.getAppLink() == null ? "" : item.getAppLink();
            String senderName = item.getSenderName() == null ? "" : item.getSenderName();
            String avatarUrl = item.getAvatarUrl() == null ? "" : item.getAvatarUrl();
            BubbleHistoryItemModel historyItemModel = new BubbleHistoryItemModel(
                    getShortcutNotificationId(item.getNotificationId()),
                    applink,
                    senderName,
                    avatarUrl
            );
            mappedResult.add(historyItemModel);
        }
        return mappedResult;
    }

    private String getShortcutNotificationId(Integer notificationId) {
        int notificationIdNonNull = notificationId != null ? notificationId : 0;
        return String.valueOf(notificationIdNonNull);
    }

    private BubbleNotificationModel getBubbleNotificationModel(ApplinkNotificationModel applinkNotificationModel, int notificationType, int notificationId) {
        return new BubbleNotificationModel(
                notificationType,
                notificationId,
                getShortcutNotificationId(notificationId),
                applinkNotificationModel.getApplinks(),
                applinkNotificationModel.getFullName(),
                applinkNotificationModel.getThumbnail(),
                applinkNotificationModel.getSummary(),
                applinkNotificationModel.getSentTime()
        );
    }

}
