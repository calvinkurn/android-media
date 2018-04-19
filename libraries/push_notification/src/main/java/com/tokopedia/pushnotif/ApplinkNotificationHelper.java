package com.tokopedia.pushnotif;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.pushnotif.factory.SummaryNotificationFactory;
import com.tokopedia.pushnotif.factory.TalkNotificationFactory;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;
import com.tokopedia.pushnotif.model.HistoryNotificationModel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author ricoharisin .
 */

public class ApplinkNotificationHelper {

    private Context context;

    public ApplinkNotificationHelper(Context context) {
        this.context = context;
    }

    public void notifyApplinkNotification(Bundle data) {
        ApplinkNotificationModel applinkNotificationModel = convertToApplinkModel(data);

    }

    public static ApplinkNotificationModel convertToApplinkModel(Bundle data) {
        ApplinkNotificationModel model = new ApplinkNotificationModel();
        model.setApplinks(data.getString("applinks", ApplinkConst.HOME));
        model.setCounter(data.getString("counter", ""));
        model.setCreateTime(data.getString("create_time", ""));
        model.setDesc(data.getString("desc", ""));
        model.setFullName(data.getString("full_name", ""));
        model.setGId(data.getString("g_id", ""));
        model.setLoginRequired(data.getString("login_required", "false").equals("true"));
        model.setSenderId(data.getString("sender_id", ""));
        model.setSummary(data.getString("summary", ""));
        model.setThumbnail(data.getString("thumbnail", ""));
        model.setTkpCode(Integer.parseInt(data.getString("tkp_code", "0")));
        model.setToUserId(data.getString("to_user_id", ""));

        return model;
    }

    public static Boolean allowToShow(Context context, ApplinkNotificationModel applinkNotificationModel) {
        String loginId = ((AbstractionRouter) context.getApplicationContext()).getSession().getUserId();
        return applinkNotificationModel.getToUserId().equals(loginId) &&
                checkLocalNotificationAppSettings(context, applinkNotificationModel.getTkpCode());
    }

    public static int getNotificationId(String appLinks) {
        Uri uri = Uri.parse(appLinks);
        if (appLinks.contains("talk") || appLinks.contains("chat")) {
            return Integer.parseInt(uri.getLastPathSegment());
        }

        return 0;
    }

    public static int generateNotifictionId(String appLink) {
        if (appLink.contains("talk")) {
            return Constant.NotificationId.TALK;
        } else if (appLink.contains("message")) {
            return Constant.NotificationId.GENERAL;
        } else if (appLink.contains("groupchat")) {
            return Constant.NotificationId.GROUPCHAT;
        } else if (appLink.contains("chat")) {
            return Constant.NotificationId.CHAT;
        } else if (appLink.contains("buyer")) {
            return Constant.NotificationId.TRANSACTION;
        } else if (appLink.contains("seller")) {
            return Constant.NotificationId.SELLER;
        } else if (appLink.contains("resolution")) {
            return Constant.NotificationId.RESOLUTION;
        } else {
            return Constant.NotificationId.GENERAL;
        }
    }

    public static boolean checkLocalNotificationAppSettings(Context context, int code) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        switch (code) {
            case Constant.GCMServiceState.GCM_CHAT:
            case Constant.GCMServiceState.GCM_MESSAGE:
                return settings.getBoolean(Constant.Settings.NOTIFICATION_PM, true);
            case Constant.GCMServiceState.GCM_TALK:
                return settings.getBoolean(Constant.Settings.NOTIFICATION_TALK, true);

            case Constant.GCMServiceState.GCM_REVIEW:
            case Constant.GCMServiceState.GCM_REVIEW_EDIT:
            case Constant.GCMServiceState.GCM_REVIEW_REPLY:
                return settings.getBoolean(Constant.Settings.NOTIFICATION_REVIEW, true);

            case Constant.GCMServiceState.GCM_PROMO:
            case Constant.GCMServiceState.GCM_HOT_LIST:
                return settings.getBoolean(Constant.Settings.NOTIFICATION_PROMO, true);

            case Constant.GCMServiceState.GCM_REPUTATION_SMILEY_TO_BUYER:
            case Constant.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_BUYER:
            case Constant.GCMServiceState.GCM_REPUTATION_SMILEY_TO_SELLER:
            case Constant.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_SELLER:
                return settings.getBoolean(Constant.Settings.NOTIFICATION_REP, true);

            case Constant.GCMServiceState.GCM_NEWORDER:
                return settings.getBoolean(Constant.Settings.NOTIFICATION_SALES, true);

            case Constant.GCMServiceState.GCM_PURCHASE_VERIFIED:
            case Constant.GCMServiceState.GCM_PURCHASE_ACCEPTED:
            case Constant.GCMServiceState.GCM_PURCHASE_PARTIAL_PROCESSED:
            case Constant.GCMServiceState.GCM_PURCHASE_REJECTED:
            case Constant.GCMServiceState.GCM_PURCHASE_DELIVERED:
                return settings.getBoolean(Constant.Settings.NOTIFICATION_PURCHASE, true);

            case Constant.GCMServiceState.GCM_PURCHASE_DISPUTE:
            case Constant.GCMServiceState.GCM_RESCENTER_SELLER_REPLY:
            case Constant.GCMServiceState.GCM_RESCENTER_BUYER_REPLY:
            case Constant.GCMServiceState.GCM_RESCENTER_SELLER_AGREE:
            case Constant.GCMServiceState.GCM_RESCENTER_BUYER_AGREE:
            case Constant.GCMServiceState.GCM_RESCENTER_ADMIN_SELLER_REPLY:
            case Constant.GCMServiceState.GCM_RESCENTER_ADMIN_BUYER_REPLY:
                return settings.getBoolean(Constant.Settings.NOTIFICATION_RESCENTER, true);

            case Constant.GCMServiceState.GCM_SELLER_INFO:
                return settings.getBoolean(Constant.Settings.NOTIFICATION_SELLER_INFO, true);

            default:
                return true;
        }
    }
}

