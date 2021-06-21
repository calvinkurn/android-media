package com.tokopedia.home_account


/**
 * @author okasurya on 7/20/18.
 */
object AccountConstants {

    object Analytics {
        const val CLICK = "click"
        const val USER = "user"
        const val SETTING = "setting"
        const val SHOP = "shop"
        const val ACCOUNT = "account"
        const val PAYMENT_METHOD = "payment method"
        const val TERM_CONDITION = "terms & condition"
        const val ABOUT_US = "about us"
        const val PRIVACY_POLICY = "privacy policy"
        const val APPLICATION_REVIEW = "application review"
        const val DEVELOPER_OPTIONS = "developer options"
        const val LOGOUT = "logout"
        const val PERSONAL_DATA = "personal data"
        const val ADDRESS_LIST = "address list"
        const val ACCOUNT_BANK = "account bank"
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val EVENT_BUSINESS_UNIT = "businessUnit"
        const val EVENT_CURRENT_SITE = "currentSite"
        const val EVENT_USER_ID = "userId"
        const val ECOMMERCE = "ecommerce"
        const val CURRENCY_CODE = "currencyCode"
        const val IDR = "IDR"
        const val IMPRESSIONS = "impressions"
        const val DATA_NAME = "name"
        const val DATA_ID = "id"
        const val DATA_PRICE = "price"
        const val DATA_BRAND = "brand"
        const val DATA_CATEGORY = "category"
        const val DATA_VARIAN = "variant"
        const val LIST = "list"
        const val DATA_POSITION = "position"
        const val NONE_OTHER = "none/other"
        const val ACTION_FIELD = "actionField"
        const val PRODUCTS = "products"
        const val DATA_ATTRIBUTION = "attribution"
        const val VALUE_PRODUCT_RECOMMENDATION_LIST = "/user - rekomendasi untuk anda - %s"
        const val VALUE_PRODUCT_TOPADS = " - product topads"
        const val VALUE_WISHLIST_PRODUCT = "%s - wishlist on product recommendation"
        const val DATA_DIMENSION_83 = "dimension83"
        const val VALUE_BEBAS_ONGKIR = "bebas ongkir"

        object Event {
            const val EVENT_CLICK_HOME_PAGE = "clickHomePage"
            const val EVENT_CLICK_ACCOUNT = "clickAccount"
            const val EVENT_PRODUCT_CLICK = "productClick"
            const val EVENT_PRODUCT_VIEW = "productView"
            const val EVENT_CLICK_SETTING = "clickSetting"
        }

        object Category {
            const val CATEGORY_HOMEPAGE = "homepage"
            const val CATEGORY_ACCOUNT_PAGE_BUYER = "account page - buyer"
            const val CATEGORY_ACCOUNT_BUYER = "akun saya pembeli"
            const val CATEGORY_OVO_HOMEPAGE = "ovo home page"
            const val CATEGORY_SETTING_PAGE = "setting page"
        }

        object Action {
            const val ACTION_CLICK_TOGGLE_ON_GEOLOCATION = "click toggle on geolocation"
            const val ACTION_CLICK_PRODUCT_RECOMMENDATION = "click - product recommendation"
            const val ACTION_IMPRESSION_PRODUCT_RECOMMENDATION = "impression - product recommendation"
            const val ACTION_CLICK_PROFILE = "click - profile page - click profile"
            const val ACTION_VIEW_OVO_HOMEPAGE = "view ovo homepage"
            const val ACTION_CLICK_BALANCE = "click - tokopedia pay - saldo"
            const val ACTION_CLICK_TOKOPOINTS = "click - tokopedia pay - tokopoints"
            const val ACTION_CLICK_ON_MORE_OPTION = "click on more option"
            const val ACTION_CLICK_REWARD_SECTION = "click reward section"
            const val ACTION_CLICK_ACCOUNT_SETTING_SECTION = "click account settings section"
            const val ACTION_CLICK_APP_SETTING_SECTION = "click on application setting section"
            const val ACTION_CLICK_ABOUT_TOKOPEDIA_SECTION = "click on seputar tokopedia section"
            const val ACTION_CLICK_LOGOUT = "click on logout"
            const val ACTION_SIMPAN_THEME_SELECTION = "click simpan on theme selection"
        }

        object Label {
            const val LABEL_EMPTY = ""
            const val LABEL_MEMBER = "Member"
            const val LABEL_MEMBER_STORE = "TokoMember"
            const val LABEL_TOP_QUEST = "TopQuest"
            const val LABEL_MY_COUPON = "Kupon Saya"
            const val LABEL_LIST_ADDRESS = "Daftar Alamat"
            const val LABEL_BANK_ACCOUNT = "Rekening Bank"
            const val LABEL_INSTANT_PAYMENT = "Pembayaran Instan"
            const val LABEL_INSTANT_BUY = "Beli Langsung"
            const val LABEL_ACCOUNT_SECURITY = "Keamanan Akun"
            const val LABEL_NOTIFICATION = "Notifikasi"
            const val LABEL_APP_SETTING = "Pengaturan Applikasi"
            const val LABEL_ENABLE = "enable"
            const val LABEL_DISABLE = "disable"
            const val LABEL_SHAKE = "Shake shake"
            const val LABEL_GEOLOCATION = "Geolokasi"
            const val LABEL_SAFE_MODE = "Safe Mode"
            const val LABEL_STICKER_TOKOPEDIA = "Sticker Tokopedia"
            const val LABEL_IMAGE_QUALITY = "Kualitas Gambar"
            const val LABEL_CLEAN_CACHE = "Bersihkan Cache"
            const val LABEL_VIEW_MODE = "Mode Tampilan"
            const val LABEL_ABOUT_TOKOPEDIA = "Seputar Tokopedia"
            const val LABEL_GET_TO_KNOW_TOKOPEDIA = "Kenali Tokopedia"
            const val LABEL_TERMS_AND_CONDITIONS = "Syarat dan Ketentuan"
            const val LABEL_PRIVACY_POLICY = "Kebijakan Privasi"
            const val LABEL_IP = "Hak Kekayaan Intelektual"
            const val LABEL_REVIEW_THIS_APP = "Ulas Aplikasi Ini"
        }

        object BusinessUnit {
            const val USER_PLATFORM_UNIT = "user platform"
        }

        object CurrentSite {
            const val TOKOPEDIA_MARKETPLACE_SITE = "tokopediamarketplace"
        }
    }

    interface ErrorCodes {
        companion object {
            const val ERROR_CODE_BUYER_ACCOUNT = "ACB001"
        }
    }

    object Query {
        const val NEW_QUERY_BUYER_ACCOUNT_HOME = "query_account_buyer"
        const val QUERY_USER_REWARDSHORCUT = "query_account_shortcut"
        const val QUERY_GET_BALANCE = "query_get_balance"
        const val QUERY_GET_USER_ASSET_CONFIG = "query_get_user_asset_config"
        const val QUERY_TOKOPOINTS_DRAWER_LIST = "query_tokopoints_drawerr_list"
    }

    object Url {
        const val BASE_MOBILE = "https://m.tokopedia.com/"
        const val PATH_TERM_CONDITION = "terms.pl?isBack=true"
        const val PATH_IP = "intellectual-property-protection"
        const val PATH_PRIVACY_POLICY = "privacy.pl?isBack=true"
        const val BASE_WEBVIEW_APPLINK = "tokopedia://webview?url="
        const val PATH_ABOUT_US = "about"
        const val OVO_IMG = "/img/android/ovo/drawable-xxxhdpi/ovo.png"
        const val KEY_IMAGE_HOST = "image_host"
        const val CDN_URL = "https://ecs7.tokopedia.net"
        const val PLAYSTORE_URL = "https://play.google.com/store/apps/details?id="
        const val TOKOPOINTS_ICON = "https://images.tokopedia.net/img/img/phoenix/tokopoints.png"
        const val SALDO_ICON = "https://images.tokopedia.net/img/android/user/emoney/saldo.png"
        const val OVO_ICON = "https://images.tokopedia.net/img/android/user/emoney/ovo.png"
    }

    object SettingCode {
        const val SETTING_TKPD_PAY_ID = 3
        const val SETTING_TNC_ID = 5
        const val SETTING_PRIVACY_ID = 6
        const val SETTING_IP = 7
        const val SETTING_OUT_ID = 8
        const val SETTING_SHAKE_ID = 9
        const val SETTING_GEOLOCATION_ID = 98
        const val SETTING_SAFE_SEARCH_ID = 99
        const val SETTING_ACCOUNT_PERSONAL_DATA_ID = 11
        const val SETTING_ACCOUNT_ADDRESS_ID = 13
        const val SETTING_BANK_ACCOUNT_ID = 33
        const val SETTING_FEEDBACK_FORM = 37
        const val SETTING_DEV_OPTIONS = -1
        const val SETTING_APP_REVIEW_ID = 10
        const val SETTING_APP_ADVANCED_CLEAR_CACHE = 20
        const val SETTING_ABOUT_US = 36
        const val SETTING_NOTIFICATION = 38
        const val SETTING_SECURITY = 39
        const val SETTING_OLD_ACCOUNT = 40
        const val SETTING_OVO = 41
        const val SETTING_SALDO = 42
        const val SETTING_MORE_MEMBER = 43
        const val SETTING_INSTANT_PAYMENT = 44
        const val SETTING_INSTANT_BUY = 45
        const val SETTING_APP_SETTING = 46
        const val SETTING_ABOUT_TOKOPEDIA = 47
        const val SETTING_TOKOPOINTS = 48
        const val SETTING_QUALITY_SETTING = 49
        const val SETTING_DARK_MODE = 50
    }

    object KEY {
        const val KEY_PREF_SHAKE = "notification_shake_shake"
        const val KEY_PREF_SAFE_SEARCH = "notification_safe_mode"
        const val KEY_SHOW_COACHMARK = "home_account_coachmark"
        const val CLEAR_CACHE = "clear cache"
    }

    object REQUEST {
        const val REQUEST_ADD_PASSWORD = 100
    }

    object LAYOUT {
        const val TYPE_ERROR = -1
    }
}