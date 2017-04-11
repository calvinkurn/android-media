package com.tokopedia.core.gcm;

/**
 * @author  by alvarisi on 12/20/16.
 */

public interface Constants {
    String FIREBASE_PROJECT_ID = "673352445777";
    String ARG_NOTIFICATION_CODE = "tkp_code";
    String ARG_NOTIFICATION_TITLE = "title";
    String ARG_NOTIFICATION_DESCRIPTION = "desc";
    String ARG_NOTIFICATION_IMAGE = "url_img";
    String ARG_NOTIFICATION_ICON = "url_icon";
    String ARG_NOTIFICATION_URL = "url";
    String ARG_NOTIFICATION_UPDATE_APPS_TITLE = "title_update";
    String ARG_NOTIFICATION_TARGET_USER_ID = "to_user_id";
    String ARG_NOTIFICATION_CART_EXISTS = "is_cart_exists";
    String ARG_NOTIFICATION_APPLINK = "applinks";
    String ARG_NOTIFICATION_ISPROMO = "ispromo";
    String ARG_NOTIFICATION_APPLINK_MESSAGE = "message";
    String ARG_NOTIFICATION_APPLINK_MESSAGE_CUSTOM_INDEX = "sender_id";
    String ARG_NOTIFICATION_APPLINK_DISCUSSION = "talk";
    String ARG_NOTIFICATION_APPLINK_DISCUSSION_CUSTOM_INDEX = "sender_id";
    String ARG_NOTIFICATION_APPLINK_PROMO_LABEL = "promo";
    String KEY_ORIGIN = "origin";
    int REGISTRATION_STATUS_OK = 1;
    int REGISTRATION_STATUS_ERROR = 2;
    String REGISTRATION_MESSAGE_OK = "FCM Sucessfully";
    String REGISTRATION_MESSAGE_ERROR = "FCM Error";
    String EXTRA_PLAYSTORE_URL = "market://details?id=com.tokopedia.tkpd";
    String EXTRA_FROM_PUSH = "from_notif";
    String EXTRA_UNREAD = "unread";
    String EXTRA_APPLINK_CATEGORY = "applink_category";
    String EXTRA_APPLINK_RESET = "applink_reset";
    int ARG_NOTIFICATION_APPLINK_MESSAGE_ID = 1001;
    int ARG_NOTIFICATION_APPLINK_DISCUSSION_ID = 1002;
    int ARG_NOTIFICATION_APPLINK_PROMO = 1003;

    String ACTION_BC_RESET_APPLINK = "com.tokopedia.tkpd.APPLINK_ACTION";
    String ARG_NOTIFICATION_APPLINK_LOGIN_REQUIRED = "login_required";

    interface Applinks{
        String MESSAGE = "tokopedia://message";
        String MESSAGE_DETAIL = "tokopedia://message/{message_id}";
        String TALK = "tokopedia://talk";
        String TALK_DETAIL = "tokopedia://talk/{talk_id}";
    }

    interface Settings{
        String NOTIFICATION_RINGTONE    = "notifications_new_message_ringtone";
        String NOTIFICATION_VIBRATE     = "notifications_new_message_vibrate";
        String NOTIFICATION_PROMO       = "notification_receive_promo";
        String NOTIFICATION_PM          = "notification_receive_pm";
        String NOTIFICATION_TALK        = "notification_receive_talk";
        String NOTIFICATION_REVIEW      = "notification_receive_review";
        String NOTIFICATION_REP         = "notification_receive_reputation";
        String NOTIFICATION_SALES       = "notification_sales";
        String NOTIFICATION_PURCHASE    = "notification_purchase";
        String NOTIFICATION_RESCENTER   = "notification_receive_rescenter";

    }
}
