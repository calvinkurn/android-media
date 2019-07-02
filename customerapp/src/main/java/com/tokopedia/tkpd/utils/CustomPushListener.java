package com.tokopedia.tkpd.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.moe.pushlibrary.utils.MoEHelperUtils;
import com.moengage.core.ConfigurationProvider;
import com.moengage.pushbase.push.PushMessageListener;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.tkpd.R;

public class CustomPushListener extends PushMessageListener {

    static final String EXTRA_NOTIFICATION_ID = "extra_delete_notification_id";
    static final String ACTION_DELETE_NOTIFY = "com.tokopedia.tkpd.utils.delete";
    private static final int NOTIFICATION_ID = 777;
    private static final int GRID_NOTIFICATION_ID = 778;


    private final static String KEY_ICON_NAME1 = "icon_name1";
    private final static String KEY_ICON_NAME2 = "icon_name2";
    private final static String KEY_ICON_NAME3 = "icon_name3";
    private final static String KEY_ICON_NAME4 = "icon_name4";
    private final static String KEY_DEEPLINK1 = "deeplink1";
    private final static String KEY_DEEPLINK2 = "deeplink2";
    private final static String KEY_DEEPLINK3 = "deeplink3";
    private final static String KEY_DEEPLINK4 = "deeplink4";
    private final static String KEY_DEEPLINK5 = "deeplink5";
    private final static String KEY_DEEPLINK6 = "deeplink6";
    private final static String KEY_ICON_URL1 = "icon_url1";
    private final static String KEY_ICON_URL2 = "icon_url2";
    private final static String KEY_ICON_URL3 = "icon_url3";
    private final static String KEY_ICON_URL4 = "icon_url4";
    private final static String KEY_ICON_URL5 = "icon_url5";
    private final static String KEY_ICON_URL6 = "icon_url6";

    final static String ACTION_GRID_CLICK = "action_grid_click";
    final static String EXTRA_DEEP_LINK = "extra_deep_link";


    static final String EVENT_PERSISTENT_CLICK_NAME = "Click_Action_Persistent";
    static final String ACTION_PERSISTENT_CLICK = "extra_action_persistent_click";
    static final String EXTRA_PERSISTENT_ICON_NAME = "icon_name";
    static final String EXTRA_PERSISTENT_ICON_URL = "icon_url";
    static final String EXTRA_PERSISTENT_DEEPLINK = "deeplink";
    static final String EXTRA_CAMPAIGN_ID = "campaign_id";

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
        final String KEY_IS_PERSISTENT = "is_persistent";
        final String KEY_IS_GRID = "is_grid";
        final String PERSISTENT = "1";
        final String GRID = "1";

        NotificationCompat.Builder builder = super.onCreateNotification(context, extras, provider);
        if (PERSISTENT.equalsIgnoreCase(extras.getString(KEY_IS_PERSISTENT))) {
            provider.updateNotificationId(NOTIFICATION_ID);
            createPersistentNotification(context, extras, builder);
        } else if (GRID.equalsIgnoreCase(extras.getString(KEY_IS_GRID))) {
            provider.updateNotificationId(GRID_NOTIFICATION_ID);
            createGridNotification(context, extras, builder);
        }
        return builder;
    }


    private void createPersistentNotification(Context context, Bundle extras, NotificationCompat.Builder builder) {
        long when = System.currentTimeMillis();

        String campaign_id = "";
        String KEY_CAMPAIGN_ID = "gcm_campaign_id";
        if (extras.containsKey(KEY_CAMPAIGN_ID)) {
            campaign_id = extras.getString(KEY_CAMPAIGN_ID);
        }

        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.persistent_notification_layout);

        String[] titles = new String[]{
                extras.getString(KEY_ICON_NAME1), extras.getString(KEY_ICON_NAME2),
                extras.getString(KEY_ICON_NAME3), extras.getString(KEY_ICON_NAME4)
        };

        String[] deepLinks = new String[]{
                extras.getString(KEY_DEEPLINK1), extras.getString(KEY_DEEPLINK2),
                extras.getString(KEY_DEEPLINK3), extras.getString(KEY_DEEPLINK4)
        };
        String[] iconUrls = new String[]{
                extras.getString(KEY_ICON_URL1), extras.getString(KEY_ICON_URL2),
                extras.getString(KEY_ICON_URL3), extras.getString(KEY_ICON_URL4)
        };

        Integer[] titleResIds = new Integer[]{
                R.id.title1, R.id.title2, R.id.title3, R.id.title4
        };
        Integer[] iconResIds = new Integer[]{
                R.id.image_icon1, R.id.image_icon2, R.id.image_icon3, R.id.image_icon4
        };

        Integer[] containerResIds = new Integer[]{
                R.id.lin_container_1, R.id.lin_container_2, R.id.lin_container_3, R.id.lin_container_4
        };

        for (int i = 0; i < 4; i++) {
            if (!TextUtils.isEmpty(titles[i]) && !TextUtils.isEmpty(deepLinks[i]) && !TextUtils.isEmpty(iconUrls[i])) {
                remoteView.setTextViewText(titleResIds[i], titles[i]);
                remoteView.setImageViewBitmap(iconResIds[i], MoEHelperUtils.downloadImageBitmap(iconUrls[i]));

                PendingIntent pIntent = getPersistentClickPendingIntent(
                        context, iconUrls[i], titles[i], deepLinks[i], campaign_id, 100 + i);

                remoteView.setOnClickPendingIntent(containerResIds[i], pIntent);
            }
        }

        Intent deleteIntent = new Intent(context, NotificationBroadcast.class);
        deleteIntent.setAction(ACTION_DELETE_NOTIFY);
        deleteIntent.putExtra(EXTRA_NOTIFICATION_ID, NOTIFICATION_ID);
        PendingIntent deletePendingIntent = getPendingIntent(context, NOTIFICATION_ID, deleteIntent);
        remoteView.setOnClickPendingIntent(R.id.image_icon5, deletePendingIntent);

        Intent notificationIntent = new Intent(context, MainParentActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 104, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.image_icon6, contentIntent);
        builder.setSmallIcon(R.drawable.ic_stat_notify_white)
                .setCustomContentView(remoteView)
                .setCustomBigContentView(remoteView)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setWhen(when);
    }

    /**
     * it will create grid notification (2,3,6) images which will have {@link PendingIntent}
     * setOnClickPendingIntent
     *
     * @param context This context is used to create intent, to get packageName
     * @param extras  This bundle contains the payload for Grid Type Notification
     * @param builder This builder object is provided by MoEngage and here we are setting contentView
     */
    private void createGridNotification(Context context, Bundle extras, NotificationCompat.Builder builder) {
        long when = System.currentTimeMillis();

        RemoteViews expandRemoteView = new RemoteViews(context.getPackageName(),
                R.layout.grid_notificaiton_layout);
        setCollapseData(context, expandRemoteView, extras);

        String deeplink1 = extras.getString(KEY_DEEPLINK1);
        String deeplink2 = extras.getString(KEY_DEEPLINK2);
        String deeplink3 = extras.getString(KEY_DEEPLINK3);
        String deeplink4 = extras.getString(KEY_DEEPLINK4);
        String deeplink5 = extras.getString(KEY_DEEPLINK5);
        String deeplink6 = extras.getString(KEY_DEEPLINK6);
        String url1 = extras.getString(KEY_ICON_URL1);
        String url2 = extras.getString(KEY_ICON_URL2);
        String url3 = extras.getString(KEY_ICON_URL3);
        String url4 = extras.getString(KEY_ICON_URL4);
        String url5 = extras.getString(KEY_ICON_URL5);
        String url6 = extras.getString(KEY_ICON_URL6);

        createGrid(context, expandRemoteView, R.id.iv_gridOne, url1, deeplink1, 201);
        createGrid(context, expandRemoteView, R.id.iv_gridTwo, url2, deeplink2, 202);
        createGrid(context, expandRemoteView, R.id.iv_gridThree, url3, deeplink3, 203);

        if (null != url4 && null != url5 && null != url6) {
            createGrid(context, expandRemoteView, R.id.iv_gridFour, url4, deeplink4, 204);
            createGrid(context, expandRemoteView, R.id.iv_gridFive, url5, deeplink5, 205);
            createGrid(context, expandRemoteView, R.id.iv_gridSix, url6, deeplink6, 206);
            expandRemoteView.setViewVisibility(R.id.ll_bottomGridParent, View.VISIBLE);
        }

        RemoteViews collapsedView = new RemoteViews(context.getApplicationContext().getPackageName(),
                R.layout.collapsed_notification_layout);
        setCollapseData(context, collapsedView, extras);

        builder.setSmallIcon(R.drawable.ic_stat_notify_white)
                .setLargeIcon(null)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandRemoteView)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setOngoing(false)
                .setWhen(when);
    }

    private void setCollapseData(Context context, RemoteViews remoteView, Bundle extras) {
        Intent notificationIntent = new Intent(context, NotificationBroadcast.class);
        notificationIntent.setAction(ACTION_GRID_CLICK);

        String deepLink = extras.getString("gcm_webUrl");
        if (null != deepLink) {
            notificationIntent.putExtra(EXTRA_DEEP_LINK, deepLink);
        } else {
            notificationIntent.putExtra(EXTRA_DEEP_LINK, "tokopedia://home");
        }

        notificationIntent.putExtra(EXTRA_NOTIFICATION_ID, GRID_NOTIFICATION_ID);
        PendingIntent contentIntent = getPendingIntent(context, 200, notificationIntent);
        remoteView.setTextViewText(R.id.tv_collapse_title, extras.getString("gcm_title"));
        remoteView.setTextViewText(R.id.tv_collapsed_message, extras.getString("gcm_alert"));
        remoteView.setOnClickPendingIntent(R.id.collapseMainView, contentIntent);
    }


    private PendingIntent getPersistentClickPendingIntent(Context context, String iconUrl,
                                                          String iconName, String deepLink,
                                                          String campaignId, int requestCode) {
        Intent intent = new Intent(context, NotificationBroadcast.class);
        intent.setAction(ACTION_PERSISTENT_CLICK);
        intent.putExtra(EXTRA_CAMPAIGN_ID, campaignId);
        intent.putExtra(EXTRA_PERSISTENT_ICON_URL, iconUrl);
        intent.putExtra(EXTRA_PERSISTENT_ICON_NAME, iconName);
        intent.putExtra(EXTRA_PERSISTENT_DEEPLINK, deepLink);
        return getPendingIntent(context, requestCode, intent);
    }

    private PendingIntent getPendingIntent(Context context, int requestCode, Intent intent) {
        return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private void createGrid(Context context, RemoteViews remoteView, int resId, String url, String deepLink, int requestCode) {
        if (null == url)
            return;
        remoteView.setViewVisibility(resId, View.VISIBLE);
        remoteView.setImageViewBitmap(resId, MoEHelperUtils.downloadImageBitmap(url));
        Intent intent = new Intent(context, NotificationBroadcast.class);
        intent.setAction(ACTION_GRID_CLICK);
        intent.putExtra(EXTRA_DEEP_LINK, deepLink);
        intent.putExtra(EXTRA_NOTIFICATION_ID, GRID_NOTIFICATION_ID);
        PendingIntent resultPendingIntent = getPendingIntent(context, requestCode, intent);
        remoteView.setOnClickPendingIntent(resId, resultPendingIntent);
    }

    @Override
    public void onNonMoEngageMessageReceived(Context context, Bundle extras) {
        super.onNonMoEngageMessageReceived(context, extras);

    }

    @Override
    public void onNotificationNotRequired(Context context, Bundle extras) {

    }

}
