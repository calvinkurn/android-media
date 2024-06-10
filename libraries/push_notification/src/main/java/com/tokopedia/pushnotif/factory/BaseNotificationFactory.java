package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.media.loader.JvmMediaLoader;
import com.tokopedia.media.loader.data.Resize;
import com.tokopedia.pushnotif.data.constant.Constant;
import com.tokopedia.pushnotif.R;
import com.tokopedia.pushnotif.data.repository.TransactionRepository;
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel;
import com.tokopedia.pushnotif.domain.TrackPushNotificationUseCase;
import com.tokopedia.pushnotif.receiver.NotifierReceiverActivity;
import com.tokopedia.pushnotif.services.ClickedBroadcastReceiver;
import com.tokopedia.pushnotif.services.DismissBroadcastReceiver;
import com.tokopedia.pushnotif.util.NotificationChannelBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.tokopedia.pushnotif.util.NotificationRingtoneUtil.ringtoneUri;

import androidx.core.app.NotificationCompat;

/**
 * @author ricoharisin .
 */

public abstract class BaseNotificationFactory {

    protected Context context;

    protected NotificationCompat.InboxStyle inboxStyle;

    public BaseNotificationFactory(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    public abstract Notification createNotification(
            ApplinkNotificationModel applinkNotificationModel,
            int notificationType,
            int notificationId
    );

    void storeToTransaction(
            Context context,
            int notificationType,
            int notificationId,
            ApplinkNotificationModel element
    ) {
        TransactionRepository.insert(
                context,
                element,
                notificationType,
                notificationId
        );
    }

    protected String generateGroupKey(String appLink) {
        if (appLink.contains("talk")) {
            return Constant.NotificationGroup.TALK;
        } else if (appLink.contains("chat")) {
            return Constant.NotificationGroup.TOPCHAT;
        } else if (appLink.contains("buyer")) {
            return Constant.NotificationGroup.TRANSACTION;
        } else if (appLink.contains("seller")) {
            return Constant.NotificationGroup.NEW_ORDER;
        } else if (appLink.contains("resolution")) {
            return Constant.NotificationGroup.RESOLUTION;
        } else if (appLink.contains("review")) {
            return Constant.NotificationGroup.REVIEW;
        } else {
            return Constant.NotificationGroup.GENERAL;
        }
    }

    protected int getDrawableIcon() {
        if (GlobalConfig.isSellerApp()) {
            return com.tokopedia.notification.common.R.mipmap.ic_statusbar_notif_seller;
        } else {
            return R.mipmap.ic_statusbar_notif_customer;
        }
    }

    protected int getDrawableLargeIcon() {
        return GlobalConfig.LAUNCHER_ICON_RES_ID;
    }

    protected Bitmap getBitmapLargeIcon() {
        return BitmapFactory.decodeResource(context.getResources(), getDrawableLargeIcon());
    }

    protected Bitmap getBitmap(String url) {
        try {
            return JvmMediaLoader.getBitmapImageUrl(context, url, 3_000, properties -> {
                properties.overrideSize(new Resize(getImageWidth(), getImageHeight()));
                return null;
            });
        } catch (Exception e) {
            return BitmapFactory.decodeResource(context.getResources(), getDrawableLargeIcon());
        }
    }

    protected int getImageWidth() {
        return context.getResources().getDimensionPixelSize(R.dimen.notif_width);
    }

    protected int getImageHeight() {
        return context.getResources().getDimensionPixelSize(R.dimen.notif_height);
    }

    protected PendingIntent createPendingIntent(String appLinks, int notificationType, int notificationId, ApplinkNotificationModel applinkNotificationModel) {

        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            intent = new Intent(context, NotifierReceiverActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent = new Intent(context, ClickedBroadcastReceiver.class);
            intent.setAction(Constant.NotificationReceiver.ACTION_ON_NOTIFICATION_CLICK);
        }

        intent.putExtra(Constant.EXTRA_NOTIFICATION_TYPE, notificationType);
        intent.putExtra(Constant.EXTRA_NOTIFICATION_ID, notificationId);
        intent.putExtra(Constant.EXTRA_APPLINK_VALUE, appLinks);
        intent.putExtra(TrackPushNotificationUseCase.PARAM_TRANSACTION_ID, applinkNotificationModel.getTransactionId());
        intent.putExtra(TrackPushNotificationUseCase.PARAM_RECIPIENT_ID, applinkNotificationModel.getToUserId());

        return getPendingIntent(intent, notificationId);

    }

    protected PendingIntent createDismissPendingIntent(int notificationType, int notificationId, ApplinkNotificationModel applinkNotificationModel) {
        Intent intent = new Intent(context.getApplicationContext(), DismissBroadcastReceiver.class);
        intent.setAction(Constant.NotificationReceiver.ACTION_ON_NOTIFICATION_DISMISS);

        intent.putExtra(Constant.EXTRA_NOTIFICATION_TYPE, notificationType);
        intent.putExtra(Constant.EXTRA_NOTIFICATION_ID, notificationId);
        intent.putExtra(TrackPushNotificationUseCase.PARAM_TRANSACTION_ID, applinkNotificationModel.getTransactionId());
        intent.putExtra(TrackPushNotificationUseCase.PARAM_RECIPIENT_ID, applinkNotificationModel.getToUserId());

        return getPendingIntent(intent, notificationId);
    }

    protected PendingIntent getPendingIntent(Intent intent, int notificationId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && intent.getComponent().getClassName().contains(
                Constant.NotificationReceiver.ACTIVITY_NOTIFIER_RECEIVER)
        ) {
            return PendingIntent.getActivity(
                    context.getApplicationContext(),
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !intent.getComponent().getClassName().contains(
                Constant.NotificationReceiver.ACTIVITY_NOTIFIER_RECEIVER)
        ) {
            return PendingIntent.getBroadcast(
                    context.getApplicationContext(),
                    notificationId,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );
        } else {
            return PendingIntent.getBroadcast(
                    context.getApplicationContext(),
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
    }

    protected Boolean isAllowBell() {
        if (isRevert()) {
            return checkCacheAllowBell();
        } else {
            return true;
        }
    }

    protected Boolean isRevert() {
        return false;
    }

    protected Boolean checkCacheAllowBell() {
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

    protected Boolean isAllowVibrate() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(Constant.Settings.NOTIFICATION_VIBRATE, false);
    }

    protected long[] getVibratePattern() {
        /*
        * If you look carefully the `longArrayToString()` method on NotificationCompat, you can
        * identify that the method can leads for ArrayOutOfBoundException if we are sending an array
        * with a zero size at line 7, when this happens the system throws DeadSystemException and
        * restart the phone.
        *
        * @solution:
        * some of device isn't support vibration with {500,500}, to fix DeadSystemException,
        * we can throw with try-catch and make `null` as silent/remove vibrate to unsupported
        * {500,500} vibration pattern.
        *
        * #source:
        * https://medium.com/p/ca122fa4d9cb
        * */
        try {
            return new long[]{500, 500};
        } catch (Exception e) {
            return null;
        }
    }

    protected Uri getRingtoneUri() {
        return ringtoneUri(context);
    }

    protected void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelBuilder.create(
                    context,
                    getRingtoneUri(),
                    getVibratePattern()
            );
        }
    }

    public void createNotificationInboxStyle() {
        if (inboxStyle == null) {
            inboxStyle = new NotificationCompat.InboxStyle();
        }
    }

    protected static String generateSummaryText(int notificationType) {
        switch (notificationType) {
            case Constant.NotificationId.TALK:
                return "Tokopedia - Diskusi";
            case Constant.NotificationId.CHAT:
                return "Tokopedia - Chat";
            default:
                return "Tokopedia - Notifikasi";
        }
    }

}
