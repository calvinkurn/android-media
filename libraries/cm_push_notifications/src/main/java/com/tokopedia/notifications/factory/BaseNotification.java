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
import android.media.AudioAttributes;
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
import com.tokopedia.notifications.receiver.CMBroadcastReceiver;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.app.Notification.BADGE_ICON_SMALL;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public abstract class BaseNotification {

    protected Context context;
    public BaseNotificationModel baseNotificationModel;

    public BaseNotification(Context context, BaseNotificationModel baseNotificationModel) {
        this.context = context;
        this.baseNotificationModel = baseNotificationModel;
    }

    public abstract Notification createNotification();

    protected NotificationCompat.Builder getBuilder() {
        NotificationCompat.Builder builder;
        if (baseNotificationModel.getChannelName() != null && !baseNotificationModel.getChannelName().isEmpty()) {
            builder = new NotificationCompat.Builder(context, baseNotificationModel.getChannelName());
        } else {
            builder = new NotificationCompat.Builder(context, CMConstant.NotificationGroup.CHANNEL_ID);
        }
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelGroup();
            createNotificationChannel();
            builder.setBadgeIconType(BADGE_ICON_SMALL);
            builder.setNumber(1);
        } else {
            setNotificationSound(builder);
        }
        if (!baseNotificationModel.getIcon().isEmpty()) {
            builder.setLargeIcon(getBitmap(baseNotificationModel.getIcon()));
        } else {
            builder.setLargeIcon(getBitmapLargeIcon());
        }
        return builder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        if (baseNotificationModel.getChannelName() != null && !baseNotificationModel.getChannelName().isEmpty()) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(baseNotificationModel.getChannelName(),
                    baseNotificationModel.getChannelName(), importance);
            channel.setDescription(CMConstant.NotificationGroup.CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (baseNotificationModel.getSoundFileName() != null && !baseNotificationModel.getSoundFileName().isEmpty()) {
                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                channel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" +
                        "/raw/" + baseNotificationModel.getSoundFileName()), att);
            }
            channel.setVibrationPattern(getVibratePattern());
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
            channel.setGroup(CMConstant.NotificationGroup.CHANNEL_GROUP_ID);
        } else {
            createDefaultChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createDefaultChannel() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        NotificationChannel channel = new NotificationChannel(CMConstant.NotificationGroup.CHANNEL_ID,
                CMConstant.NotificationGroup.CHANNEL,
                importance);
        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        channel.setSound(getRingtoneUri(), att);
        channel.setShowBadge(true);
        channel.setDescription(CMConstant.NotificationGroup.CHANNEL_DESCRIPTION);
        notificationManager.createNotificationChannel(channel);
        channel.setVibrationPattern(getVibratePattern());
        createChannelGroup();
        channel.setGroup(CMConstant.NotificationGroup.CHANNEL_GROUP_ID);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannelGroup() {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(CMConstant.NotificationGroup.CHANNEL_GROUP_ID,
                CMConstant.NotificationGroup.CHANNEL_GROUP_NAME);
        mNotificationManager.createNotificationChannelGroup(notificationChannelGroup);
    }

    private void setNotificationSound(NotificationCompat.Builder builder) {
        if (baseNotificationModel.getSoundFileName() != null && !baseNotificationModel.getSoundFileName().isEmpty()) {
            Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" +
                    "/raw/" + baseNotificationModel.getSoundFileName());
            builder.setSound(soundUri);
        }else {
            builder.setSound(getRingtoneUri());
        }
        builder.setVibrate(getVibratePattern());
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

    protected PendingIntent getButtonPendingIntent(ActionButton actionButton) {
        PendingIntent resultPendingIntent;
        Intent intent = new Intent(context, CMBroadcastReceiver.class);
        intent.setAction(CMConstant.ReceiverAction.ACTION_BUTTON);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.getNotificationId());
        intent.putExtra(CMConstant.ActionButtonExtra.ACTION_BUTTON_APP_LINK, actionButton.getAppLink());
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
                    baseNotificationModel.getNotificationId(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
        return resultPendingIntent;
    }

    protected PendingIntent createPendingIntent(String appLinks, int notificationType) {
        PendingIntent resultPendingIntent;
        Intent intent = RouteManager.getIntent(context, appLinks);
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
                    baseNotificationModel.getNotificationId(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }

        return resultPendingIntent;
    }

    protected PendingIntent createDismissPendingIntent(int notificationId) {
        Intent intent = new Intent(context, CMBroadcastReceiver.class);
        intent.setAction(CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, notificationId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            return PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
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
