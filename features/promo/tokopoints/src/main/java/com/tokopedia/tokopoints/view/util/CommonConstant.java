package com.tokopedia.tokopoints.view.util;

import static com.tokopedia.tokopoints.view.util.CommonConstant.BaseUrl.WEB_DOMAIN_MOBILE;

public interface CommonConstant {
    String PREF_TOKOPOINTS = "pref_tokopoints";
    String PREF_KEY_ON_BOARDED = "pref_key_on_boarded";
    String ARGS_CATEGORY_ID = "category_id";
    String ARGS_SORT_TYPE = "sort_type";
    int DEVICE_ID_BANNER = 256;
    int PAGE_SIZE = 5;
    int HOMEPAGE_PAGE_SIZE = 5;
    int TAB_SETUP_DELAY_MS = 150;
    int HOMEPAGE_TAB_COUNT = 2;
    int DEFAULT_SORT_TYPE = 1;
    int DEFAULT_CATEGORY_TYPE = 0;
    int DEFAULT_AUTO_REFRESH_S = 10000;
    int CATALOG_TYPE_FLASH_SALE = 3;
    int MY_COUPON_TAB = 1;
    int MAX_COUPON_TO_SHOW_COUNT = 9;
    int PIN_COUNT = 4;
    String TOKOPOINTS_CATALOG_STATUS_AUTO_REFRESH_S = "tokopoints_catalog_status_auto_refresh_s";
    String EXTRA_COUPON_COUNT = "extra_coupon_count";
    String EXTRA_COUPON_CODE = "coupon_code";
    String EXTRA_CATALOG_CODE = "catalog_code";
    String EXTRA_COUPON_ID = "extra_coupon_id";
    String EXTRA_PIN_INFO = "extra_pin_info";
    String EXTRA_COUPON_TITLE = "extra_coupon_title";
    String EXTRA_COUPON_POINT = "extra_coupon_point";
    String CLIPBOARD_COUPON_CODE = "tokopedia_tokopoints_coupon";
    int MAX_COUPON_RE_FETCH_COUNT = 3;
    int COUPON_RE_FETCH_DELAY_S = 5;
    long COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S = 86400;
    int UI_SETTLING_DELAY_MS = 250;

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
        String POINTS_RANGE = "pointRange";
        String CATALOG_ID = "catalog_id";
        String IS_GIFT = "is_gift";
        String PROMO_CODE = "promoCode";
        String DEVICE = "device";
        String SERVICE_ID = "serviceID";
        String CATEGORY_ID_COUPON = "categoryIDCoupon";
        String SLUG = "slug";
        String CATALOG_IDS = "catalogIDs";
        String CODE = "code";
        String GIFT_EMAIL = "gift_email";
        String NOTES = "notes";
        String PIN = "pin";
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

    class BaseUrl {
        static String WEB_DOMAIN_MOBILE = "https://m.tokopedia.com/";
    }

    interface WebLink {
        String MEMBERSHIP = WEB_DOMAIN_MOBILE + "tokopoints/mobile/membership";
        String HISTORY = WEB_DOMAIN_MOBILE + "tokopoints/mobile/history";
        String INFO = WEB_DOMAIN_MOBILE + "tokopoints/info";
    }
}
