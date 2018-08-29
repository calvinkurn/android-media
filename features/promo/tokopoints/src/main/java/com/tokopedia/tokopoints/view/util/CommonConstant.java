package com.tokopedia.tokopoints.view.util;

import static com.tokopedia.tokopoints.view.util.CommonConstant.BaseUrl.WEB_DOMAIN_MOBILE;

public interface CommonConstant {
    String PREF_TOKOPOINTS = "pref_tokopoints";
    String PREF_KEY_ON_BOARDED = "pref_key_on_boarded";
    String ARGS_CATEGORY_ID = "category_id";
    String ARGS_SORT_TYPE = "sort_type";
    int DEVICE_ID_BANNER = 256;
    int PAGE_SIZE = 100;
    int HOMEPAGE_PAGE_SIZE = 5;
    int TAB_SETUP_DELAY_MS = 150;
    int HOMEPAGE_TAB_COUNT = 2;
    int DEFAULT_SORT_TYPE = 1;
    int DEFAULT_CATEGORY_TYPE = 0;
    int DEFAULT_AUTO_REFRESH_S = 10000;
    int CATALOG_TYPE_FLASH_SALE = 3;
    int MY_COUPON_TAB = 1;
    int MAX_COUPON_TO_SHOW_COUNT = 9;
    String TOKOPOINTS_CATALOG_STATUS_AUTO_REFRESH_S = "tokopoints_catalog_status_auto_refresh_s";
    String EXTRA_COUPON_COUNT = "extra_coupon_count";

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
        String SEE_COUPON = WEB_DOMAIN_MOBILE + "tokopoints/mobile/kupon-saya/";
        String LUCKY_EGG_PAGE = WEB_DOMAIN_MOBILE + "tokopoints/hadiah";
        String INFO = WEB_DOMAIN_MOBILE + "tokopoints/info";
        String EXCHANGE_COUPON = WEB_DOMAIN_MOBILE + "tokopoints/mobile/tukar-point";
        String COUPON_DETAIL = WEB_DOMAIN_MOBILE + "tokopoints/mobile/detail/";
    }
}
