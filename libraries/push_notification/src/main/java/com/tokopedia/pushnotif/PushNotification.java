package com.tokopedia.pushnotif;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.pushnotif.data.constant.Constant;
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel;
import com.tokopedia.pushnotif.data.repository.TransactionRepository;
import com.tokopedia.pushnotif.factory.ChatNotificationFactory;
import com.tokopedia.pushnotif.factory.GeneralNotificationFactory;
import com.tokopedia.pushnotif.factory.ReviewNotificationFactory;
import com.tokopedia.pushnotif.factory.SummaryNotificationFactory;
import com.tokopedia.pushnotif.factory.TalkNotificationFactory;
import com.tokopedia.pushnotif.util.NotificationTracker;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import androidx.core.app.NotificationManagerCompat;
import timber.log.Timber;

import static com.tokopedia.pushnotif.domain.TrackPushNotificationUseCase.STATUS_DELIVERED;
import static com.tokopedia.pushnotif.domain.TrackPushNotificationUseCase.STATUS_DROPPED;

/**
 * @author ricoharisin .
 */

public class PushNotification {
    private static boolean isChatBotWindowOpen;

    public static void setIsChatBotWindowOpen(boolean isChatBotWindowOpen) {
        PushNotification.isChatBotWindowOpen = isChatBotWindowOpen;
    }

    public static void notify(Context context, Bundle data) {
        ApplinkNotificationModel applinkNotificationModel = ApplinkNotificationHelper.convertToApplinkModel(data);

        if (allowToShowNotification(context, applinkNotificationModel)) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            int notificationId = ApplinkNotificationHelper.generateNotifictionId(applinkNotificationModel.getApplinks());
            logEvent(context, applinkNotificationModel, data,
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
            }
        } else {
            UserSessionInterface userSession = new UserSession(context);
            String loginId = userSession.getUserId();
            logEvent(context, applinkNotificationModel, data,
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

    private static boolean allowToShowNotification(
            Context context,
            ApplinkNotificationModel applinkNotificationModel
    ) {
        UserSessionInterface userSession = new UserSession(context);
        String loginId = userSession.getUserId();
        Boolean sameUserId = applinkNotificationModel.getToUserId().equals(loginId);
        Boolean allowInLocalNotificationSetting = ApplinkNotificationHelper.checkLocalNotificationAppSettings(context, applinkNotificationModel.getTkpCode());
        Boolean isRenderable = TransactionRepository.isRenderable(context, applinkNotificationModel.getTransactionId());
        Boolean isTargetApp = ApplinkNotificationHelper.isTargetApp(applinkNotificationModel);
        return sameUserId && allowInLocalNotificationSetting && isTargetApp && isRenderable;
    }


    private static void logEvent(Context context, ApplinkNotificationModel model, Bundle data, String message) {
        try {
            String whiteListedUsers = FirebaseRemoteConfig.getInstance().getString(RemoteConfigKey.WHITELIST_USER_LOG_NOTIFICATION);
            String userId = model.getToUserId();
            if (!userId.isEmpty() && whiteListedUsers.contains(userId)) {
                executeCrashlyticLog(context, data,  message);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void executeCrashlyticLog(Context context, Bundle data, String message) {
        if (!BuildConfig.DEBUG) {
            String logMessage = generateLogMessage(context, data, message);
            FirebaseCrashlytics.getInstance().recordException(new Exception(logMessage));
            Timber.w(
                    "P2#LOG_PUSH_NOTIF#'%s';data='%s'",
                    "PushNotification::notify(Context context, Bundle data)",
                    logMessage
            );
        }
    }

    private static String generateLogMessage(Context context, Bundle data, String message) {
        StringBuilder logMessage = new StringBuilder(message + " \n");
        String fcmToken = getFcmTokenFromPref(context);
        addLogLine(logMessage, "fcmToken", fcmToken);
        addLogLine(logMessage, "isSellerApp", GlobalConfig.isSellerApp());
        for (String key : data.keySet()) {
            addLogLine(logMessage, key, data.get(key));
        }
        return logMessage.toString();
    }

    private static String getFcmTokenFromPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_fcm_token", "");
    }

    private static void addLogLine(StringBuilder stringBuilder, String key, Object value) {
        stringBuilder.append(key);
        stringBuilder.append(": ");
        stringBuilder.append(value);
        stringBuilder.append(", \n");
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
