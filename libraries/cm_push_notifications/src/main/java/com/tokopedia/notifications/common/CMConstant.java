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
    String EXTRA_CAMPAIGN_ID = "extra_campaign_id";
    String EXTRA_PRE_DEF_ACTION = "extra_pre_def_action";



    interface NotificationType {
        String GENERAL = "General";
        String GRID_NOTIFICATION = "Grid";
        String BIG_IMAGE = "Image";
        String PERSISTENT = "Persist";
        String ACTION_BUTTONS = "Action";
        String DELETE_NOTIFICATION = "Delete";
        String CAROUSEL_NOTIFICATION = "Carousel";
        String SILENT_PUSH = "Silent";
        String VISUAL_NOTIIFICATION = "Visual";
    }


    interface PayloadKeys {
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
        String TYPE = "type";


        String CUSTOM_VALUE = "customValues";

        String VIDEO_DATA = "videoData";
        String CAROUSEL_DATA = "carousel";
        String CAROUSEL_INDEX = "carouselIndex";
        String IMG = "img";
        String VIBRATE = "vibrate";
        String UPDATE = "update";
        String GRID_DATA = "gridData";
        String SUB_TEXT = "subText";


        String VISUAL_COLLAPSED_IMAGE = "collapsedImg";
        String VISUAL_EXPANDED_IMAGE = "expandedImg";
        String ACTION_BUTTON_ICON = "icon";
        String CAMPAIGN_ID = "campaignId";
        String PD_ACTION = "pdAction";
        String NOTIFICATION_PRIORITY = "priorityPreOreo";
    }

    interface ReceiverExtraData {
        String ACTION_BUTTON_APP_LINK = "action_button_app_link";
        String PERSISTENT_BUTTON_DATA = "persistent_data";
        String ACTION_APP_LINK = "action_app_link";
        String CAROUSEL_DATA = "carousel_data";
        String CAROUSEL_DATA_ITEM = "carousel_data_item";


        String GRID_APP_LINK = "GRID_APP_LINK";
    }

    interface NotificationChannel {
        String CHANNEL = "General";
        String CHANNEL_ID = "General";
        String CHANNEL_DESCRIPTION = "General";

        String Channel_DefaultSilent_Id = "Default_Channel";
        String Channel_DefaultSilent_Name = "Default";
        String Channel_DefaultSilent_DESCRIPTION = "Dafault Silent";
    }

    interface ReceiverAction {
        String ACTION_BUTTON = "com.tokopedia.notification.ACTION_BUTTON";
        String ACTION_CANCEL_PERSISTENT = "com.tokopedia.notification.ACTION_CANCEL_PERSISTENT";
        String ACTION_ON_NOTIFICATION_DISMISS = "com.tokopedia.notification.ACTION_ON_NOTIFICATION_DISMISS";
        String ACTION_ON_COPY_COUPON_CODE = "com.tokopedia.notification.ACTION_ON_COPY_COUPON_CODE";
        String ACTION_PERSISTENT_CLICK = "com.tokopedia.notification.ACTION_PERSISTENT_CLICK";
        String ACTION_NOTIFICATION_CLICK = "com.tokopedia.notification.ACTION_NOTIFICATION_CLICK";
        String ACTION_RIGHT_ARROW_CLICK = "com.tokopedia.notification.ACTION_RIGHT_ARROW_CLICK";
        String ACTION_LEFT_ARROW_CLICK = "com.tokopedia.notification.ACTION_LEFT_ARROW_CLICK";
        String ACTION_CAROUSEL_IMAGE_CLICK = "com.tokopedia.notification.ACTION_CAROUSEL_IMAGE_CLICK";
        String ACTION_GRID_CLICK = "com.tokopedia.notification.GRID_CLICK";
    }

    interface CouponCodeExtra {
        String COUPON_CODE = "coupon_code";
    }

    interface CustomValuesKeys {
        String COUPON_CODE = "coupon_code";
    }

}