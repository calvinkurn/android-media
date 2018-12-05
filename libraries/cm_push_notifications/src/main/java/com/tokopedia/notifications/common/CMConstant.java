package com.tokopedia.notifications.common;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public interface CMConstant {
    String EXTRA_NOTIFICATION_ID = "notif_id";

    String EXTRA_NOTIFICATION_BUNDLE = "work_bundle";


    String FCM_TOKEN_CACHE_KEY = "fcm_token_cache";
    String USERID_CACHE_KEY = "userid_cache";
    String GADSID_CACHE_KEY = "gadsid_cache";
    String UNIQUE_APP_ID_CACHE_KEY = "unique_app_id__cache";


    interface NotificationType {
        String GENERAL = "General";
        String BIG_IMAGE = "Image";
        String PERSISTENT = "Persist";
        String ACTION_BUTTONS = "Action";
        String DELETE_NOTIFICATION = "Delete";
        String COUPON_CODE_NOTIFICATION = "coupon";
    }


    interface PayloadKeys{
        String ICON = "icon";
        String SOUND = "sound";
        String NOTIFICATION_ID = "notificationId";
        String SOURCE = "source";

        String FCM_EXTRA_CONFIRMATION_VALUE = "toko-cm";

        String TRIBE_KEY = "tribe";
        String NOTIFICATION_TYPE = "notificationType";
        String CHANNEL = "channel";

        String TITLE = "title";
        String MESSAGE = "message";
        String DESCRIPTION = "desc";
        String MEDIA = "media";
        String APP_LINK = "appLink";

        String ACTION_BUTTON = "actionButtons";
        String PERSISTENT_DATA = "persistentButtons";
        String TEXT = "text";

        String CUSTOM_VALUE = "customValues";
    }

    interface ReceiverExtraData {
        String ACTION_BUTTON_APP_LINK = "action_button_app_link";
        String PERSISTENT_BUTTON_DATA = "persistent_data";

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
        String ACTION_ON_COPY_COUPON_CODE = "com.tokopedia.notification.ACTION_ON_COPY_COUPON_CODE";
        String ACTION_PERSISTENT_CLICK = "com.tokopedia.notification.ACTION_PERSISTENT_CLICK";

    }

    interface CouponCodeExtra {
        String COUPON_CODE = "coupon_code";
    }

    interface CustomValuesKeys {
        String COUPON_CODE = "coupon_code";
    }

}


/*
    String NOTIFICATION_CUSTOM_VALUES = "custom_values";
    String FCM_EXTRA_CONFIRMATION_KEY = "isFromCM";
    String FCM_EXTRA_CONFIRMATION_VALUE = "cm_toko";
    String GENERAL = "ANDROID_GENERAL_CHANNEL";
    String EXTRA_APPLINK_FROM_PUSH = "applink_from_cm_notif";
    String EXTRA_NOTIFICATION_TYPE = "notif_type";
    String NOTIFICATION_ACTION_BUTTONS = "actionButton";
    String NOTIFICATION_PERSISTENT = "persButton";

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


        /*
        String CUSTOM = "cm_custom";
        String IN_APP = "cm_in_app";
        String CAROUSEL = "cm_carousel";
        String VIDEO = "cm_video";*/