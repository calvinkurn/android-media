package com.tokopedia.tokopoints.view.util;

import static com.tokopedia.tokopoints.view.util.CommonConstant.BaseUrl.WEB_DOMAIN_MOBILE;

public interface CommonConstant {
    String UTF_ENCODING = "UTF-8";
    String COUPON_MIME_TYPE = "text/html";
    String FRAGMENT_DETAIL_TOKOPOINT = "sendFragment";
    String PREF_TOKOPOINTS = "pref_tokopoints";
    String PREF_KEY_ON_BOARDED = "pref_key_on_boarded";
    String ARGS_CATEGORY_ID = "category_id";
    String ARGS_SUB_CATEGORY_ID = "sub_category_id";
    String ARGS_SLUG_CATEGORY = "slug_category";
    String ARGS_SLUG_SUB_CATEGORY = "slug_sub_category";
    String ARGS_SORT_TYPE = "sort_type";
    int DEVICE_ID_BANNER = 256;
    int PAGE_SIZE = 10;
    int HOMEPAGE_PAGE_SIZE = 10;
    int TAB_SETUP_DELAY_MS = 150;
    int HOMEPAGE_TAB_COUNT = 1;
    int DEFAULT_SORT_TYPE = 1;
    int DEFAULT_CATEGORY_TYPE = 1;
    int DEFAULT_AUTO_REFRESH_S = 10000;
    int CATALOG_TYPE_FLASH_SALE = 3;
    int MY_COUPON_TAB = 1;
    int PIN_COUNT = 4;
    String TOKOPOINTS_CATALOG_STATUS_AUTO_REFRESH_S = "tokopoints_catalog_status_auto_refresh_s";
    String TOKOPOINTS_NEW_HOME = "tokopoints_homepage_new_ui";
    String TOKOPOINTS_NEW_COUPON_LISTING = "tokopoints_coupon_stack_new_ui";
    String EXTRA_COUPON_COUNT = "extra_coupon_count";
    String EXTRA_COUPON_CODE = "coupon_code";
    String EXTRA_CATALOG_CODE = "catalog_code";
    String EXTRA_COUPON_ID = "extra_coupon_id";
    String EXTRA_PIN_INFO = "extra_pin_info";
    String EXTRA_COUPON_TITLE = "extra_coupon_title";
    String EXTRA_COUPON_BANNER = "extra_coupon_banner";
    String EXTRA_COUPON_POINT = "extra_coupon_point";
    String EXTRA_SLUG = "slug";
    String CLIPBOARD_COUPON_CODE = "tokopedia_tokopoints_coupon";
    int MAX_COUPON_RE_FETCH_COUNT = 3;
    int COUPON_RE_FETCH_DELAY_S = 5;
    long COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S = 86400;
    int UI_SETTLING_DELAY_MS = 250;
    int UI_SETTLING_DELAY_MS2 = 500;
    String ARGS_POINTS_AVAILABILITY = "points_availability";
    String BUNDLE_ARGS_USER_IS_LOGGED_IN = "USER_IS_LOGGEDIN";
    String APIVERSION = "2.0.0";
    String IMPRESSION_LIST="/tokopoints - p{x} - promo lis";

    interface CouponRedemptionCode {
        int LOW_POINT = 42020;
        int PROFILE_INCOMPLETE = 42021;
        int QUOTA_LIMIT_REACHED = 42022;
        int SUCCESS = 200;
    }

    interface GraphqlVariableKeys {
        String PAGE = "page";
        String PAGE_SIZE = "limit";
        String SORT_ID = "sortID";
        String CATEGORY_ID = "categoryID";
        String SUB_CATEGORY_ID = "subCategoryID";
        String POINTS_RANGE = "pointRange";
        String CATALOG_ID = "catalog_id";
        String IS_GIFT = "is_gift";
        String PROMO_CODE = "promoCode";
        String DEVICE = "device";
        String SERVICE_ID = "serviceID";
        String CATEGORY_ID_COUPON = "categoryIDCoupon";
        String SLUG = "slug";
        String SLUG_CATEGORY = "slugCategory";
        String SLUG_SUB_CATEGORY = "slugSubCategory";
        String CATALOG_IDS = "catalogIDs";
        String CODE = "code";
        String GIFT_EMAIL = "gift_email";
        String NOTES = "notes";
        String PIN = "pin";
        String STACK_ID = "stackID";
        String APIVERSION = "apiVersion";
    }

    interface TickerMapKeys {
        String APP_LINK = "applink";
        String TOKOPEDIA = "tokopedia";
        String CONTENT = "content";
        String URL = "url";
    }

    interface CouponMapKeys {
        String TITLE = "title";
        String SUB_TITLE = "subTitle";
    }

    interface BaseUrl {
        String WEB_DOMAIN_MOBILE = "https://m.tokopedia.com/";
    }

    interface WebLink {
        String MEMBERSHIP = WEB_DOMAIN_MOBILE + "rewards/membership";
        String INFO = WEB_DOMAIN_MOBILE + "rewards/intro";
        String DETAIL = WEB_DOMAIN_MOBILE + "rewards/kupon/detail";
    }

    interface SectionLayoutType {
        String CATEGORY = "category";
        String TICKER = "ticker";
        String COUPON = "coupon";
        String CATALOG = "catalog";
        String BANNER = "banner";
        String TOPADS = "topads";
        String TOPHEADER = "topheader";
    }

    interface BannerType {
        String BANNER_1_1 = "full_width_1:1";
        String BANNER_2_1 = "full_width_2:1";
        String BANNER_3_1 = "full_width_3:1";
        String CAROUSEL_1_1 = "carousel_1:1";
        String CAROUSEL_2_1 = "carousel_2:1";
        String CAROUSEL_3_1 = "carousel_3:1";
        String COLUMN_3_1_BY_1 = "3_column_1:1";
        String COLUMN_2_1_BY_1 = "2_column_1:1";
        String COLUMN_2_3_BY_4 = "2_column_3:4";
    }

    interface GQLQuery {
        String TP_GQL_CURRENT_POINTS = "tp_gql_current_points";
        String TP_GQL_TOKOPOINT_APPLY_COUPON = "tp_gql_tokopoint_apply_coupon";
        String TP_GQL_COUPON_DETAIL = "tp_gql_coupon_detail";
        String TP_GQL_REFETCH_REAL_CODE = "tp_gql_refetch_real_code";
        String TP_GQL_SWIPE_COUPON = "tp_gql_swipe_coupon";
        String TP_GQL_COUPON_FILTER = "tp_gql_coupon_filter";
        String TP_GQL_COUPON_LISTING_STACK = "tp_gql_coupon_listing_stack";
        String TP_GQL_COUPON_IN_STACK = "tp_gql_coupon_in_stack";
        String TP_GQL_USER_INFO = "tp_gql_user_info";
        String TP_GQL_TOKOPOINT_REDEEM_COUPON = "tp_gql_tokopoint_redeem_coupon";
        String TP_GQL_CATLOG_STATUS = "tp_gql_catalog_status";
        String TP_GQL_TOKOPOINT_VALIDATE_REDEEM = "tp_gql_tokopoint_validate_redeem";
        String TP_GQL_PRE_VALIDATE_REDEEM = "tp_gql_pre_validate_redeem";
        String TP_GQL_CATALOG_DETAIL = "tp_gql_catalog_detail";
        String TP_GQL_TOKOPOINT_DETAIL = "tp_gql_tokopoint_detail";
        String TP_GQL_CATALOG_FILTER = "tp_gql_catalog_filter";
        String TP_GQL_LUCKY_EGG_DETAILS = "tp_gql_lucky_egg_detail";
        String TP_GQL_TOKOPOINT_TOP_SECTION_NEW = "tp_gql_topsection_new";
        String TP_GQL_HOME_PAGE_SECTION = "tp_gql_home_page_section";
        String TP_GQL_REWARD_INTRO = "tp_gql_reward_intro";
    }
}
