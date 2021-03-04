package com.tokopedia.pushnotif;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.pushnotif.data.constant.Constant;
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import static com.tokopedia.pushnotif.data.constant.Constant.Host.CHATBOT;

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
        model.setTitle(data.getString("title", ""));
        model.setTargetApp(data.getString("target_app", ""));
        model.setTransactionId(data.getString("trans_id", ""));
        model.setImages(data.getString("images", ""));
        model.setMainAppPriority(data.getString("mainapp_priority", ""));
        model.setSellerAppPriority(data.getString("sellerapp_priority", ""));
        model.setIsAdvanceTarget(data.getString("is_advance_target", "false").equals("true"));
        return model;
    }

    public static Boolean allowToShow(Context context, ApplinkNotificationModel applinkNotificationModel) {
        UserSessionInterface userSession = new UserSession(context);
        String loginId = userSession.getUserId();
        return applinkNotificationModel.getToUserId().equals(loginId) &&
                checkLocalNotificationAppSettings(context, applinkNotificationModel.getTkpCode())
                && isTargetApp(applinkNotificationModel);
    }

    public static int getNotificationId(String appLinks) {
        try {
            Uri uri = Uri.parse(appLinks);
            String host = uri.getHost();
            if (host.equals("talk") || host.equals("topchat")) {
                return Integer.parseInt(uri.getLastPathSegment());
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int generateNotifictionId(String appLink) {
        try {
            Uri uri = Uri.parse(appLink);
            String host = uri.getHost();
            switch (host) {
                case "talk":
                    return Constant.NotificationId.TALK;
                case "groupchat":
                    return Constant.NotificationId.GROUPCHAT;
                case "topchat":
                    return Constant.NotificationId.CHAT;
                case "buyer":
                    return Constant.NotificationId.TRANSACTION;
                case "seller":
                    return Constant.NotificationId.SELLER;
                case "resolution":
                    return Constant.NotificationId.RESOLUTION;
                case CHATBOT:
                    return Constant.NotificationId.CHAT_BOT;
                case "product-review":
                    return Constant.NotificationId.REVIEW;
                default:
                    return Constant.NotificationId.GENERAL;
            }
        } catch (Exception e) {
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

    public static Boolean allowGroup() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT;
    }

    private static final int CONSUMER_PRO_APPLICATION = 3;

    public static Boolean isTargetApp(ApplinkNotificationModel applinkNotificationModel) {
        if (GlobalConfig.APPLICATION_TYPE == CONSUMER_PRO_APPLICATION) {
            return (applinkNotificationModel.getTargetApp() == null) ||
                    (applinkNotificationModel.getTargetApp() != null && applinkNotificationModel.getTargetApp().contains(GlobalConfig.PACKAGE_CONSUMER_APP));
        }
        else {
            return (applinkNotificationModel.getTargetApp() == null) ||
                    (applinkNotificationModel.getTargetApp() != null && applinkNotificationModel.getTargetApp().contains(GlobalConfig.APPLICATION_ID));
        }
    }
}

