package com.tokopedia.notifications.common;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public interface CMConstant {
    String FCM_EXTRA_CONFIRMATION_KEY = "isFromCM";
    String FCM_EXTRA_CONFIRMATION_VALUE = "cm_platform";
    String GENERAL = "ANDROID_GENERAL_CHANNEL";
    String EXTRA_APPLINK_FROM_PUSH = "applink_from_cm_notif";
    String EXTRA_NOTIFICATION_TYPE = "notif_type";
    String EXTRA_NOTIFICATION_ID = "notif_id";

    String NOTIFICATION_ACTION_BUTTONS = "actionButton";
    String NOTIFICATION_PERSISTENT = "persButton";


    String NOTIFICATION_CUSTOM_VALUES = "custom_values";
    String FCM_TOKEN_CACHE_KEY = "fcm_token_cache";
    String USERID_CACHE_KEY = "userid_cache";
    String GADSID_CACHE_KEY = "gadsid_cache";
    String UNIQUE_APP_ID_CACHE_KEY = "unique_app_id__cache";

    String EXTRA_NOTIFICATION_BUNDLE = "work_bundle";

    interface NotificationId {
        int GENERAL = 501;
        int BIG_IMAGE = 502;
        int PERSISTENT = 503;
        int CUSTOM = 504;
        int IN_APP = 505;
        int CAROUSEL = 506;
        int VIDEO = 507;
        int ACTION_BUTTONS = 508;
    }

    interface NotificationType {
        String GENERAL = "cm_general";
        String BIG_IMAGE = "cm_big";
        String PERSISTENT = "cm_persistent";
        String CUSTOM = "cm_custom";
        String IN_APP = "cm_in_app";
        String CAROUSEL = "cm_carousel";
        String VIDEO = "cm_video";
        String ACTION_BUTTONS = "cm_action_button";
    }

    interface ActionButtonExtra {
        String ACTION_BUTTON_APP_LINK = "action_button_app_link";
    }

    interface NotificationGroup {
        String CHANNEL = "Digital";
        String CHANNEL_DESCRIPTION = "Digital";
        String CHANNEL_ID = "Digital";
        String CHANNEL_GROUP_ID = "marketing_group_01";
        String CHANNEL_GROUP_NAME = "Marketing";
    }

    interface ReceiverAction{
        String ACTION_BUTTON = "com.tokopedia.notification.ACTION_BUTTON";
        String ACTION_CANCEL_PERSISTENT = "com.tokopedia.notification.ACTION_CANCEL_PERSISTENT";
        String ACTION_ON_NOTIFICATION_DISMISS = "com.tokopedia.notification.ACTION_ON_NOTIFICATION_DISMISS";
    }

}
