package com.tokopedia.pushnotif.factory;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.R;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author ricoharisin .
 */

public class BaseNotificationFactory {

    protected Context context;

    public BaseNotificationFactory(Context context) {
        this.context = context;
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
            return R.drawable.ic_big_notif_customerapp;
    }

    protected Bitmap getBitmap(String url) {
        try {
            return Glide.with(context).load(url)
                    .asBitmap()
                    .into(getImageWidth(), getImageHeight())
                    .get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e ) {
            return BitmapFactory.decodeResource(context.getResources(), getDrawableLargeIcon());
        }
    }

    protected int getImageWidth() {
        return context.getResources().getDimensionPixelSize(R.dimen.notif_width);
    }

    protected int getImageHeight() {
        return context.getResources().getDimensionPixelSize(R.dimen.notif_height);
    }

    protected PendingIntent createPendingIntent(String appLinks, int notificationId) {
        PendingIntent resultPendingIntent;
        Intent intent = RouteManager.getIntent(context, appLinks);
        intent.setData(Uri.parse(appLinks));
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.EXTRA_APPLINK_FROM_PUSH, true);
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
}
