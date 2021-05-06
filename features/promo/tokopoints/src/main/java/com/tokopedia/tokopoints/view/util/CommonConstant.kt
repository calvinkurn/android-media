package com.tokopedia.tokopoints.view.util

interface CommonConstant {
    interface CouponRedemptionCode {
        companion object {
            const val LOW_POINT = 42020
            const val PROFILE_INCOMPLETE = 42021
            const val QUOTA_LIMIT_REACHED = 42022
            const val SUCCESS = 200
        }
    }

    interface GraphqlVariableKeys {
        companion object {
            const val PAGE = "page"
            const val PAGE_SIZE = "limit"
            const val SORT_ID = "sortID"
            const val CATEGORY_ID = "categoryID"
            const val SUB_CATEGORY_ID = "subCategoryID"
            const val POINTS_RANGE = "pointRange"
            const val CATALOG_ID = "catalog_id"
            const val IS_GIFT = "is_gift"
            const val PROMO_CODE = "promoCode"
            const val DEVICE = "device"
            const val SERVICE_ID = "serviceID"
            const val CATEGORY_ID_COUPON = "categoryIDCoupon"
            const val SLUG = "slug"
            const val SLUG_CATEGORY = "slugCategory"
            const val SLUG_SUB_CATEGORY = "slugSubCategory"
            const val CATALOG_IDS = "catalogIDs"
            const val CODE = "code"
            const val GIFT_EMAIL = "gift_email"
            const val NOTES = "notes"
            const val PIN = "pin"
            const val STACK_ID = "stackID"
            const val APIVERSION = "apiVersion"
            const val SAVING_YEAR = "year"
            const val SAVING_MONTH = "month"
            const val SAVING_TYPE = "type"
        }
    }

    interface TickerMapKeys {
        companion object {
            const val APP_LINK = "applink"
            const val TOKOPEDIA = "tokopedia"
            const val CONTENT = "content"
            const val URL = "url"
        }
    }

    interface CouponMapKeys {
        companion object {
            const val TITLE = "title"
            const val SUB_TITLE = "subTitle"
        }
    }

    interface BaseUrl {
        companion object {
            const val WEB_DOMAIN_MOBILE = "https://m.tokopedia.com/"
        }
    }

    interface WebLink {
        companion object {
            const val MEMBERSHIP = BaseUrl.WEB_DOMAIN_MOBILE + "rewards/membership"
            const val INFO = BaseUrl.WEB_DOMAIN_MOBILE + "rewards/intro"
            const val DETAIL = BaseUrl.WEB_DOMAIN_MOBILE + "rewards/kupon/detail"
            const val USERSAVING = "tokopedia://webview?url=https://m.tokopedia.com/rewards/saving"
        }
    }

    interface SectionLayoutType {
        companion object {
            const val CATEGORY = "category"
            const val TICKER = "ticker"
            const val COUPON = "coupon"
            const val CATALOG = "catalog"
            const val BANNER = "banner"
            const val TOPADS = "topads"
            const val TOPHEADER = "topheader"
            const val RECOMM = "recomm"

        }
    }

    interface BannerType {
        companion object {
            const val BANNER_1_1 = "full_width_1:1"
            const val BANNER_2_1 = "full_width_2:1"
            const val BANNER_3_1 = "full_width_3:1"
            const val CAROUSEL_1_1 = "carousel_1:1"
            const val CAROUSEL_2_1 = "carousel_2:1"
            const val CAROUSEL_3_1 = "carousel_3:1"
            const val COLUMN_3_1_BY_1 = "3_column_1:1"
            const val COLUMN_2_1_BY_1 = "2_column_1:1"
            const val COLUMN_2_3_BY_4 = "2_column_3:4"
        }
    }

    interface GQLQuery {
        companion object {
            const val TP_GQL_CURRENT_POINTS = "tp_gql_current_points"
            const val TP_GQL_TOKOPOINT_APPLY_COUPON = "tp_gql_tokopoint_apply_coupon"
            const val TP_GQL_COUPON_DETAIL = "tp_gql_coupon_detail"
            const val TP_GQL_REFETCH_REAL_CODE = "tp_gql_refetch_real_code"
            const val TP_GQL_SWIPE_COUPON = "tp_gql_swipe_coupon"
            const val TP_GQL_COUPON_FILTER = "tp_gql_coupon_filter"
            const val TP_GQL_COUPON_LISTING_STACK = "tp_gql_coupon_listing_stack"
            const val TP_GQL_COUPON_IN_STACK = "tp_gql_coupon_in_stack"
            const val TP_GQL_USER_INFO = "tp_gql_user_info"
            const val TP_GQL_TOKOPOINT_REDEEM_COUPON = "tp_gql_tokopoint_redeem_coupon"
            const val TP_GQL_CATLOG_STATUS = "tp_gql_catalog_status"
            const val TP_GQL_TOKOPOINT_VALIDATE_REDEEM = "tp_gql_tokopoint_validate_redeem"
            const val TP_GQL_PRE_VALIDATE_REDEEM = "tp_gql_pre_validate_redeem"
            const val TP_GQL_CATALOG_DETAIL = "tp_gql_catalog_detail"
            const val TP_GQL_TOKOPOINT_DETAIL = "tp_gql_tokopoint_detail"
            const val TP_GQL_CATALOG_FILTER = "tp_gql_catalog_filter"
            const val TP_GQL_LUCKY_EGG_DETAILS = "tp_gql_lucky_egg_detail"
            const val TP_GQL_TOKOPOINT_TOP_SECTION_NEW = "tp_gql_topsection_new"
            const val TP_GQL_HOME_PAGE_SECTION = "tp_gql_home_page_section"
            const val TP_GQL_REWARD_INTRO = "tp_gql_reward_intro"
            const val TP_GQL_CATALOG_LIST = "tp_gql_catalog_listing"
            const val TP_GQL_REWARD_USESAVING = "tp_gql_usersaving"
        }
    }

    companion object {
        const val UTF_ENCODING = "UTF-8"
        const val COUPON_MIME_TYPE = "text/html"
        const val FRAGMENT_DETAIL_TOKOPOINT = "sendFragment"
        const val PREF_TOKOPOINTS = "pref_tokopoints"
        const val PREF_KEY_ON_BOARDED = "pref_key_on_boarded"
        const val ARGS_CATEGORY_ID = "category_id"
        const val ARGS_SUB_CATEGORY_ID = "sub_category_id"
        const val ARGS_SLUG_CATEGORY = "slug_category"
        const val ARGS_SLUG_SUB_CATEGORY = "slug_sub_category"
        const val ARGS_SORT_TYPE = "sort_type"
        const val DEVICE_ID_BANNER = 256
        const val PAGE_SIZE = 10
        const val HOMEPAGE_PAGE_SIZE = 10
        const val TAB_SETUP_DELAY_MS = 150
        const val HOMEPAGE_TAB_COUNT = 1
        const val DEFAULT_SORT_TYPE = 1
        const val DEFAULT_CATEGORY_TYPE = 1
        const val DEFAULT_AUTO_REFRESH_S = 10000
        const val CATALOG_TYPE_FLASH_SALE = 3
        const val MY_COUPON_TAB = 1
        const val PIN_COUNT = 4
        const val TOKOPOINTS_CATALOG_STATUS_AUTO_REFRESH_S = "tokopoints_catalog_status_auto_refresh_s"
        const val TOKOPOINTS_NEW_HOME = "tokopoints_homepage_new_ui"
        const val TOKOPOINTS_NEW_COUPON_LISTING = "tokopoints_coupon_stack_new_ui"
        const val EXTRA_COUPON_COUNT = "extra_coupon_count"
        const val EXTRA_COUPON_CODE = "coupon_code"
        const val EXTRA_CATALOG_CODE = "catalog_code"
        const val EXTRA_COUPON_ID = "extra_coupon_id"
        const val EXTRA_PIN_INFO = "extra_pin_info"
        const val EXTRA_COUPON_TITLE = "extra_coupon_title"
        const val EXTRA_COUPON_BANNER = "extra_coupon_banner"
        const val EXTRA_COUPON_POINT = "extra_coupon_point"
        const val EXTRA_SLUG = "slug"
        const val CLIPBOARD_COUPON_CODE = "tokopedia_tokopoints_coupon"
        const val MAX_COUPON_RE_FETCH_COUNT = 3
        const val COUPON_RE_FETCH_DELAY_S = 5
        const val COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S: Long = 86400
        const val UI_SETTLING_DELAY_MS = 250
        const val UI_SETTLING_DELAY_MS2 = 500
        const val ARGS_POINTS_AVAILABILITY = "points_availability"
        const val BUNDLE_ARGS_USER_IS_LOGGED_IN = "USER_IS_LOGGEDIN"
        const val APIVERSION = "2.0.0"
        const val IMPRESSION_LIST = "/tokopoints - p{x} - promo list"
        const val TIMER_RED_BACKGROUND_HEX = "EF144A"
        const val HASH = "#"
        const val USERSAVING_COLORSTR= "color:"
        const val CATALOG_CLAIM_MESSAGE = "CATALOG_CLAIM_MESSAGE"
    }
}