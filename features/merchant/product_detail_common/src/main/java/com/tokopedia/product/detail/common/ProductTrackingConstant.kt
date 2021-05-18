package com.tokopedia.product.detail.common

object ProductTrackingConstant {

    object Category {
        const val PDP = "product detail page"
        const val PDP_AFTER_ATC = "product detail page after atc"
        const val TOP_NAV_SEARCH_PDP = "top nav - search - product detail page"
        const val TOP_NAV_SHARE_PDP = "top nav - product detail page"
        const val PRODUCT_DETAIL_PAGE_SELLER = "product detail page - seller side"
        const val PRODUCT_DETAIL_PAGE_SHIPPING = "pdp courier section"
    }

    object Tracking {
        const val KEY_SHOP_ID_SELLER = "shopId"
        const val KEY_SHOP_TYPE = "shopType"
        const val KEY_PAGE_TYPE = "pageType"
        const val KEY_PRODUCT_ID_ = "productId"
        const val KEY_EVENT = "event"
        const val KEY_CATEGORY = "eventCategory"
        const val KEY_ACTION = "eventAction"
        const val KEY_LABEL = "eventLabel"
        const val KEY_ECOMMERCE = "ecommerce"
        const val KEY_PROMOTIONS = "promotions"
        const val KEY_USER_ID = "user_id"
        const val KEY_PROMO_ID = "promoId"
        const val USER_NON_LOGIN = "non login"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val KEY_SCREEN_NAME = "screenName"
        const val KEY_TICKER_TYPE = "ticker type"
        const val KEY_GROUP_NAME = "productGroupName"
        const val KEY_GROUP_ID = "productGroupId"
        const val KEY_CATEGORY_ID = "categoryId"

        const val KEY_USER_ID_VARIANT = "userId"

        const val PRODUCT_DETAIL_SCREEN_NAME = "/product"

        const val ID = "id"
        const val NAME = "name"
        const val PROMO_NAME = "name"
        const val PROMO_POSITION = "position"
        const val PROMO_ID = "promo_id"
        const val PROMO_CODE = "promo_id"
        const val CREATIVE = "creative"
        const val POSITION = "position"
        const val PROMO_CLICK = "promoClick"
        const val PROMO_VIEW = "promoView"

        const val ACTION_FIELD = "actionField"
        const val LIST = "list"
        const val PRODUCTS = "products"
        const val IMPRESSIONS = "impressions"
        const val PRICE = "price"
        const val BRAND = "brand"
        const val DEFAULT_VALUE = "none / other"
        const val VARIANT = "variant"
        const val QUANTITY = "quantity"
        const val CATEGORY = "category"
        const val LIST_DEFAULT = "/product - "
        const val LIST_RECOMMENDATION = " - rekomendasi untuk anda - "
        const val LIST_PRODUCT_AFTER_ATC = "/productafteratc  - "
        const val CURRENCY_CODE = "currencyCode"
        const val CURRENCY_DEFAULT_VALUE = "IDR"
        const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        const val VALUE_BEBAS_ONGKIR_EXTRA = "bebas ongkir extra"
        const val VALUE_NONE_OTHER = "none / other"
        const val VALUE_FALSE = "false"
        const val KEY_PRODUCT_ID = "productId"
        const val KEY_LAYOUT = "layout"
        const val KEY_COMPONENT = "component"
        const val KEY_ISLOGGIN = "isLoggedInStatus"
        const val KEY_ADD = "add"
        const val BUSINESS_UNIT = "physical goods"
        const val BUSINESS_UNIT_PDP = "product detail page"
        const val CURRENT_SITE = "tokopediamarketplace"
        const val CURRENT_SITE_FINTECH = "tokopediafintechinsurance"
        const val KEY_FINTECH = "fintech"
        const val KEY_INSURANCE = "insurance"
        const val KEY_DISCUSSION_USER_ID = "userId"
        const val SWIPE_IMAGE_BUSINESS_UNIT = "Physical Goods - PDP"

        //Tradein
        const val TRADEIN_TRUE_DIAGNOSTIC = "true diagnostic"
        const val TRADEIN_TRUE_NON_DIAGNOSTIC = "true non diagnostic"

        const val KEY_DIMENSION_81 = "dimension81"
        const val KEY_DIMENSION_83 = "dimension83"
        const val KEY_DIMENSION_54 = "dimension54"
        const val KEY_DIMENSION_38 = "dimension38"
        const val KEY_DIMENSION_40 = "dimension40"
        const val KEY_DIMENSION_82 = "dimension82"
        const val KEY_DIMENSION_80 = "dimension80"
        const val KEY_DIMENSION_79 = "dimension79"
        const val KEY_DIMENSION_45 = "dimension45"

        const val CONTENT_TYPE = "product"
        const val BRANCH_QUANTITY ="1"
    }
    object Action {
        const val CLICK = "click"
        const val ADD = "add"
        const val CLICK_CART_BUTTON_VARIANT = "click - cart button on sticky header"
        const val PRODUCT_CLICK = "productClick"
        const val PRODUCT_VIEW = "productView"
        const val RECOMMENDATION_CLICK = "clickRecommendation"
        const val TOPADS_CLICK = "click - product recommendation"
        const val TOPADS_ATC_CLICK = "click add to cart on product card after atc"
        const val TOPADS_IMPRESSION = "impression - product recommendation"
        const val CLICK_BY_ME = "click - by.me"
        const val CLICK_VARIANT = "click - variant"
        const val CLICK_RATE_ESTIMATE = "click - estimasi ongkir"
        const val CLICK_PRODUCT_PICTURE = "click - product picture"
        const val SWIPE_PRODUCT_PICTURE = "click - swipe product picture"
        const val ACTION_WISHLIST_ON_PRODUCT_RECOMMENDATION = " - wishlist on product recommendation"
        const val CLICK_APPLY_LEASING = "click - ajukan kredit"
        const val VIEW_HELP_POP_UP_WHEN_ATC = "view help pop up when atc"
        const val CLICK_REPORT_ON_HELP_POP_UP_ATC = "click report on help pop up atc"
        const val CLICK_CLOSE_ON_HELP_POP_UP_ATC = "click close on help pop up atc"
        const val CLICK_SEARCH_BOX = "click search box"
        const val CLICK_RIBBON_TRADE_IN = "click - ribbon trade in"
        const val CLICK_SEE_MORE_WIDGET = "click - see more on widget %s"
        const val CLICK_ANNOTATION_RECOM_CHIP = "click annotation chips"
        const val CLICK_SHARE_PDP = "click - share button"
        const val CLICK_READ_MORE = "click - baca selengkapnya"
        const val CLICK_CATEGORY = "click - category on informasi produk"
        const val CLICK_ETALASE = "click - etalase on informasi produk"
        const val CLICK_LIHAT_SEMUA_ON_SIMULASI_CICILAN = "click - info cicilan"
        const val CLICK_PAGE_CHAT = "click - page chat"
        const val CLICK_BUTTON_CHAT = "click - sticky chat"
        const val ADD_WISHLIST = "add wishlist"
        const val REMOVE_WISHLIST = "remove wishlist"
        const val ADD_WISHLIST_NON_LOGIN = "add wishlist - non logged in"
        const val CLICK_SHOP_PAGE = "click - shop page link"
        const val CLICK_IMAGE_MOST_HELPFULL_REVIEW = "click -review gallery on most helpful review"
        const val CLICK_SEE_ALL_MERCHANT_VOUCHER = "click - merchant voucher - see all"
        const val CLICK_DETAIL_MERCHANT_VOUCHER = "click - merchant voucher - mvc detail"
        const val CLICK_USE_MERCHANT_VOUCHER = "click - merchant voucher - use voucher"
        const val IMPRESSION_USE_MERCHANT_VOUCHER = "impression - merchant voucher - use voucher"
        const val CLICK_TRADEIN = "click trade in widget"
        const val CLICK_UNFOLLOW = "click - unfollow shop"
        const val CLICK_FOLLOW = "click - follow shop"
        const val CLICK_EDIT_PRODUCT = "click - edit product button"
        const val CLICK_CHOOSE_PRODUCT_VARIANT = "click - choose product variant"
        const val CLICK_VARIANT_GUIDELINE = "click - lihat panduan on size chart"
        const val IMPRESSION_CHOOSE_VARIANT_NOTIFICATION = "impression - choose variant notification"
        const val CLICK_ATC_NON_LOGIN = "click - tambah ke keranjang on pdp - non login"
        const val CLICK_WHOLESALE = "click - info harga grosir"
        const val CLICK_SHOP_INFO_MINI = "click - shop info mini"
        const val CLICK_TICKER = "click - selengkapnya on component ticker"
        const val CLICK_CUSTOM_INFO = "click - custom info component"
        const val CLICK_NPL_FOLLOWERS = "click - follow shop on bottom sheet"

        const val CLICK_REQUEST_PERMISSION_IMEI = "click - beli then show popup"
        const val CLICK_ACCEPT_PERMISSION = "click -  berikan akses on bottomsheet"
        const val CLICK_GO_TO_SETTING = "click - ke pengaturan hp on popup"
        const val CLICK_PERMISSION_LATER = "click - nanti saja on popup"
        const val CLICK_NOTIFY_ME = "click - ingatkan saya on pdp campaign"
        const val CLICK_DISKUSI_PRODUCT_TAB = "click - diskusi produk tab"

        const val CLICK_BUY_ACTIVATION_OVO = "click - beli then go to bottomsheet ovo activation"
        const val CLICK_SEE_BOTTOMSHEET_OVO = "click - beli then show bottomsheet"
        const val CLICK_TOPUP_BOTTOMSHEET_OVO = "on bottomsheet"
        const val CLICK_PP_INSURANCE_BOTTOMSHEET = "click - pp - insurance section"

        const val ACTION_VIEW_ERROR_WHEN_ADD_TO_CART = "view error when add to cart"

        const val CLICK_DISCUSSION_SEE_ALL = "click - lihat semua diskusi on diskusi terakhir"
        const val CLICK_THREAD_DETAIL_DISCUSSION = "click - thread detail on diskusi"
        const val CLICK_SEND_QUESTION = "click - kirim pertanyaan on diskusi"

        const val CLICK_SEE_ALL_ULASAN = "click - lihat semua ulasan"

        const val CLICK_TDN_BANNER_ADS_WIDGET= "click - tdn banner ads widget"
        const val VIEW_TDN_BANNER_ADS_WIDGET= "view - tdn banner ads widget"

        const val CLICK_VARIANT_GUIDELINE_BOTTOM_SHEET = "click - panduan ukuran on product detail bottomsheet"
        const val CLICK_SHOP_NOTES_BOTTOM_SHEET = "click - lihat informasi penting catatan toko on product detail bottomsheet"
        const val CLICK_SPECIFICATION_BOTTOM_SHEET = "click - selengkapnya on product detail bottomsheet"
        const val CLICK_CATEGORY_BOTTOM_SHEET = "click - kategori on product detail bottomsheet"
        const val CLICK_ETALASE_BOTTOM_SHEET = "click - etalase on product detail bottomsheet"
        const val CLICK_WRITE_DISCUSSION_BOTTOM_SHEET = "click - tanya di diskusi on product detail bottomsheet"
        const val CLICK_CHECK_DISCUSSION_BOTTOM_SHEET = "click - cek di diskusi on product detail bottomsheet"

        const val CLICK_REPORT_FROM_COMPONENT= "click - laporkan on produk bermasalah"
        const val CLICK_SHARE_FROM_CONTENT= "click - share product button on pdp"
        const val VIEW_TICKER_OOS = "view - ticker on pdp"
        const val CLICK_BUTTON_OOS = "click - %s on pdp"

        const val CLICK_FULLSCREEN_VIDEO = "click - fullscreen on video"
        const val CLICK_INTERACTION_VIDEO = "click - video on product image"
        const val CLICK_MUTE_VIDEO = "click - mute on video"

        const val CLICK_CHECK_CART = "click - cek keranjang"
        const val CLICK_PELAJARI_TOKO_CABANG = "click pelajari dikirim dari tokocabang"
        const val CLICK_SEE_OTHER_COURIER = "click - lihat kurir lainnya"
        const val CLICK_SHIPMENT_ERROR_COMPONENT = "click - ganti alamat when error on component shipping"
        const val VIEW_SHIPMENT_ERROR_BOTTOM_SHEET = "impression - bottomsheet on pdp"
        const val CLICK_BUTTON_SHIPMENT_ERROR_BOTTOM_SHEET_CHAT = "click - chat penjual on bottomsheet error"
        const val CLICK_BUTTON_SHIPMENT_ERROR_BOTTOM_SHEET_CHOOSE_ADDRESS = "click - ganti alamat on bottomsheet error"
    }


    object Label {
        const val EMPTY_LABEL = ""
        const val PDP = "PDP"
        const val DISCUSSION_EMPTY_QUESTION = "count thread:0;variant selected:%s;variant available:%s;"
        const val DISCUSSION_SEE_ALL ="count thread:%s;"
        const val DISCUSSION_DETAIL = "talk id:%s;count thread:%s;"
        const val TICKER_OOS = "ticker type:%s;ticker title:%s;ticker message:%s;"
        const val BUTTON_OOS = "fitur : OOS; is_variant:%s;"
        const val VIDEO_STATE = "state:"
        const val VIDEO_DURATION = "duration:"
        const val VIDEO_LAST_STOP_TIME = "stop time:"
        const val VIDEO_AUTO_PLAY = "auto play:"
        const val EVENT_LABEL_CLICK_SHIPMENT = "title:%s;labelShipping:%s;cod:%s"
        const val EVENT_LABEL_CLICK_SHIPMENT_ERROR = "error message:%s;"
        const val VIEW_LABEL_CLICK_SHIPMENT_ERROR_BOTTOM_SHEET = "bottomsheet title:%s;"

    }

    object Report {
        const val EVENT = "clickReport"
        const val EVENT_LABEL = "Report"
        const val NOT_LOGIN_EVENT_LABEL = "Report - Not Login"
    }

    object PDP {
        const val EVENT_CLICK_PDP = "clickPDP"
        const val EVENT_CLICK_RECOMMENDATION = "clickRecommendation"
        const val EVENT_VIEW_PDP = "viewPDP"
        const val EVENT_CLICK_TOP_NAV = "clickTopNav"
        const val EVENT_VIEW_PDP_IRIS = "viewPDPIris"
        const val EVENT_CLICK_COURIER = "clickCourier"
    }

    object Affiliate {
        const val CLICK_AFFILIATE = "clickAffiliate"
        const val CATEGORY = "product detail page tokopedia by.me"
        const val ACTION = "click tambah ke by.me"
        const val ACTION_CLICK_WISHLIST = "click wishlist"
    }

    object ImeiChecker {
        const val CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS = "butuh akses ke hp mu"
        const val CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS_INFO = "aktifkan akses telepon yuk"
    }

    object ImageReview {
        const val ACTION_SEE_ITEM = "click - review gallery on foto dari pembeli"
        const val ACTION_SEE_ALL = "click - lihat semua review gallery"
    }

    object MerchantVoucher {
        const val PROMO_CLICK = "promoClick"
        const val PROMO_VIEW = "promoView"
    }

    object TopAds {
        const val PDP_BANNER_TOPADS = "/product - tdn banner ads"
        const val DFAULT_CREATIVE_NAME_BANNER_TOP_ADS = "none / other"
        const val RECOMMENDATION_COMPARISON = "comparison widget"
        const val RECOMMENDATION_CAROUSELL = "carousell"
    }

    object MiniSocialProof {
        const val CLICK_BUYER_PHOTOS = "click - foto dari pembeli on social proof"
    }

}