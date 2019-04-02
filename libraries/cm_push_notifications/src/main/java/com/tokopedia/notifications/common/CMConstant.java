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
    String APP_VERSION_CACHE_KEY = "app_version_cache_key";


    interface NotificationType {
        String GENERAL = "General";
        String GRID_NOTIFICATION = "Grid";
        String BIG_IMAGE = "Image";
        String PERSISTENT = "Persist";
        String ACTION_BUTTONS = "Action";
        String DELETE_NOTIFICATION = "Delete";
        String CAROUSAL_NOTIFICATION = "Carousal";
        String SILENT_PUSH = "Silent";
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

        String VIDEO_DATA = "videoData";
        String CAROUSAL_DATA =  "carousal";
        String CAROUSAL_INDEX =  "carousalIndex";
        String IMG =  "img";
        String VIBRATE = "vibrate";
        String UPDATE = "update";
        String GRID_DATA = "gridData";
        String SUB_TEXT = "subText";


    }

    interface ReceiverExtraData {
        String ACTION_BUTTON_APP_LINK = "action_button_app_link";
        String PERSISTENT_BUTTON_DATA = "persistent_data";
        String ACTION_APP_LINK = "action_app_link";
        String CAROUSAL_DATA = "carousal_data";
        String CAROUSAL_DATA_ITEM = "carousal_data_item";


        String GRID_APP_LINK = "GRID_APP_LINK";
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
        String ACTION_NOTIFICATION_CLICK = "com.tokopedia.notification.ACTION_NOTIFICATION_CLICK";
        String ACTION_RIGHT_ARROW_CLICK = "com.tokopedia.notification.ACTION_RIGHT_ARROW_CLICK";
        String ACTION_LEFT_ARROW_CLICK = "com.tokopedia.notification.ACTION_LEFT_ARROW_CLICK";
        String ACTION_CAROUSAL_IMAGE_CLICK = "com.tokopedia.notification.ACTION_CAROUSAL_IMAGE_CLICK";
        String ACTION_GRID_CLICK = "com.tokopedia.notification.GRID_CLICK";
    }

    interface CouponCodeExtra {
        String COUPON_CODE = "coupon_code";
    }

    interface CustomValuesKeys {
        String COUPON_CODE = "coupon_code";
    }

}