package com.tokopedia.tkpd.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.moengage.core.ConfigurationProvider;
import com.moengage.pushbase.push.PushMessageListener;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.ParentIndexHome;

public class CustomPushListener extends PushMessageListener {


    @Override
    protected void onPostNotificationReceived(Context context, Bundle extras) {
        super.onPostNotificationReceived(context, extras);


        //Update your own notification inbox
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

        //add properties to the builder as required
        long when = System.currentTimeMillis();
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews contentViewBig = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);
        //Null and Empty checks for your Key Value Pairs
//        if(imgUrl!=null && !imgUrl.isEmpty()) {
//            URL imgUrlLink = new URL(imgUrl);
//            contentViewBig.setImageViewBitmap(R.id.image_pic, BitmapFactory.decodeStream(imgUrlLink.openConnection().getInputStream()));
//        }
//        if(appImgUrl!=null && !appImgUrl.isEmpty()) {
//            URL appImgUrlLink = new URL(appImgUrl);
//            contentViewBig.setImageViewBitmap(R.id.image_app, BitmapFactory.decodeStream(appImgUrlLink.openConnection().getInputStream()));
//            contentViewSmall.setImageViewBitmap(R.id.image_app, BitmapFactory.decodeStream(appImgUrlLink.openConnection().getInputStream()));
//        }
//        if(notifTitle!=null && !notifTitle.isEmpty()) {
//            contentViewBig.setTextViewText(R.id.title, notifTitle);
//            contentViewSmall.setTextViewText(R.id.title, notifTitle);
//        }
//
//        if(notifText!=null && !notifText.isEmpty()) {
//            contentViewBig.setTextViewText(R.id.text, notifText);
//            contentViewSmall.setTextViewText(R.id.text, notifText);
//        }

        Intent notificationIntent = new Intent(context, ParentIndexHome.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_icon)
                .setCustomContentView(contentViewBig)
                .setCustomBigContentView(contentViewBig)
                .setContentTitle("Custom Notification")
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setWhen(when);
        return builder;
    }

    @Override
    public void onNonMoEngageMessageReceived(Context context, Bundle extras) {
        super.onNonMoEngageMessageReceived(context, extras);
        //do your own stuff here
    }

    @Override
    public void onNotificationNotRequired(Context context, Bundle extras) {

    }

    public void createNotification() {

    }
}
