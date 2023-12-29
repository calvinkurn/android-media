package com.tokopedia.notifications.common;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public interface CMConstant {

    interface RemoteKeys{
        String KEY_IS_INAPP_ENABLE = "app_cm_inapp_enable_new2";
        String KEY_IS_CM_PUSH_ENABLE = "app_cm_push_enable_new2";
        String KEY_IS_OFFLINE_PUSH_ENABLE = "app_cm_offline_push_enabled";
        String KEY_CM_PUSH_END_TIME_INTERVAL = "app_cm_push_end_time_interval";
        String KEY_SELLERAPP_CM_ADD_TOKEN_ENABLED = "sellerapp_cm_add_token_enabled";
        String NOTIFICATION_DELETION_INTERVAL_KEY = "cm_notification_deletion_interval";
        String KEY_GENERAL_PROMPT_TIME_INTERVAL = "android_cm_general_prompt_gap_time_interval";
        String KEY_CM_REMINDER_PROMPT_PAGE_FREQ = "android_cm_reminder_prompt_page_freq";
    }


    String EXTRA_NOTIFICATION_ID = "notification_id";
    String EXTRA_BASE_MODEL = "extra_base_model";
    String EXTRA_PRODUCT_INFO = "extra_product_info";
    String EXTRA_CAROUSEL_ITEM = "extra_carousel_item";

    String EXTRA_NOTIFICATION_BUNDLE = "work_bundle";


    String FCM_TOKEN_CACHE_KEY = "fcm_token_cache";
    String USERID_CACHE_KEY = "userid_cache";
    String GADSID_CACHE_KEY = "gadsid_cache";
    String UNIQUE_APP_ID_CACHE_KEY = "unique_app_id__cache";
    String APP_VERSION_CACHE_KEY = "app_version_cache_key";
    String NEXT_PUSH_DELETE_TIME_CACHE_KEY = "next_push_delete_time";
    String INAPP_DISPLAY_COUNTER = "inapp_display_counter";
    String MAX_INAPP_DISPLAY_COUNT = "max_inapp_display_count";
    String NEXT_INAPP_DISPLAY_TIME = "next_inapp_display_time";
    String EXTRA_CAMPAIGN_ID = "extra_campaign_id";
    String EXTRA_PRE_DEF_ACTION = "extra_pre_def_action";
    String HTTP = "http";
    String WWW = "www";
    String SUFFIX_REMINDER_PROMPT_PAGE_DATA = "_reminder_prompt_data";

    interface CMPrefKeys{
        String KEY_WIFI_MAC_ADDRESS = "key_wifi_mac_address";
        String KEY_REMINDER_PROMPT_APP_DATA = "reminder_prompt_app_data";
    }


    interface NotificationType {
        String GENERAL = "General";
        String GRID_NOTIFICATION = "Grid";
        String BIG_IMAGE = "Image";
        String PERSISTENT = "Persist";
        String ACTION_BUTTONS = "Action";
        String DELETE_NOTIFICATION = "Delete";
        String DROP_NOTIFICATION = "Drop";
        String CAROUSEL_NOTIFICATION = "Carousel";
        String SILENT_PUSH = "Silent";
        String VISUAL_NOTIIFICATION = "Visual";
        String PRODUCT_NOTIIFICATION = "Product";
    }


    interface PayloadKeys {
        String ICON = "icon";
        String SOUND = "sound";
        String NOTIFICATION_ID = "notificationId";
        String SOURCE = "source";

        String FCM_EXTRA_CONFIRMATION_VALUE = "toko-cm";
        String SOURCE_VALUE = "toko-cm-inapp";

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
        String UPDATE_NOTIFICATION = "update_notification";
        String IS_TEST = "isTest";
        String GRID_DATA = "gridData";
        String SUB_TEXT = "subText";

        String VISUAL_COLLAPSED_IMAGE = "collapsedImg";
        String VISUAL_EXPANDED_IMAGE = "expandedImg";
        String VISUAL_COLLAPSED_ELEMENT_ID = "ceid";
        String VISUAL_EXPANDED_ELEMENT_ID = "eeid";
        String ACTION_BUTTON_ICON = "icon";
        String CAMPAIGN_ID = "campaignId";
        String PD_ACTION = "pdAction";
        String NOTIFICATION_PRIORITY = "priorityPreOreo";
        String PRODUCT_INFO_LIST = "product_info_list";
        String PARENT_ID = "parentId";
        String CAMPAIGN_USER_TOKEN = "campaignUserToken";
        String ELEMENT_ID = "id";
        String GENERIC_LINK = "genericLink";

        String NOTIFICATION_MODE = "isOffline";
        String NOTIFICATION_START_TIME= "startTime";
        String NOTIFICATION_END_TIME= "endTime";

        String TRANSACTION_ID = "transId";
        String USER_TRANSACTION_ID = "userTransId";
        String USER_ID = "userId";
        String SHOP_ID = "shopId";
        String IS_BIG_IMAGE = "is_big_image";
        String BLAST_ID = "notifcenterBlastId";

        String ADD_TO_CART = "addToCart";
        String PRODUCT_ID = "product_id";
        String PRODUCT_NAME = "product_name";
        String PRODUCT_BRAND = "product_brand";
        String PRODUCT_PRICE = "product_price";
        String PRODUCT_VARIANT = "product_variant";
        String PRODUCT_QUANTITY = "product_quantity";
        String ATC_SHOP_ID = "shop_id";
        String SHOP_NAME = "shop_name";
        String SHOP_TYPE = "shop_type";

        String WEBHOOK_PARAM = "webhook_params";
        String NOTIFCENTER_NOTIFICATION_ID = "nc_notif_id";
        String NOTIFCENTER_NOTIFICATION_TYPE = "nc_type_of_notif";
        String NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY = "nc_template_key";

        String NOTIFICATION_PRODUCT_TYPE = "notificationProductType";
        String FREE_DELIVERY = "bebasOngkir";
        String STOCK_AVAILABLE = "stockAvailable";
        String REVIEW_SCORE = "reviewScore";
        String REVIEW_NUMBER = "reviewNumber";
        String REVIEW_ICON = "reviewIcon";
        String IS_REVIEW = "isReviewNotif";
        String REPLY_TYPE = "replyType";

        String MAIN_APP_PRIORITY = "mainappPriority";
        String SELLER_APP_PRIORITY = "sellerappPriority";
        String ADVANCE_TARGET = "isAdvanceTarget";
        String GROUP_ID = "groupId";
        String GROUP_NAME = "groupName";
        String USER_TYPE = "userType";

        interface PayloadExtraDataKey {
            String CAMPAIGN_NAME = "cmpgnName";
            String JOURNEY_ID = "jrnId";
            String JOURNEY_NAME = "jrnName";
            String SESSION_ID = "sesnId";
            String INTENT_ACTION = "intentAction";
            String TOPCHAT = "topChat";

            String TOKOCHAT = "tokoChat";
        }


        String NOTIFICATION_CHANNEL = "nfChnl";
        String NOTIFICATION_SOUND = "nfSnd";
    }

    interface NotificationProductType {
        String V1 = "v1";
        String V2 = "v2";
    }

    interface PreDefineActionType {
        String ATC = "atc";
        String OCC = "occ";
    }

    interface ReceiverExtraData {
        String ACTION_BUTTON_APP_LINK = "action_button_app_link";
        String ACTION_BUTTON_EXTRA = "ACTION_BUTTON_EXTRA";
        String EXTRA_GRID_DATA ="EXTRA_GRID_DATA";
        String PERSISTENT_BUTTON_DATA = "persistent_data";
        String ACTION_APP_LINK = "action_app_link";
        String CAROUSEL_DATA = "carousel_data";
        String CAROUSEL_DATA_ITEM = "carousel_data_item";
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
        String ACTION_CAROUSEL_MAIN_CLICK = "com.tokopedia.notification.action_carousel_main";
        String ACTION_CAROUSEL_NOTIFICATION_DISMISS = "com.tokopedia.notification.action_carousel_dismiss";
        String ACTION_GRID_CLICK = "com.tokopedia.notification.GRID_CLICK";
        String ACTION_GRID_MAIN_CLICK = "com.tokopedia.notification.action_grid_main_click";
        String ACTION_VISUAL_COLLAPSED_CLICK = "com.tokopedia.notification.action_visual_collapsed_click";
        String ACTION_VISUAL_EXPANDED_CLICK = "com.tokopedia.notification.action_visual_expanded_click";

        String ACTION_PRODUCT_NOTIFICATION_DISMISS = "com.tokopedia.notification.product_notification_dismiss ";
        String ACTION_PRODUCT_CLICK = "com.tokopedia.notification.product_click";
        String ACTION_PRODUCT_COLLAPSED_CLICK = "com.tokopedia.notification.product_collapsed_click";
        String ACTION_PRODUCT_CAROUSEL_LEFT_CLICK = "com.tokopedia.notification.product_carousel_left_click";
        String ACTION_PRODUCT_CAROUSEL_RIGHT_CLICK = "com.tokopedia.notification.product_carousel_right_click";

        String ACTION_BANNER_CLICK ="com.tokopedia.notification.ACTION_BANNER_CLICK";
        String ACTION_NOTIFICATION_BLANK = "com.tokopedia.notification.action_notification_blank";
        String ACTION_REVIEW_NOTIFICATION_STAR_CLICKED = "com.tokopedia.notification.STAR_CLICKED";
    }

    interface CouponCodeExtra {
        String COUPON_CODE = "coupon_code";
        String GRATIFICATION_ID = "gratificationId";
    }

    interface CustomValuesKeys {
        String COUPON_CODE = "coupon_code";
        String GRATIFICATION_ID = "gratificationId";
        String TOKOCHAT_PN_ID = "tokochat_pn_id";
    }

    interface TimberTags {
        int MAX_LIMIT = 1000;
    }

    interface UTMParams {
        String UTM_SOURCE = "utm_source";
        String UTM_MEDIUM = "utm_medium";
        String UTM_CAMPAIGN = "utm_campaign";
        String UTM_CONTENT = "utm_content";
        String UTM_TERM = "utm_term";
        String SCREEN_NAME = "screenName";
        String SCREEN_NAME_VALUE = "CM Applink Handler";
        String UTM_GCLID = "gclid";
    }

    interface ReviewStarNumber {
        String STAR_NUMBER = "starNumber";
        String ONE_STAR = "1";
        String TWO_STAR = "2";
        String THREE_STAR = "3";
        String FOUR_STAR = "4";
        String FIVE_STAR = "5";
    }

    interface GtmTrackerEvents {
        String KEY_USER_ID = "userId";
        String KEY_CURRENT_SITE = "currentSite";
        String KEY_BUSINESS_UNIT = "businessUnit";
        String KEY_TRACKER_ID = "trackerId";
        String KEY_DEVICE_ID = "deviceId";
        String KEY_PAGE_PATH = "pagePath";
        String KEY_PAGE_TYPE = "pageType";
        String VALUE_BUSINESS_UNIT = "cm";
        String VALUE_TRACKER_ID_VIEW = "35363";
        String VALUE_TRACKER_ID_NOT_ALLOW = "35365";
        String VALUE_TRACKER_ID_ALLOW = "35364";
        String VALUE_TRACKER_ID_VIEW_SA = "37113";
        String VALUE_TRACKER_ID_NOT_ALLOW_SA = "37115";
        String VALUE_TRACKER_ID_ALLOW_SA = "37114";
        String VALUE_TRACKER_ID_VIEW_GEN_MA = "37792";
        String VALUE_TRACKER_ID_VIEW_GEN_SA = "37795";
        String VALUE_TRACKER_ID_CLICK_CLOSE_GEN_SA = "37796";
        String VALUE_TRACKER_ID_CLICK_CLOSE_GEN_MA = "37793";
        String VALUE_TRACKER_ID_CLICK_CTA_GEN_SA = "37797";
        String VALUE_TRACKER_ID_CLICK_CTA_GEN_MA = "37794";
        String VALUE_TRACKER_ID_REMINDER_VIEW = "42642";
        String VALUE_TRACKER_ID_REMINDER_VIEW_SA = "42649";
        String VALUE_TRACKER_ID_CLICK_CTA_REMINDER = "42645";
        String VALUE_TRACKER_ID_CLICK_CTA_REMINDER_SA = "42650";
        String VALUE_TRACKER_ID_CLICK_CLOSE_REMINDER = "42646";
        String VALUE_TRACKER_ID_CLICK_CLOSE_REMINDER_SA = "42651";
        String VALUE_CURRENT_SITE = "tokopediamarketplace";
        String VALUE_EVENT_CLICK_CONTENT = "clickContent";
        String VALUE_EVENT_VIEW_CONTENT = "viewContentIris";
        String VALUE_CATEGORY = "native android push prompt";
        String VALUE_CATEGORY_SA = "native android seller push prompt";
        String VALUE_GEN_CATEGORY = "general android push prompt";
        String VALUE_GEN_CATEGORY_SA = "general android seller push prompt";
        String VALUE_REMINDER_CATEGORY = "reminder push prompt";
        String VALUE_REMINDER_CATEGORY_SA = "seller reminder push prompt";
        String VALUE_ACTION_IMPRESSION = "impression";
        String VALUE_ACTION_NOT_ALLOW = "click not allow";
        String VALUE_ACTION_ALLOW = "click allow";
        String VALUE_ACTION_CLICK_CLOSE = "click close";
        String VALUE_ACTION_CLICK_CTA = "click cta";
        String KEY_EVENT_NAME = "event_name";
        String KEY_EVENT_GA = "event_ga";
        String KEY_MAIN_APP_PUSH_PERMISSION_STATUS = "android_push_permission_mainapp_device_status";
        String KEY_SELLER_APP_PUSH_PERMISSION_STATUS = "android_push_permission_sellerapp_device_status";
        String KEY_TRAFFIC_SOURCE_NAME = "campaign_name";
        String KEY_TRAFFIC_SOURCE_ID = "campaign_id";
        String KEY_EVENT_REFRESH_SOURCE = "refreshsource";
        String KEY_DELAY_HRS_OPEN_HOME_SCR = "delayhours";
        String KEY_DELAY_DAY_OPEN_HOME_SCR = "delayday";
        String KEY_DEVICE_ID_NEW = "device_id";
        String KEY_USER_ID_NEW = "user_id";
        String KEY_SHOP_ID_NEW = "shop_id";
        String KEY_CLIENT_TIMESTAMP = "client_timestamp";
        String KEY_DEVICE_MODEL = "device_model";
        String KEY_OS_VESION = "os_version";
        String KEY_OS_NAME = "os_name";
        String KEY_APP_VERSION = "app_version";
        String KEY_DEVICE_MANUFACTURER = "device_manufacturer";
        String KEY_PROMO_APP_PUSHPERMISSION_LATESTSTATUS = "apppushpermissionstatus";
        String KEY_DEVICE_PUSHPERMISSION_LATESTSTATUS = "devicepushpermissionstatus";
    }

}