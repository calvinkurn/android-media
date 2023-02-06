package com.tokopedia.product.detail.common

object ProductTrackingConstant {

    object Category {
        const val PDP = "product detail page"
        const val PDP_DETAIL_BOTTOMSHEET = "product detail page - product detail bottomsheet"
        const val PDP_VARIANT_BOTTOMSHEET = "product detail page - global variant bottomsheet"
        const val PDP_AFTER_ATC = "product detail page after atc"
        const val TOP_NAV_SHARE_PDP = "top nav - product detail page"
        const val PRODUCT_DETAIL_PAGE_SELLER = "product detail page - seller side"
        const val PRODUCT_DETAIL_PAGE_SHIPPING = "pdp courier section"
        const val GLOBAL_VARIANT_BOTTOM_SHEET = "%s - global variant bottomsheet" //page source
        const val ITEM_CATEGORY_BUILDER = "%s / %s / %s / %s "
        const val KEY_UNDEFINED = "undefined"
    }

    object Tracking {
        const val KEY_SHOP_ID_SELLER = "shopId"
        const val KEY_SHOP_TYPE = "shopType"
        const val KEY_SHOP_NAME = "shopName"
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
        const val KEY_TRACKER_ID = "trackerId"

        /**
         * Tracking Key - Product Level
         * is using snake_case
         */
        const val KEY_PRODUCT_CATEGORY_ID = "category_id"
        const val KEY_PRODUCT_SHOP_ID = "shop_id"
        const val KEY_PRODUCT_SHOP_NAME = "shop_name"
        const val KEY_PRODUCT_SHOP_TYPE = "shop_type"

        /**
         * Tracking Key - Hit Level
         * is using camelCase
         */
        const val KEY_HIT_USER_ID = "userId"

        const val VALUE_BUSINESS_UNIT_SHARING = "sharingexperience"

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
        const val PRODUCT_PRICE = "productPrice"

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
        const val SELECT_CONTENT = "select_content"
        const val BUILDER_SHOP_ID = "shop_id:%s"
        const val KEY_WAREHOUSE_ID = "warehouseId"

        //Tradein
        const val TRADEIN_TRUE_DIAGNOSTIC = "true diagnostic"
        const val TRADEIN_TRUE_NON_DIAGNOSTIC = "true non diagnostic"

        const val KEY_DIMENSION_10 = "dimension10"
        const val KEY_DIMENSION_12 = "dimension12"
        const val KEY_DIMENSION_14 = "dimension14"
        const val KEY_DIMENSION_16 = "dimension16"
        const val KEY_DIMENSION_38 = "dimension38"
        const val KEY_DIMENSION_40 = "dimension40"
        const val KEY_DIMENSION_45 = "dimension45"
        const val KEY_DIMENSION_54 = "dimension54"
        const val KEY_DIMENSION_79 = "dimension79"
        const val KEY_DIMENSION_80 = "dimension80"
        const val KEY_DIMENSION_81 = "dimension81"
        const val KEY_DIMENSION_82 = "dimension82"
        const val KEY_DIMENSION_83 = "dimension83"
        const val KEY_DIMENSION_120 = "dimension120"

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
        const val CLICK_VARIANT = "click - variant"
        const val CLICK_PRODUCT_PICTURE = "click - product picture"
        const val CLICK_PRODUCT_IMAGE = "click - product image"
        const val SWIPE_PRODUCT_PICTURE = "click - swipe product picture"
        const val ACTION_WISHLIST_ON_PRODUCT_RECOMMENDATION = " - wishlist on product recommendation"
        const val CLICK_RIBBON_TRADE_IN = "click - ribbon trade in"
        const val CLICK_SEE_MORE_WIDGET = "click - see more on widget %s"
        const val CLICK_ANNOTATION_RECOM_CHIP = "click annotation chips"
        const val CLICK_SHARE_PDP = "click - share button"
        const val CLICK_READ_MORE = "click - baca selengkapnya"
        const val CLICK_SPECIFICATION_READ_MORE = "click - selengkapnya on spesifikasi produk  - product detail bottomsheet"
        const val CLICK_CATEGORY = "click - category on informasi produk"
        const val CLICK_ETALASE = "click - etalase on informasi produk"
        const val CLICK_LIHAT_SEMUA_ON_SIMULASI_CICILAN = "click - info cicilan"
        const val CLICK_BUTTON_CHAT = "click - sticky chat"
        const val ADD_WISHLIST = "add wishlist"
        const val REMOVE_WISHLIST = "remove wishlist"
        const val ADD_WISHLIST_NON_LOGIN = "add wishlist - non logged in"
        const val CLICK_CHECK_WISHLIST = "click - check wishlist kamu"
        const val CLICK_SHOP_PAGE = "click - shop page link"
        const val CLICK_TOASTER_LIHAT_SUCCESS_ATC = "click - lihat on add to cart success toaster"
        const val CLICK_TRADEIN = "click trade in widget"
        const val CLICK_UNFOLLOW = "click - unfollow shop"
        const val CLICK_FOLLOW = "click - follow shop"
        const val CLICK_EDIT_PRODUCT = "click - edit product button"
        const val CLICK_CHOOSE_PRODUCT_VARIANT = "click - choose product variant"
        const val CLICK_VARIANT_GUIDELINE = "click - lihat panduan on size chart"
        const val CLICK_VARIANT_BOTTOMSHEET_GUIDELINE = "click - lihat panduan on variant level ukuran"
        const val IMPRESSION_CHOOSE_VARIANT_NOTIFICATION = "impression - choose variant notification"
        const val VIEW_CHOOSE_VARIANT_ERROR = "view error when add to cart"
        const val CLICK_ATC_NON_LOGIN = "click - tambah ke keranjang on pdp - non login"
        const val CLICK_WHOLESALE = "click - info harga grosir"
        const val CLICK_TICKER = "click - selengkapnya on component ticker"
        const val CLICK_CUSTOM_INFO = "click - custom info component"
        const val CLICK_NPL_FOLLOWERS = "click - follow shop on bottom sheet"

        const val CLICK_NOTIFY_ME = "click - ingatkan saya on pdp campaign"
        const val CLICK_NOTIFY_ME_VARIANT_BOTTOMSHEET = "click - ingatkan saya"
        const val CLICK_DISKUSI_PRODUCT_TAB = "click - diskusi produk tab"

        const val CLICK_BUY_ACTIVATION_OVO = "click - beli then go to bottomsheet ovo activation"
        const val CLICK_SEE_BOTTOMSHEET_OVO = "click - beli then show bottomsheet"
        const val CLICK_TOPUP_BOTTOMSHEET_OVO = "on bottomsheet"
        const val CLICK_PP_INSURANCE_BOTTOMSHEET = "click - pp - insurance section"
        const val ACTION_PP_INSURANCE = "eligible - pp - insurance section"

        const val ACTION_CLICK_VARIANT = "click - pilih varian"
        const val ACTION_CLICK_TOKOCABANG = "click - tokocabang hyperlink"
        const val ACTION_CLICK_RESTRICTION_COMPONENT = "click - button on pdp restriction component"

        const val ACTION_VIEW_ERROR_WHEN_ADD_TO_CART = "view error when add to cart"

        const val CLICK_DISCUSSION_SEE_ALL = "click - lihat semua diskusi on diskusi terakhir"
        const val CLICK_THREAD_DETAIL_DISCUSSION = "click - thread detail on diskusi"
        const val CLICK_SEND_QUESTION = "click - kirim pertanyaan on diskusi"

        const val CLICK_SEE_ALL_ULASAN = "click - lihat semua ulasan"

        const val CLICK_TDN_BANNER_ADS_WIDGET= "click - tdn banner ads widget"
        const val VIEW_TDN_BANNER_ADS_WIDGET= "view - tdn banner ads widget"

        const val CLICK_VARIANT_GUIDELINE_BOTTOM_SHEET = "click - panduan ukuran on product detail bottomsheet"
        const val CLICK_CUSTOM_INFO_HAMPERS_BOTTOM_SHEET = "click - informasi pelengkap bingkisan"
        const val CLICK_SHOP_NOTES_BOTTOM_SHEET = "click - lihat informasi penting catatan toko on product detail bottomsheet"
        const val CLICK_SPECIFICATION_BOTTOM_SHEET = "click - selengkapnya on product detail bottomsheet"
        const val CLICK_CATEGORY_BOTTOM_SHEET = "click - kategori on product detail bottomsheet"
        const val CLICK_CATALOG_BOTTOM_SHEET = "click - catalog on detail produk bottomsheet"
        const val CLICK_ETALASE_BOTTOM_SHEET = "click - etalase on product detail bottomsheet"
        const val CLICK_WRITE_DISCUSSION_BOTTOM_SHEET = "click - tanya di diskusi on product detail bottomsheet"
        const val CLICK_CHECK_DISCUSSION_BOTTOM_SHEET = "click - cek di diskusi on product detail bottomsheet"

        const val CLICK_REPORT_FROM_COMPONENT= "click - laporkan on produk bermasalah"
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
        const val CLICK_MODULAR_COMPONENT = "click - modular component"
        const val CLICK_VARIANT_BUY_BUTTON = "click - Beli Langsung on pdp - to global variant bottomsheet"
        const val CLICK_VARIANT_ATC_BUTTON = "click - tambah ke keranjang on pdp - to global variant bottomsheet"

        const val CLICK_VARIANT_QUANTITY_EDITOR = "click - quantity editor"

        const val CLICK_SEE_ALL_CATEGORY_CAROUSEL = "click - lihat semua on category carousel"
        const val CLICK_CATEGORY_IMAGE = "click - category card on category carousel"
        const val EVENT_ACTION_CLICK_SEE_ALL_RECOM = "%s - %s - %s" //recoom title, page name, widget type

        const val CLICK_INFO_MULTILOC = "click - information icon on shop component"

        const val CLICK_PRODUCT_BUNDLING = "click - product bundling"
        const val CLICK_CHECK_PRODUCT_BUNDLING = "click - lihat paket on bundling component"
        const val IMPRESSION_PRODUCT_BUNDLING = "impression - bundling component"

        const val CLICK_LOCATION_RESTRICTION = "click - button on pdp restriction bottomsheet"
        const val IMPRESS_LOCATION_RESTRICTION = "impression - pdp restriction bottomsheet"
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
        const val EVENT_LABEL_CLICK_SHIPMENT = "title:%s;labelShipping:%s;cod:%s;"
        const val EVENT_LABEL_CLICK_SHIPMENT_ERROR = "error message:%s;"
        const val VIEW_LABEL_CLICK_SHIPMENT_ERROR_BOTTOM_SHEET = "bottomsheet title:%s;"
        const val EVENT_LABEL_CLICK_BEST_SELLER = "label:%s;category_id:%s;category_name:%s;"
        const val EVENT_COMPONENT_CLICK_BEST_SELLER = "comp:%s;temp:%s;elem:%s;cpos:%s;"
        const val EVENT_CREATIVE_CLICK_BEST_SELLER = "layout:%s;comp:%s;temp:%s;"
        const val EVENT_LAYOUT_CLICK_BEST_SELLER = "layout:%s;catName:%s;catId:%s;"
        const val EVENT_LABEL_CLICK_IMAGE_CATEGORY_CAROUSEL = "category_id:%s ;\ncategory_name:%s"
        const val EVENT_LABEL_CLICK_PRODUCT_BUNDLING_MULTIPLE = "product_id:%s; bundling_id:%s; bundling_type:multiple;"
        const val EVENT_LABEL_CLICK_CHECK_PRODUCT_BUNDLING = "bundling_id:%s; bundling_type:%s;"
        const val VIEW_LABEL_PRODUCT_BUNDLING = "bundling_id:%s; bundling_type:%s;"
        const val EVENT_LABEL_FLOW_CHOOSE_VARIANT = "flow:%s;parent_id:%s;child_id:%s"
    }

    object Report {
        const val EVENT = "clickReport"
        const val EVENT_LABEL = "Report"
        const val NOT_LOGIN_EVENT_LABEL = "Report - Not Login"
    }

    object PDP {
        const val EVENT_CLICK_PDP = "clickPDP"
        const val EVENT_CLICK_PG = "clickPG"
        const val EVENT_CLICK_RECOMMENDATION = "clickRecommendation"
        const val EVENT_VIEW_PDP = "viewPDP"
        const val EVENT_VIEW_PDP_IRIS = "viewPDPIris"
        const val EVENT_CLICK_COURIER = "clickCourier"
        const val EVENT_CLICK_COMMUNICATION = "clickCommunication"
    }

    object Affiliate {
        const val CLICK_AFFILIATE = "clickAffiliate"
        const val CATEGORY = "product detail page tokopedia by.me"
        const val ACTION = "click tambah ke by.me"
        const val ACTION_CLICK_WISHLIST = "click wishlist"
    }

    object ImageReview {
        const val ACTION_SEE_ITEM = "click - review gallery on foto dari pembeli"
        const val ACTION_SEE_ALL = "click - lihat semua review gallery"
    }

    object MostHelpfulReview {
        const val TRACKER_ID_CLICK_REVIEWER_NAME = "33836"
        const val ACTON_CLICK_REVIEWER_NAME = "click - reviewer name"
        const val LABEL_CLICK_REVIEWER_NAME = "feedback_id:%s;user_id:%s;statistics:%s;label:%s;"
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

    object ImpulsiveBanner{
        const val IMPRESSION_BANNER = "impression on pdp 7 widget banner"
        const val CLICK_BANNER = "click on pdp 7 widget banner"
        const val EVENT_COMPONENT_IMPRESSION_BANNER = "comp:%s;temp:%s;elem:%s;cpos:%s;"
        const val EVENT_LAYOUT_IMPRESSION_BANNER = "layout:%s;catName:%s;catId:%s;"
        const val CREATIVE_BUILDER = "/product - pdp_7 - %s - %s" //recomAlgo - keywordName
        const val CREATIVE_NAME = "impulsive widget"
    }

    object RecomTokonow {

        //recomatc
        const val KEY_EVENT_ATC = "addToCart"
        const val KEY_EVENT_PAGE_SOURCE = "pageSource"
        const val KEY_EVENT_CATEGORY_ATC = "tokonow product detail page"
        const val KEY_EVENT_ACTION_ATC = "click add to cart on tokonow product recommendation"
        const val KEY_EVENT_LABEL_ATC = "%s, %s" //recom title, chips value

        // example /tokonow - recomproduct - pdp_1 - rekomendasi untuk anda - {recommendation type}
        const val PARAM_ATC_DIMENS_40 = "/tokonow - recomproduct - %s - rekomendasi untuk anda - %s"
        const val PARAM_EVENT_PAGE_SOURCE = "%s.%s" //page source like PDP, recommendationType
    }

    object TrackerId {
        const val TRACKER_ID_CLICK_THUMBNAIL = "31790"
        const val TRACKER_ID_IMPRESS_THUMBNAIL = "31789"
        const val TRACKER_ID_CLICK_SPECIFICATION = "36325"
        const val TRACKER_ID_IMPRESS_SPECIFICATION = "36326"
        const val TRACKER_ID_CLICK_LOCATION_RESTRICTION = "40907"
        const val TRACKER_ID_IMPRESSION_LOCATION_RESTRICTION = "40906"
    }
}
