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
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.notifications.R;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMNotificationCacheHandler;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.receiver.CMBroadcastReceiver;

import org.json.JSONObject;

import java.util.Iterator;
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
    private CMNotificationCacheHandler cacheHandler;

    private static final String CM_REQUEST_CODE = "cm_request_code";


    BaseNotification(Context context, BaseNotificationModel baseNotificationModel) {
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

        if (!TextUtils.isEmpty(baseNotificationModel.getSubText())) {
            builder.setSubText(baseNotificationModel.getSubText());
        }
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        if (baseNotificationModel.isUpdateExisting()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                silentChannel();
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannelGroup();
                createNotificationChannel();
                builder.setBadgeIconType(BADGE_ICON_SMALL);
                builder.setNumber(1);
            } else {
                setNotificationSound(builder);
            }
        }
        if (baseNotificationModel.getIcon().isEmpty()) {
            builder.setLargeIcon(getBitmapLargeIcon());
        } else {
            builder.setLargeIcon(getBitmap(baseNotificationModel.getIcon()));

        }
        return builder;
    }

    protected NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder builder;
        if (baseNotificationModel.getChannelName() != null && !baseNotificationModel.getChannelName().isEmpty()) {
            builder = new NotificationCompat.Builder(context, baseNotificationModel.getChannelName());
        } else {
            builder = new NotificationCompat.Builder(context, CMConstant.NotificationGroup.CHANNEL_ID);
        }
        if (!TextUtils.isEmpty(baseNotificationModel.getSubText())) {
            builder.setSubText(baseNotificationModel.getSubText());
        }
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setSmallIcon(getDrawableIcon());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelGroup();
            createNotificationChannel();
            builder.setBadgeIconType(BADGE_ICON_SMALL);
            builder.setNumber(1);
        } else {
            setNotificationSound(builder);
        }
        return builder;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void silentChannel() {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        NotificationChannel channel = new NotificationChannel("SILENT_01)",
                "App channel",
                importance);
        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        channel.setSound(null, null);
        // channel.setShowBadge(true);
        channel.setDescription(CMConstant.NotificationGroup.CHANNEL_DESCRIPTION);
        //channel.setGroup(CMConstant.NotificationGroup.CHANNEL_GROUP_ID);
        channel.setVibrationPattern(null);
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
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
            channel.setGroup(CMConstant.NotificationGroup.CHANNEL_GROUP_ID);
            notificationManager.createNotificationChannel(channel);
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
        channel.setGroup(CMConstant.NotificationGroup.CHANNEL_GROUP_ID);
        channel.setVibrationPattern(getVibratePattern());
        notificationManager.createNotificationChannel(channel);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannelGroup() {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(CMConstant.NotificationGroup.CHANNEL_GROUP_ID,
                CMConstant.NotificationGroup.CHANNEL_GROUP_NAME);
        mNotificationManager.createNotificationChannelGroup(notificationChannelGroup);
    }

    public void setNotificationSound(NotificationCompat.Builder builder) {
        if (baseNotificationModel.getSoundFileName() != null && !baseNotificationModel.getSoundFileName().isEmpty()) {
            Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" +
                    "/raw/" + baseNotificationModel.getSoundFileName());
            builder.setSound(soundUri);
        } else {
            builder.setSound(getRingtoneUri());
        }
        builder.setVibrate(getVibratePattern());
    }

    int getDrawableIcon() {
        if (GlobalConfig.isSellerApp())
            return R.drawable.ic_status_bar_notif_sellerapp;
        else
            return R.drawable.ic_status_bar_notif_customerapp;
    }

    private int getDrawableLargeIcon() {
        //TODO need to discuss on Seller App Icon--- 02-12-2018
        if (GlobalConfig.isSellerApp())
            return R.drawable.ic_big_notif_sellerapp;
        else
            return R.mipmap.ic_launcher;
    }

    protected Bitmap getBitmapLargeIcon() {
        return BitmapFactory.decodeResource(context.getResources(), getDrawableLargeIcon());
    }

    Bitmap getBitmap(String url) {
        try {
            return Glide.with(context).load(url)
                    .asBitmap()
                    .into(getImageWidth(), getImageHeight())
                    .get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException | IllegalArgumentException e) {
            return BitmapFactory.decodeResource(context.getResources(), getDrawableLargeIcon());
        }
    }

    private int getImageWidth() {
        return context.getResources().getDimensionPixelSize(R.dimen.notif_width);
    }

    private int getImageHeight() {
        return context.getResources().getDimensionPixelSize(R.dimen.notif_height);
    }

    PendingIntent createMainPendingIntent(BaseNotificationModel baseNotificationModel, int requestCode) {
        PendingIntent resultPendingIntent;
        Intent intent = new Intent(context, CMBroadcastReceiver.class);
        intent.setAction(CMConstant.ReceiverAction.ACTION_NOTIFICATION_CLICK);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.getNotificationId());
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_APP_LINK, baseNotificationModel.getAppLink());
        intent.putExtras(getBundle(baseNotificationModel));
        intent = getCouponCode(intent);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        } else {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    getRequestCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }

        return resultPendingIntent;
    }

    PendingIntent createDismissPendingIntent(int notificationId, int requestCode) {
        Intent intent = new Intent(context, CMBroadcastReceiver.class);
        intent.setAction(CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, notificationId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            return PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
    }

    private long[] getVibratePattern() {
        return new long[]{500, 500};
    }

    private Uri getRingtoneUri() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    protected int getRequestCode() {
        if (cacheHandler == null)
            cacheHandler = new CMNotificationCacheHandler(context);
        int requestCode = cacheHandler.getIntValue( CM_REQUEST_CODE);
        if (requestCode < 3000 || requestCode > 4000) {
            requestCode = 3000;
        }
        cacheHandler.saveIntValue(CM_REQUEST_CODE, requestCode + 1);
        return requestCode;
    }

    protected Bundle getBundle(BaseNotificationModel baseNotificationModel) {
        Bundle bundle = new Bundle();
        if (baseNotificationModel.getVideoPushModel() != null) {
            bundle = jsonToBundle(bundle, baseNotificationModel.getVideoPushModel());
        }
        if (baseNotificationModel.getCustomValues() != null) {
            bundle = jsonToBundle(bundle, baseNotificationModel.getCustomValues());
        }
        return bundle;
    }

    private Bundle jsonToBundle(Bundle bundle, JSONObject jsonObject) {
        try {
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = jsonObject.getString(key);
                bundle.putString(key, value);
            }
        } catch (Exception e) {

        }
        return bundle;
    }

    private Intent getCouponCode(Intent intent) {

        if (baseNotificationModel.getCustomValues() != null)
            intent.putExtra(CMConstant.CouponCodeExtra.COUPON_CODE, baseNotificationModel.getCustomValues().optString(CMConstant.CustomValuesKeys.COUPON_CODE));
        return intent;
    }

}