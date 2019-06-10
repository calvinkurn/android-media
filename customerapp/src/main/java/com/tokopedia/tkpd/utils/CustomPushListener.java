package com.tokopedia.tkpd.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.moe.pushlibrary.utils.MoEHelperUtils;
import com.moengage.core.ConfigurationProvider;
import com.moengage.pushbase.push.PushMessageListener;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.tkpd.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CustomPushListener extends PushMessageListener {

    static final String EXTRA_NOTIFICATION_ID = "extra_delete_notification_id";
    static final String DELETE_NOTIFY = "com.tokopedia.tkpd.utils.delete";
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
            String bundleData = extras.toString();
            writeStringAsFile(context, bundleData, "bundleData.txt");
            provider.updateNotificationId(GRID_NOTIFICATION_ID);
            createGridNotification(context, extras, builder);
        }
        return builder;
    }


    private void createPersistentNotification(Context context, Bundle extras, NotificationCompat.Builder builder) {
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

        if (!TextUtils.isEmpty(title1) && !TextUtils.isEmpty(deeplink1) && !TextUtils.isEmpty(url1)) {
            remoteView.setTextViewText(R.id.title1, title1);
            Intent intent = RouteManager.getIntent(context, deeplink1);
            remoteView.setImageViewBitmap(R.id.image_icon1, MoEHelperUtils.downloadImageBitmap(url1));
            PendingIntent pIntent = PendingIntent.getActivity(context, 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.lin_container_1, pIntent);
        }

        if (!TextUtils.isEmpty(title2) && !TextUtils.isEmpty(deeplink2) && !TextUtils.isEmpty(url2)) {
            remoteView.setTextViewText(R.id.title2, title2);
            remoteView.setImageViewBitmap(R.id.image_icon2, MoEHelperUtils.downloadImageBitmap(url2));
            Intent intent2 = RouteManager.getIntent(context, deeplink2);
            PendingIntent pIntent2 = PendingIntent.getActivity(context, 101, intent2,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.lin_container_2, pIntent2);
        }

        if (!TextUtils.isEmpty(title3) && !TextUtils.isEmpty(deeplink3) && !TextUtils.isEmpty(url3)) {
            remoteView.setTextViewText(R.id.title3, title3);

            remoteView.setImageViewBitmap(R.id.image_icon3, MoEHelperUtils.downloadImageBitmap(url3));
            Intent intent3 = RouteManager.getIntent(context, deeplink3);
            PendingIntent pIntent3 = PendingIntent.getActivity(context, 102, intent3,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.lin_container_3, pIntent3);
        }

        if (!TextUtils.isEmpty(title4) && !TextUtils.isEmpty(deeplink4) && !TextUtils.isEmpty(url4)) {
            remoteView.setTextViewText(R.id.title4, title4);
            remoteView.setImageViewBitmap(R.id.image_icon4, MoEHelperUtils.downloadImageBitmap(url4));
            Intent intent4 = RouteManager.getIntent(context, deeplink4);
            PendingIntent pIntent4 = PendingIntent.getActivity(context, 103, intent4,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.lin_container_4, pIntent4);
        }

        Intent deleteIntent = new Intent(context, NotificationBroadcast.class);
        deleteIntent.setAction(DELETE_NOTIFY);
        deleteIntent.putExtra(EXTRA_NOTIFICATION_ID, NOTIFICATION_ID);
        PendingIntent pIntent5 = PendingIntent.getBroadcast(
                context,
                NOTIFICATION_ID,
                deleteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.image_icon5, pIntent5);

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

        String deepLink = extras.getString("dl");
        if (null != deepLink) {
            notificationIntent.putExtra(EXTRA_DEEP_LINK, deepLink);
        } else {
            notificationIntent.putExtra(EXTRA_DEEP_LINK, "tokopedia://home");
        }

        notificationIntent.putExtra(EXTRA_NOTIFICATION_ID, GRID_NOTIFICATION_ID);
        PendingIntent contentIntent = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            contentIntent = PendingIntent.getBroadcast(
                    context,
                    200,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        } else {
            contentIntent = PendingIntent.getBroadcast(
                    context,
                    200,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }


        remoteView.setTextViewText(R.id.tv_collapse_title, extras.getString("gcm_title"));
        remoteView.setTextViewText(R.id.tv_collapsed_message, extras.getString("gcm_alert"));
        remoteView.setOnClickPendingIntent(R.id.collapseMainView, contentIntent);
    }

    private void createGrid(Context context, RemoteViews remoteView, int resId, String url, String deepLink, int requestCode) {
        if (null == url)
            return;
        PendingIntent resultPendingIntent;
        remoteView.setViewVisibility(resId, View.VISIBLE);
        remoteView.setImageViewBitmap(resId, MoEHelperUtils.downloadImageBitmap(url));
        Intent intent = new Intent(context, NotificationBroadcast.class);
        intent.setAction(ACTION_GRID_CLICK);
        intent.putExtra(EXTRA_DEEP_LINK, deepLink);
        intent.putExtra(EXTRA_NOTIFICATION_ID, GRID_NOTIFICATION_ID);

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

        remoteView.setOnClickPendingIntent(resId, resultPendingIntent);
    }

    @Override
    public void onNonMoEngageMessageReceived(Context context, Bundle extras) {
        super.onNonMoEngageMessageReceived(context, extras);

    }

    @Override
    public void onNotificationNotRequired(Context context, Bundle extras) {

    }

    public static void writeStringAsFile(Context context, final String fileContents, String fileName) {
        try {
            File data = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);
            File file = new File(data, "highscore.txt");
            FileWriter out = new FileWriter(file);
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
        }
    }

}
