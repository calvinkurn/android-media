package com.tokopedia.tkpd.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.moe.pushlibrary.utils.MoEHelperUtils;
import com.moengage.core.ConfigurationProvider;
import com.moengage.pushbase.push.PushMessageListener;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.tkpd.R;

public class CustomPushListener extends PushMessageListener {

    public static final String EXTRA_DELETE_NOTIFICATION_ID = "extra_delete_notification_id";
    public static final String DELETE_NOTIFY = "com.tokopedia.tkpd.utils.delete";
    public static final int NOTIFICATION_ID = 777;

    public static final String EVENT_PERSISTENT_CLICK_NAME = "Click_Action_Persistent";
    public static final String EXTRA_ACTION_PERSISTENT_CLICK = "extra_action_persistent_click";
    public static final String EXTRA_PERSISTENT_ICON_NAME = "icon_name";
    public static final String EXTRA_PERSISTENT_ICON_URL = "icon_url";
    public static final String EXTRA_PERSISTENT_DEEPLINK = "deeplink";
    public static final String EXTRA_CAMPAIGN_ID = "campaign_id";


    @Override
    protected void onPostNotificationReceived(Context context, Bundle extras) {
        super.onPostNotificationReceived(context, extras);

    }

    //if the app wants to modify the logic for showing notifications
    @Override
    public boolean isNotificationRequired(Context context, Bundle extras) {

        boolean result = super.isNotificationRequired(context,
                extras);//if SUPER is not not called then it will throw an exception
        return result;
    }

    // if the app wants to add additional properties to the NotificationCompat.Builder
    @Override
    public NotificationCompat.Builder onCreateNotification(Context context, Bundle extras,
                                                           ConfigurationProvider provider) {
        final String KEY_ICON_NAME1 = "icon_name1";
        final String KEY_ICON_NAME2 = "icon_name2";
        final String KEY_ICON_NAME3 = "icon_name3";
        final String KEY_ICON_NAME4 = "icon_name4";
        final String KEY_DEEPLINK1 = "deeplink1";
        final String KEY_DEEPLINK2 = "deeplink2";
        final String KEY_DEEPLINK3 = "deeplink3";
        final String KEY_DEEPLINK4 = "deeplink4";
        final String KEY_ICON_URL1 = "icon_url1";
        final String KEY_ICON_URL2 = "icon_url2";
        final String KEY_ICON_URL3 = "icon_url3";
        final String KEY_ICON_URL4 = "icon_url4";
        final String KEY_CAMPAIGN_ID = "gcm_campaign_id";
        final String KEY_IS_PERSISTENT = "is_persistent";
        final String PERSISTENT = "1";


        NotificationCompat.Builder builder = super.onCreateNotification(context, extras, provider);

        if (PERSISTENT.equalsIgnoreCase(extras.getString(KEY_IS_PERSISTENT))) {

            long when = System.currentTimeMillis();
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.persistent_notification_layout);
            String title1 = extras.getString(KEY_ICON_NAME1);
            String title2 = extras.getString(KEY_ICON_NAME2);
            String title3 = extras.getString(KEY_ICON_NAME3);
            String title4 = extras.getString(KEY_ICON_NAME4);
            String deeplink1 = extras.getString(KEY_DEEPLINK1);
            String deeplink2 = extras.getString(KEY_DEEPLINK2);
            String deeplink3 = extras.getString(KEY_DEEPLINK3);
            String deeplink4 = extras.getString(KEY_DEEPLINK4);
            String url1 = extras.getString(KEY_ICON_URL1);
            String url2 = extras.getString(KEY_ICON_URL2);
            String url3 = extras.getString(KEY_ICON_URL3);
            String url4 = extras.getString(KEY_ICON_URL4);
            String campaign_id = "";
            if (extras.containsKey(KEY_CAMPAIGN_ID)) {
                campaign_id = extras.getString(KEY_CAMPAIGN_ID);
            }

            if (!TextUtils.isEmpty(title1) && !TextUtils.isEmpty(deeplink1) && !TextUtils.isEmpty(url1)) {
                remoteView.setTextViewText(R.id.title1, title1);
                //Intent intent = RouteManager.getIntent(context, deeplink1);
                remoteView.setImageViewBitmap(R.id.image_icon1, MoEHelperUtils.downloadImageBitmap(url1));
                PendingIntent pIntent = getPersistentClickPendingIntent(
                        context, url1, title1, deeplink1, campaign_id, 101);
                remoteView.setOnClickPendingIntent(R.id.lin_container_1, pIntent);
            }

            if (!TextUtils.isEmpty(title2) && !TextUtils.isEmpty(deeplink2) && !TextUtils.isEmpty(url2)) {
                remoteView.setTextViewText(R.id.title2, title2);
                remoteView.setImageViewBitmap(R.id.image_icon2, MoEHelperUtils.downloadImageBitmap(url2));
                //Intent intent2 = RouteManager.getIntent(context, deeplink2);
                PendingIntent pIntent2 = getPersistentClickPendingIntent(
                        context, url2, title2, deeplink2, campaign_id, 102);
                remoteView.setOnClickPendingIntent(R.id.lin_container_2, pIntent2);
            }

            if (!TextUtils.isEmpty(title3) && !TextUtils.isEmpty(deeplink3) && !TextUtils.isEmpty(url3)) {
                remoteView.setTextViewText(R.id.title3, title3);

                remoteView.setImageViewBitmap(R.id.image_icon3, MoEHelperUtils.downloadImageBitmap(url3));
                //Intent intent3 = RouteManager.getIntent(context, deeplink3);
                PendingIntent pIntent3 = getPersistentClickPendingIntent(
                        context, url3, title3, deeplink3, campaign_id, 103);
                remoteView.setOnClickPendingIntent(R.id.lin_container_3, pIntent3);
            }

            if (!TextUtils.isEmpty(title4) && !TextUtils.isEmpty(deeplink4) && !TextUtils.isEmpty(url4)) {
                remoteView.setTextViewText(R.id.title4, title4);
                remoteView.setImageViewBitmap(R.id.image_icon4, MoEHelperUtils.downloadImageBitmap(url4));
                //Intent intent4 = RouteManager.getIntent(context, deeplink4);
                PendingIntent pIntent4 = getPersistentClickPendingIntent(
                        context, url4, title4, deeplink4, campaign_id, 104);
                remoteView.setOnClickPendingIntent(R.id.lin_container_4, pIntent4);
            }

            ConfigurationProvider configurationProvider = ConfigurationProvider.getInstance(context);
            configurationProvider.updateNotificationId(NOTIFICATION_ID);
            Intent deleteIntent = new Intent(context, NotificationBroadcast.class);
            deleteIntent.setAction(DELETE_NOTIFY);
            deleteIntent.putExtra(EXTRA_DELETE_NOTIFICATION_ID, NOTIFICATION_ID);
            PendingIntent pIntent5 = PendingIntent.getBroadcast(
                    context,
                    NOTIFICATION_ID,
                    deleteIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.image_icon5, pIntent5);

            Intent notificationIntent = new Intent(context, MainParentActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.image_icon6, contentIntent);
            builder.setSmallIcon(R.drawable.ic_stat_notify_white)
                    .setCustomContentView(remoteView)
                    .setCustomBigContentView(remoteView)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentIntent(contentIntent)
                    .setOngoing(true)
                    .setWhen(when);
        }

        return builder;
    }


    private PendingIntent getPersistentClickPendingIntent(Context context,
                                                          String iconUrl,
                                                          String iconName,
                                                          String deepLink,
                                                          String campaignId,
                                                          int requestCode) {
        PendingIntent resultPendingIntent;
        Intent intent = new Intent(context, NotificationBroadcast.class);
        intent.setAction(EXTRA_ACTION_PERSISTENT_CLICK);

        intent.putExtra(EXTRA_CAMPAIGN_ID, campaignId);
        intent.putExtra(EXTRA_PERSISTENT_ICON_URL, iconUrl);
        intent.putExtra(EXTRA_PERSISTENT_ICON_NAME, iconName);
        intent.putExtra(EXTRA_PERSISTENT_DEEPLINK, deepLink);


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
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
        return resultPendingIntent;
    }

    @Override
    public void onNonMoEngageMessageReceived(Context context, Bundle extras) {
        super.onNonMoEngageMessageReceived(context, extras);

    }

    @Override
    public void onNotificationNotRequired(Context context, Bundle extras) {

    }
}
