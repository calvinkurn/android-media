package com.tokopedia.tokopoints.view.util;

import static com.tokopedia.tokopoints.view.util.CommonConstant.BaseUrl.WEB_DOMAIN_MOBILE;

public interface CommonConstant {
    String PREF_TOKOPOINTS = "pref_tokopoints";
    String PREF_KEY_ON_BOARDED = "pref_key_on_boarded";
    String ARGS_CATEGORY_ID = "category_id";
    String ARGS_SUB_CATEGORY_ID = "sub_category_id";
    String ARGS_SLUG_CATEGORY = "slug_category";
    String ARGS_SLUG_SUB_CATEGORY = "slug_sub_category";
    String ARGS_SORT_TYPE = "sort_type";
    int DEVICE_ID_BANNER = 256;
    int PAGE_SIZE = 100;
    int HOMEPAGE_PAGE_SIZE = 10;
    int TAB_SETUP_DELAY_MS = 150;
    int HOMEPAGE_TAB_COUNT = 2;
    int DEFAULT_SORT_TYPE = 1;
    int DEFAULT_CATEGORY_TYPE = 1;
    int DEFAULT_AUTO_REFRESH_S = 10000;
    int CATALOG_TYPE_FLASH_SALE = 3;
    int MY_COUPON_TAB = 1;
    int PIN_COUNT = 4;
    String TOKOPOINTS_CATALOG_STATUS_AUTO_REFRESH_S = "tokopoints_catalog_status_auto_refresh_s";
    String EXTRA_COUPON_COUNT = "extra_coupon_count";
    String EXTRA_COUPON_CODE = "coupon_code";
    String EXTRA_CATALOG_CODE = "catalog_code";
    String EXTRA_COUPON_ID = "extra_coupon_id";
    String EXTRA_PIN_INFO = "extra_pin_info";
    String EXTRA_COUPON_TITLE = "extra_coupon_title";
    String EXTRA_COUPON_POINT = "extra_coupon_point";
    String EXTRA_SLUG = "slug";
    String CLIPBOARD_COUPON_CODE = "tokopedia_tokopoints_coupon";
    int MAX_COUPON_RE_FETCH_COUNT = 3;
    int COUPON_RE_FETCH_DELAY_S = 5;
    long COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S = 86400;
    int UI_SETTLING_DELAY_MS = 250;
    int UI_SETTLING_DELAY_MS2 = 500;
    String ARGS_POINTS_AVAILABILITY = "points_availability";

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
        String MEMBERSHIP = WEB_DOMAIN_MOBILE + "tokopoints/mobile/membership";
        String HISTORY = WEB_DOMAIN_MOBILE + "tokopoints/mobile/history";
        String INFO = WEB_DOMAIN_MOBILE + "tokopoints/info";
    }

    interface SectionLayoutType {
        String CATEGORY = "category";
        String TICKER = "ticker";
        String TAB = "tab";
        String COUPON = "coupon";
        String CATALOG = "catalog";
        String BANNER = "banner";
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
}
