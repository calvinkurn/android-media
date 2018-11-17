package com.tokopedia.notifications.common;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public interface CMConstant {
    String FCM_EXTRA_CONFIRMATION_KEY = "is_from_cm_platform";
    String FCM_EXTRA_CONFIRMATION_VALUE = "cm_platform";
    String GENERAL = "ANDROID_GENERAL_CHANNEL";
    String EXTRA_APPLINK_FROM_PUSH = "applink_from_cm_notif";
    String EXTRA_NOTIFICATION_TYPE = "notif_type";
    String EXTRA_NOTIFICATION_ID = "notif_id";

    String NOTIFICATION_ACTION_BUTTONS = "action_buttons";
    String NOTIFICATION_PERSISTENT = "per_data";


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
        String ACTION_BUTTONS = "cm_action_btn";
    }

    interface ActionButtonExtra {
        String ACTION_BUTTON_APP_LINK = "action_button_app_link";
    }

    interface NotificationGroup {
        String CHANNEL = "Demo";
        String CHANNEL_DESCRIPTION = "DESCRIPTION";
        String CHANNEL_ID = "111";

        String CHANNEL_GROUP_ID = "campaign_group_01";
        String CHANNEL_GROUP_NAME = "Campaign";
    }

    interface PersistentExtra{
        String KEY_ICON_URL1 = "icon_url1";
        String EXTRA_APP_LINK_1 = "app_link_1";
    }

}
