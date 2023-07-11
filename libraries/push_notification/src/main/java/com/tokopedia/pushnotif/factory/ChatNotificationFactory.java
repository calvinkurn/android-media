package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

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

    private Bitmap bubbleBitmap;

    private List<HistoryNotification> listHistoryNotification;

    public ChatNotificationFactory(Context context) {
        super(context);
        remoteConfig = new FirebaseRemoteConfigImpl(context);
        if (isEnableBubble()) {
            generateBubbleFactory(context);
        }
        createNotificationInboxStyle();
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

        builder.setStyle(inboxStyle);
        PendingIntent pendingContentIntent = createPendingIntent(applinkNotificationModel.getApplinks(), notificationType, notificationId, applinkNotificationModel);
        builder.setContentIntent(pendingContentIntent);
        builder.setDeleteIntent(createDismissPendingIntent(notificationType, notificationId, applinkNotificationModel));
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

        if (isEnableBubble()) {
            if (bubblesFactory == null) {
                generateBubbleFactory(context);
            }
            if (bubblesFactory != null) {
                setupBubble(builder, applinkNotificationModel, notificationType, notificationId);
            }
        }

        return builder.build();
    }

    public void setBubbleBitmaps(Bitmap bubbleBitmap) {
        this.bubbleBitmap = bubbleBitmap;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getBroadcast(context, notificationId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            return PendingIntent.getBroadcast(context, notificationId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
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

    private void generateBubbleFactory(Context context) {
        if (context != null) {
            bubblesFactory = new BubblesFactoryImpl(context);
        }
    }

    private void setupBubble(NotificationCompat.Builder builder, ApplinkNotificationModel applinkNotificationModel, int notificationType, int notificationId) {
        try {
            BubbleNotificationModel bubbleNotificationModel = getBubbleNotificationModel(applinkNotificationModel, notificationType, notificationId);

            updateBubblesShortcuts(notificationType, bubbleNotificationModel, applinkNotificationModel);
            updateBubblesBuilder(builder, bubbleNotificationModel);
        } catch (Exception ignored) { }
    }

    private void updateBubblesShortcuts(int notificationType, BubbleNotificationModel bubbleNotificationModel, ApplinkNotificationModel applinkNotificationModel) {
        listHistoryNotification = HistoryRepository.getListHistoryNotification(context, notificationType);
        List<BubbleHistoryItemModel> historyItemModels;
        if (applinkNotificationModel.getIsFromUser()) {
            historyItemModels = getBubbleHistorySingleItems(applinkNotificationModel);
        } else {
            historyItemModels = getBubbleHistoryItems(listHistoryNotification);
        }
        bubblesFactory.updateShorcuts(historyItemModels, bubbleNotificationModel, bubbleBitmap);
    }

    private void updateBubblesBuilder(NotificationCompat.Builder builder, BubbleNotificationModel bubbleNotificationModel) {
        bubblesFactory.setupBubble(builder, bubbleNotificationModel, bubbleBitmap);
    }

    private List<BubbleHistoryItemModel> getBubbleHistoryItems(List<HistoryNotification> historyNotificationList) {
        List<BubbleHistoryItemModel> mappedResult = new ArrayList<>();
        for(HistoryNotification item: historyNotificationList) {
            String applink = item.getAppLink() == null ? "" : item.getAppLink();
            String senderName = item.getSenderName() == null ? "" : item.getSenderName();
            String avatarUrl = item.getAvatarUrl() == null ? "" : item.getAvatarUrl();
            String shortcutId = getMessageId(item.getAppLink());
            BubbleHistoryItemModel historyItemModel = new BubbleHistoryItemModel(
                    shortcutId,
                    applink,
                    senderName,
                    avatarUrl
            );
            mappedResult.add(historyItemModel);
        }
        return mappedResult;
    }

    private List<BubbleHistoryItemModel> getBubbleHistorySingleItems(ApplinkNotificationModel applinkNotificationModel) {
        List<BubbleHistoryItemModel> mappedResult = new ArrayList<>();
        String applink = applinkNotificationModel.getApplinks() == null ? "" : applinkNotificationModel.getApplinks();
        String senderName = applinkNotificationModel.getFullName() == null ? "" : applinkNotificationModel.getFullName();
        String avatarUrl = applinkNotificationModel.getThumbnail() == null ? "" : applinkNotificationModel.getThumbnail();
        String shortcutId = getMessageId(applinkNotificationModel.getApplinks());
        BubbleHistoryItemModel historyItemModel = new BubbleHistoryItemModel(
                shortcutId,
                applink,
                senderName,
                avatarUrl
        );
        mappedResult.add(historyItemModel);

        return mappedResult;
    }

    private BubbleNotificationModel getBubbleNotificationModel(ApplinkNotificationModel applinkNotificationModel, int notificationType, int notificationId) {
        String shortcutId = getMessageId(applinkNotificationModel.getApplinks());
        return new BubbleNotificationModel(
                notificationType,
                notificationId,
                shortcutId,
                applinkNotificationModel.getSenderId(),
                applinkNotificationModel.getApplinks(),
                applinkNotificationModel.getFullName(),
                applinkNotificationModel.getThumbnail(),
                applinkNotificationModel.getSummary(),
                applinkNotificationModel.getSentTime(),
                applinkNotificationModel.getIsFromUser()
        );
    }

    private boolean isEnableBubble() {
        boolean isEnableBubble =
                GlobalConfig.isSellerApp() &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                getShouldEnableBubble();
        return isEnableBubble;
    }

    private boolean getShouldEnableBubble() {
        return true;
    }

}
