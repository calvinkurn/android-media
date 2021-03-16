package com.tokopedia.pushnotif;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.notification.common.PushNotificationApi;
import com.tokopedia.notification.common.utils.NotificationTargetPriorities;
import com.tokopedia.notification.common.utils.NotificationValidationManager;
import com.tokopedia.pushnotif.data.constant.Constant;
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel;
import com.tokopedia.pushnotif.data.repository.TransactionRepository;
import com.tokopedia.pushnotif.factory.ChatNotificationFactory;
import com.tokopedia.pushnotif.factory.GeneralNotificationFactory;
import com.tokopedia.pushnotif.factory.ReviewNotificationFactory;
import com.tokopedia.pushnotif.factory.SummaryNotificationFactory;
import com.tokopedia.pushnotif.factory.TalkNotificationFactory;
import com.tokopedia.pushnotif.util.NotificationTracker;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import timber.log.Timber;

import static com.tokopedia.pushnotif.domain.TrackPushNotificationUseCase.STATUS_DELIVERED;
import static com.tokopedia.pushnotif.domain.TrackPushNotificationUseCase.STATUS_DROPPED;
import static com.tokopedia.pushnotif.util.AdvanceTargetUtil.advanceTargetNotification;

/**
 * @author ricoharisin .
 */

public class PushNotification {
    private static boolean isChatBotWindowOpen;
    private static Bundle aidlApiBundle;

    public static void init(Context context) {
        UserSessionInterface userSession = new UserSession(context);
        if (userSession.isLoggedIn()) {
            PushNotificationApi.bindService(
                    context,
                    (s, bundle) -> {
                        onAidlReceive(s, bundle);
                        return null;
                    },
                    () -> {
                        onAidlError();
                        return null;
                    });
        }
    }

    private static void onAidlReceive(String tag, Bundle data) {
        PushNotification.aidlApiBundle = data;
    }

    private static void onAidlError() {}

    public static void notify(Context context, Bundle data) {
        ApplinkNotificationModel applinkNotificationModel = ApplinkNotificationHelper.convertToApplinkModel(data);
        Bundle aidlBundle = PushNotification.aidlApiBundle;

        if (aidlBundle != null) {
            NotificationTargetPriorities targeting = advanceTargetNotification(applinkNotificationModel);
            NotificationValidationManager validation = new NotificationValidationManager(context, targeting);
            validation.validate(aidlBundle, () -> renderPushNotification(context, data, applinkNotificationModel));
        } else {
            renderPushNotification(context, data, applinkNotificationModel);
        }
    }

    private static void renderPushNotification(
            Context context,
            Bundle data,
            ApplinkNotificationModel applinkNotificationModel
    ) {
        if (isAllowToRender(context, applinkNotificationModel)) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            int notificationId = ApplinkNotificationHelper.generateNotifictionId(applinkNotificationModel.getApplinks());
            PushNotificationAnalytics.logEvent(context, applinkNotificationModel, data,
                    "ApplinkNotificationHelper.allowToShow == true"
                            + "; id " + notificationId
                            + "; v " + Build.VERSION.SDK_INT
                            + "; en " + isNotificationEnabled(context)
                            + "; bl " + isAllowBell(context));

            if (notificationId == Constant.NotificationId.TALK) {
                notifyTalk(context, applinkNotificationModel, notificationId, notificationManagerCompat);
            } else if (notificationId == Constant.NotificationId.CHAT) {
                notifyChat(context, applinkNotificationModel, notificationId, notificationManagerCompat);
            } else if (notificationId == Constant.NotificationId.GROUPCHAT) {
                notifyGroupChat(context, applinkNotificationModel, notificationId, notificationManagerCompat);
            } else if (notificationId == Constant.NotificationId.CHAT_BOT) {
                if (!isChatBotWindowOpen)
                    notifyChatbot(context, applinkNotificationModel, notificationId, notificationManagerCompat);
            } else if (notificationId == Constant.NotificationId.REVIEW) {
                notifyReview(context, applinkNotificationModel, notificationId, notificationManagerCompat);
            } else {
                notifyGeneral(context, applinkNotificationModel, notificationId, notificationManagerCompat);
            }
            if (isNotificationEnabled(context)) {
                NotificationTracker
                        .getInstance(context)
                        .trackDeliveredNotification(applinkNotificationModel, STATUS_DELIVERED);
            } else {
                NotificationTracker
                        .getInstance(context)
                        .trackDeliveredNotification(applinkNotificationModel, STATUS_DROPPED);
            }
            fetchSellerAppWidgetData(context, notificationId);
        } else {
            UserSessionInterface userSession = new UserSession(context);
            String loginId = userSession.getUserId();
            PushNotificationAnalytics.logEventFromAll(context, data,
                    "ApplinkNotificationHelper.allowToShow == false"
                            + "; sam " + applinkNotificationModel.getToUserId().equals(loginId)
                            + "; loc " + ApplinkNotificationHelper.checkLocalNotificationAppSettings(context, applinkNotificationModel.getTkpCode())
                            + "; app " + ApplinkNotificationHelper.isTargetApp(applinkNotificationModel)
                            + "; uid " + loginId
            );
            NotificationTracker
                    .getInstance(context)
                    .trackDeliveredNotification(applinkNotificationModel, STATUS_DROPPED);
        }
    }

    public static void setIsChatBotWindowOpen(boolean isChatBotWindowOpen) {
        PushNotification.isChatBotWindowOpen = isChatBotWindowOpen;
    }

    private static void fetchSellerAppWidgetData(Context context, int notificationId) {
        if (!GlobalConfig.isSellerApp()) return;

        if (notificationId == Constant.NotificationId.CHAT) {
            sendBroadcast(context, Constant.IntentFilter.GET_CHAT_SELLER_APP_WIDGET_DATA);
        } else if (notificationId == Constant.NotificationId.SELLER) {
            sendBroadcast(context, Constant.IntentFilter.GET_ORDER_SELLER_APP_WIDGET_DATA);
        }
    }

    private static void sendBroadcast(Context context, String actionName) {
        try {
            Intent intent = new Intent();
            intent.setAction(actionName);
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent);
        } catch (Exception e) {
            Timber.i(e);
        }
    }

    private static boolean isAllowToRender(Context context, ApplinkNotificationModel applinkNotificationModel) {
        UserSessionInterface userSession = new UserSession(context);
        String loginId = userSession.getUserId();
        boolean sameUserId = applinkNotificationModel.getToUserId().equals(loginId);
        boolean allowInLocalNotificationSetting = ApplinkNotificationHelper.checkLocalNotificationAppSettings(context, applinkNotificationModel.getTkpCode());
        boolean isRenderable = TransactionRepository.isRenderable(context, applinkNotificationModel.getTransactionId());
        Boolean isTargetApp = ApplinkNotificationHelper.isTargetApp(applinkNotificationModel);

        return sameUserId && allowInLocalNotificationSetting && isTargetApp && isRenderable;
    }

    private static void notifyChatbot(Context context, ApplinkNotificationModel applinkNotificationModel, int notificationId, NotificationManagerCompat notificationManagerCompat) {
        Notification notifChat = new ChatNotificationFactory(context)
                .createNotification(applinkNotificationModel, notificationId, notificationId);

        notificationManagerCompat.notify(notificationId, notifChat);
    }

    private static void notifyTalk(Context context, ApplinkNotificationModel applinkNotificationModel,
                                   int notificationType, NotificationManagerCompat notificationManagerCompat) {

        int notificationId = ApplinkNotificationHelper.getNotificationId(applinkNotificationModel.getApplinks());

        Notification notifTalk = new TalkNotificationFactory(context)
                .createNotification(applinkNotificationModel, notificationType, notificationId);

        SummaryNotificationFactory summaryNotificationFactory = new SummaryNotificationFactory(context);
        Notification notifSummary = summaryNotificationFactory
                .createNotification(applinkNotificationModel, notificationType, notificationType);

        if (ApplinkNotificationHelper.allowGroup())
            notificationManagerCompat.notify(notificationId, notifTalk);

        if ((ApplinkNotificationHelper.allowGroup() && summaryNotificationFactory.getTotalSummary() > 1)
                || (!ApplinkNotificationHelper.allowGroup() && summaryNotificationFactory.getTotalSummary() >= 1)) {
            notificationManagerCompat.notify(notificationType, notifSummary);
        }

    }

    private static void notifyChat(Context context, ApplinkNotificationModel applinkNotificationModel,
                                   int notificationType, NotificationManagerCompat notificationManagerCompat) {

        int notificationId = ApplinkNotificationHelper.getNotificationId(applinkNotificationModel.getApplinks());

        Notification notifChat = new ChatNotificationFactory(context)
                .createNotification(applinkNotificationModel, notificationType, notificationId);

        SummaryNotificationFactory summaryNotificationFactory = new SummaryNotificationFactory(context);
        Notification notifSummary = summaryNotificationFactory
                .createNotification(applinkNotificationModel, notificationType, notificationType);

        if (ApplinkNotificationHelper.allowGroup())
            notificationManagerCompat.notify(notificationId, notifChat);

        if ((ApplinkNotificationHelper.allowGroup() && summaryNotificationFactory.getTotalSummary() > 1)
                || (!ApplinkNotificationHelper.allowGroup() && summaryNotificationFactory.getTotalSummary() >= 1)) {
            notificationManagerCompat.notify(notificationType, notifSummary);
        }

    }

    private static void notifyReview(Context context, ApplinkNotificationModel applinkNotificationModel,
                                     int notificationType, NotificationManagerCompat notificationManagerCompat) {
        new ReviewNotificationFactory(context).createNotification(applinkNotificationModel,notificationType,notificationType);
    }

    private static void notifyGroupChat(Context context, ApplinkNotificationModel applinkNotificationModel,
                                        int notificationType, NotificationManagerCompat notificationManagerCompat) {

        Notification notifChat = new GeneralNotificationFactory(context)
                .createNotification(applinkNotificationModel, notificationType, notificationType);

        notificationManagerCompat.notify(notificationType, notifChat);

    }

    private static void notifyGeneral(Context context, ApplinkNotificationModel applinkNotificationModel,
                                      int notificationType, NotificationManagerCompat notificationManagerCompat) {
        Notification notifChat = new GeneralNotificationFactory(context)
                .createNotification(applinkNotificationModel, notificationType, notificationType);

        notificationManagerCompat.notify(notificationType, notifChat);

    }

    private static boolean isNotificationEnabled(Context context) {
        boolean isAllNotificationEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isAllNotificationEnabled) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = manager.getNotificationChannel(Constant.NotificationChannel.GENERAL);
            return channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
        } else {
            return isAllNotificationEnabled;
        }

    }

    private static Boolean isAllowBell(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, Constant.CACHE_DELAY);
        long prevTime = cache.getLong(Constant.PREV_TIME);
        long currTIme = System.currentTimeMillis();
        if (currTIme - prevTime > 15000) {
            cache.putLong(Constant.PREV_TIME, currTIme);
            cache.applyEditor();
            return true;
        }
        return false;
    }
}
