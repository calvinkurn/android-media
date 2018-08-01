package com.tokopedia.tkpd.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.moe.pushlibrary.utils.MoEHelperUtils;
import com.moengage.core.ConfigurationProvider;
import com.moengage.pushbase.push.PushMessageListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.ParentIndexHome;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class CustomPushListener extends PushMessageListener {

    @Override
    protected void onPostNotificationReceived(Context context, Bundle extras) {
        super.onPostNotificationReceived(context, extras);

    }

    //if the app wants to modify the logic for showing notifications
    @Override
    public boolean isNotificationRequired(Context context, Bundle extras) {

        boolean result = super.isNotificationRequired(context,
                extras);//if SUPER is not not called then it will throw an exception
        if (result) {
            //your logic to check whether notification is required or not.
            //return true or false based on your logic
        }
        return result;
    }

    // if the app wants to add additional properties to the NotificationCompat.Builder
    @Override
    public NotificationCompat.Builder onCreateNotification(Context context, Bundle extras,
                                                           ConfigurationProvider provider) {
        NotificationCompat.Builder builder = super.onCreateNotification(context, extras, provider);

        if ("1".equalsIgnoreCase(extras.getString("is_persistent"))) {
            long when = System.currentTimeMillis();
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.persistent_notification_layout);

            String title1 = extras.getString("name1");
            String title2 = extras.getString("name2");
            String title3 = extras.getString("name3");
            String title4 = extras.getString("name4");
            String deeplink1 = extras.getString("deeplink1");
            String deeplink2 = extras.getString("deeplink2");
            String deeplink3 = extras.getString("deeplink3");
            String deeplink4 = extras.getString("deeplink4");
            String url1 = extras.getString("icon1");
            String url2 = extras.getString("icon2");
            String url3 = extras.getString("icon3");
            String url4 = extras.getString("icon4");
            if (!TextUtils.isEmpty(title1) && !TextUtils.isEmpty(deeplink1) && !TextUtils.isEmpty(url1)) {
                remoteView.setTextViewText(R.id.title1, title1);
                Intent intent = RouteManager.getIntent(context, deeplink1);
                remoteView.setImageViewBitmap(R.id.image_icon1, MoEHelperUtils.downloadImageBitmap(url1));
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                remoteView.setOnClickPendingIntent(R.id.image_icon1, pIntent);
            }
            if (!TextUtils.isEmpty(title2) && !TextUtils.isEmpty(deeplink2) && !TextUtils.isEmpty(url2)) {
                remoteView.setTextViewText(R.id.title2, title2);
                remoteView.setImageViewBitmap(R.id.image_icon2, MoEHelperUtils.downloadImageBitmap(url2));
                Intent intent2 = RouteManager.getIntent(context, deeplink2);
                PendingIntent pIntent2 = PendingIntent.getActivity(context, 0, intent2,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                remoteView.setOnClickPendingIntent(R.id.image_icon2, pIntent2);
            }
            if (!TextUtils.isEmpty(title3) && !TextUtils.isEmpty(deeplink3) && !TextUtils.isEmpty(url3)) {
                remoteView.setTextViewText(R.id.title3, title3);

                remoteView.setImageViewBitmap(R.id.image_icon3, MoEHelperUtils.downloadImageBitmap(url3));
                Intent intent3 = RouteManager.getIntent(context, deeplink3);
                PendingIntent pIntent3 = PendingIntent.getActivity(context, 0, intent3,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                remoteView.setOnClickPendingIntent(R.id.image_icon3, pIntent3);
            }
            if (!TextUtils.isEmpty(title4) && !TextUtils.isEmpty(deeplink4) && !TextUtils.isEmpty(url4)) {
                remoteView.setTextViewText(R.id.title4, title4);
                remoteView.setImageViewBitmap(R.id.image_icon4, MoEHelperUtils.downloadImageBitmap(url4));
                Intent intent4 = RouteManager.getIntent(context, deeplink4);
                PendingIntent pIntent4 = PendingIntent.getActivity(context, 0, intent4,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                remoteView.setOnClickPendingIntent(R.id.image_icon4, pIntent4);
            }
            Intent intent5 = new Intent();
            PendingIntent pIntent5 = PendingIntent.getActivity(context, 0, intent5,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.image_icon5, pIntent5);

            Intent notificationIntent = new Intent(context, ParentIndexHome.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.image_icon6, contentIntent);

            builder.setSmallIcon(R.drawable.qc_launcher)
                    .setCustomContentView(remoteView)
                    .setCustomBigContentView(remoteView)
                    .setContentTitle("Tokopedia")
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setWhen(when);
        }
        return builder;
    }

    @Override
    public void onNonMoEngageMessageReceived(Context context, Bundle extras) {
        super.onNonMoEngageMessageReceived(context, extras);

    }

    @Override
    public void onNotificationNotRequired(Context context, Bundle extras) {

    }
}
