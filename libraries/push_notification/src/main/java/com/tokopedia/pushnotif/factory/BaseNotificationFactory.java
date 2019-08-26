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

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.DismissBroadcastReceiver;
import com.tokopedia.pushnotif.R;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;

import java.util.concurrent.TimeUnit;

/**
 * @author ricoharisin .
 */

public abstract class BaseNotificationFactory {

    protected Context context;

    public BaseNotificationFactory(Context context) {
        this.context = context;
    }

    public abstract Notification createNotification(ApplinkNotificationModel applinkNotificationModel, int notifcationType, int notificationId);

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
        } else {
            return Constant.NotificationGroup.GENERAL;
        }
    }

    protected int getDrawableIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.ic_status_bar_notif_sellerapp;
        else
            return R.drawable.ic_status_bar_notif_customerapp;
    }

    protected int getDrawableLargeIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.ic_big_notif_sellerapp;
        else
            return R.mipmap.ic_launcher;
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
        } catch ( Exception e ) {
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
        Intent intent = RouteManager.getIntent(context, appLinks);
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
        return new long[]{500, 500};
    }

    protected Uri getRingtoneUri() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }
}
