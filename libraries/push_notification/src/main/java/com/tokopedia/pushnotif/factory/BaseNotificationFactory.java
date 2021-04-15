package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.URLUtil;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.pushnotif.data.constant.Constant;
import com.tokopedia.pushnotif.R;
import com.tokopedia.pushnotif.data.repository.TransactionRepository;
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel;
import com.tokopedia.pushnotif.services.DismissBroadcastReceiver;
import com.tokopedia.pushnotif.util.NotificationChannelBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.tokopedia.pushnotif.util.NotificationRingtoneUtil.ringtoneUri;

/**
 * @author ricoharisin .
 */

public abstract class BaseNotificationFactory {

    protected Context context;

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
        if (GlobalConfig.isSellerApp()) {
            return com.tokopedia.resources.common.R.mipmap.ic_launcher_sellerapp_ramadhan;
        } else {
            return com.tokopedia.resources.common.R.mipmap.ic_launcher_customerapp;
        }
    }

    protected Bitmap getBitmapLargeIcon() {
        return BitmapFactory.decodeResource(context.getResources(), getDrawableLargeIcon());
    }

    protected Bitmap getBitmap(String url) {
        try {
            return Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .submit(getImageWidth(), getImageHeight())
                    .get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException | IllegalArgumentException e) {
            return BitmapFactory.decodeResource(context.getResources(), getDrawableLargeIcon());
        }
    }

    protected int getImageWidth() {
        return context.getResources().getDimensionPixelSize(R.dimen.notif_width);
    }

    protected int getImageHeight() {
        return context.getResources().getDimensionPixelSize(R.dimen.notif_height);
    }

    protected PendingIntent createPendingIntent(String appLinks, int notificationType, int notificationId) {
        PendingIntent resultPendingIntent;
        Intent intent = new Intent();

        // Notification will go through DeeplinkActivity and DeeplinkHandlerActivity
        // because we need tracking UTM for those notification applink
        if (URLUtil.isNetworkUrl(appLinks)) {
            intent.setClassName(context.getPackageName(), GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME);
        } else {
            intent.setClassName(context.getPackageName(), GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME);
        }

        intent.setData(Uri.parse(appLinks));
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.EXTRA_APPLINK_FROM_PUSH, true);
        bundle.putInt(Constant.EXTRA_NOTIFICATION_TYPE, notificationType);
        bundle.putInt(Constant.EXTRA_NOTIFICATION_ID, notificationId);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        } else {
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }

        return resultPendingIntent;
    }

    protected PendingIntent createDismissPendingIntent(int notificationType, int notificationId) {
        Intent intent = new Intent(context, DismissBroadcastReceiver.class);
        intent.putExtra(Constant.EXTRA_NOTIFICATION_TYPE, notificationType);
        intent.putExtra(Constant.EXTRA_NOTIFICATION_ID, notificationId);
        return PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, 0);
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

}
