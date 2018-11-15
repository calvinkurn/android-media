package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.notifications.R;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.model.ActionButton;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.receiver.ActionButtonReceiver;
import com.tokopedia.notifications.receiver.DismissReceiver;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public abstract class BaseNotificationFactory {

    protected Context context;

    public BaseNotificationFactory(Context context) {
        this.context = context;
    }

    public abstract Notification createNotification(BaseNotificationModel baseNotificationModel, int notificationId);

    protected NotificationCompat.Builder getBuilder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context);
        }
        return new NotificationCompat.Builder(context,CMConstant.NotificationGroup.CHANNEL_ID);
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
            return Glide.with(context).load(url)
                    .asBitmap()
                    .into(getImageWidth(), getImageHeight())
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

    protected PendingIntent getActionButtonPendingIntent(ActionButton actionButton, int notificationId) {
        PendingIntent resultPendingIntent;
        Intent intent = new Intent(context, ActionButtonReceiver.class);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, notificationId);
        intent.putExtra(CMConstant.ActionButtonExtra.ACTION_BUTTON_APP_LINK, actionButton.getApplink());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        } else {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
        return resultPendingIntent;
    }

    protected PendingIntent createPendingIntent(String appLinks, int notificationType, int notificationId) {
        PendingIntent resultPendingIntent;
        Intent intent = RouteManager.getIntent(context, appLinks);
        intent.setData(Uri.parse(appLinks));
        Bundle bundle = new Bundle();
        //bundle.putBoolean(Constant.EXTRA_APPLINK_FROM_PUSH, true);
        //bundle.putInt(Constant.EXTRA_NOTIFICATION_TYPE, notificationType);
        //bundle.putInt(Constant.EXTRA_NOTIFICATION_ID, notificationId);
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

    protected PendingIntent createDismissPendingIntent(int notificationId) {
        Intent intent = new Intent(context, DismissReceiver.class);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, notificationId);
        return PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, 0);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void createNotificationChannel(Context context) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CMConstant.NotificationGroup.CHANNEL_ID,
                CMConstant.NotificationGroup.CHANNEL,
                importance);
        channel.setDescription(CMConstant.NotificationGroup.CHANNEL_DESCRIPTION);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        createChannelGroup(context, CMConstant.NotificationGroup.CHANNEL_GROUP_ID, CMConstant.NotificationGroup.CHANNEL_GROUP_ID);
        channel.setGroup(CMConstant.NotificationGroup.CHANNEL_GROUP_ID);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void createChannelGroup(Context context, String groupId, String groupName) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(groupId, groupName);
        mNotificationManager.createNotificationChannelGroup(notificationChannelGroup);
    }


    protected long[] getVibratePattern() {
        return new long[]{500, 500};
    }

    protected Uri getRingtoneUri() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }
}


//    protected Boolean isAllowBell() {
//        LocalCacheHandler cache = new LocalCacheHandler(context, Constant.CACHE_DELAY);
//        long prevTime = cache.getLong(Constant.PREV_TIME);
//        long currTIme = System.currentTimeMillis();
//        if (currTIme - prevTime > 15000) {
//            cache.putLong(Constant.PREV_TIME, currTIme);
//            cache.applyEditor();
//            return true;
//        }
//        return false;
//    }

//    protected Boolean isAllowVibrate() {
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        return settings.getBoolean(Constant.Settings.NOTIFICATION_VIBRATE, false);
//    }

