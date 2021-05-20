package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://marketplace".
 * Order by name
 * Only create "tokopedia-android-internal://" if this deeplink is used only for android app, and not shared to iOs and web.
 * If the deeplink is shared between iOS and web, it should use "tokopedia://" scheme.
 */
object ApplinkConstInternalMarketplace {

    @JvmField
    val HOST_MARKETPLACE = "marketplace"

    @JvmField
    val INTERNAL_MARKETPLACE = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_MARKETPLACE}"

    // StoreSettingActivity
    @JvmField
    val STORE_SETTING = "$INTERNAL_MARKETPLACE/store-setting"

    // QrScannerActivity
    @JvmField
    val QR_SCANNEER = "$INTERNAL_MARKETPLACE/qr-scanner/{need_result}/"

    // IntermediaryActivity
    @JvmField
    val DISCOVERY_CATEGORY_DETAIL = "$INTERNAL_MARKETPLACE/category/{DEPARTMENT_ID}/"

    @JvmField
    val DISCOVERY_CATEGORY_DETAIL_QUERY = "$INTERNAL_MARKETPLACE/category/{DEPARTMENT_ID}/?{query_param}"

    // CheckoutVariantActivity, "EXTRA_ATC_REQUEST" = AtcRequestParam
    @JvmField
    val EXPRESS_CHECKOUT = "$INTERNAL_MARKETPLACE/checkout-variant"
    // CartActivity
    @JvmField
    val CART = "$INTERNAL_MARKETPLACE/cart"
    // ShipmentActivity
    @JvmField
    val CHECKOUT = "$INTERNAL_MARKETPLACE/checkout"
    // CartAddressChoiceActivity
    @JvmField
    val CHECKOUT_ADDRESS_SELECTION = "$INTERNAL_MARKETPLACE/checkout-address-selection"
    // PreferenceListActivity
    @JvmField
    val PREFERENCE_LIST = "$INTERNAL_MARKETPLACE/preference-list"
    // PreferenceEditActivity
    @JvmField
    val PREFERENCE_EDIT = "$INTERNAL_MARKETPLACE/preference-edit"
    // OrderSummaryPageActivity
    @JvmField
    val ONE_CLICK_CHECKOUT = "$INTERNAL_MARKETPLACE/one-click-checkout"

    // AddEditProductCategoryActivity
    @JvmField
    val PRODUCT_CATEGORY_PICKER = "$INTERNAL_MARKETPLACE/product-category-picker/{id}/"

    // ProductDetailActivity
    @JvmField
    val PRODUCT_DETAIL = "$INTERNAL_MARKETPLACE/product-detail/{id}/"
    @JvmField
    val PRODUCT_DETAIL_WITH_AFFILIATE = "$INTERNAL_MARKETPLACE/product-detail/{product_id}/?is_from_explore_affiliate={isAffiliate}"
    @JvmField
    val PRODUCT_DETAIL_WITH_WAREHOUSE_ID = "$INTERNAL_MARKETPLACE/product-detail/{id}/?warehouse_id={whid}"
    @JvmField
    val PRODUCT_DETAIL_DOMAIN = "$INTERNAL_MARKETPLACE/product-detail/{shop_domain}/{product_key}/"
    @JvmField
    val PRODUCT_DETAIL_DOMAIN_WITH_AFFILIATE = "$INTERNAL_MARKETPLACE/product-detail/{shop_domain}/{product_key}/?aff={affiliate_string}"

    // AtcVariantActivity
    @JvmField
    val ATC_VARIANT = "$INTERNAL_MARKETPLACE/atc-variant/{product_id}/" +
            "?pageSource={pageSource}&" +
            "isTokoNow={isTokoNow}"

    // ProductManageActivity
    @JvmField
    val PRODUCT_MANAGE_LIST = "$INTERNAL_MARKETPLACE/product-manage-list"

    // ReviewProductActivity, "x_prd_nm" = productName
    @JvmField
    val PRODUCT_REVIEW = "$INTERNAL_MARKETPLACE/product/{id}/review"

    // StockReminderActivity
    @JvmField
    val STOCK_REMINDER = "$INTERNAL_MARKETPLACE/stock-reminder/{productId}/{productName}/{stock}/"

    // SetCashbackActivity
    @JvmField
    val SET_CASHBACK = "$INTERNAL_MARKETPLACE/set-cashback/{productId}/"

    // CampaignStockActivity
    @JvmField
    val RESERVED_STOCK = "$INTERNAL_MARKETPLACE/reserved-stock/{productId}/{shopId}/"

    // CreateReviewActivity
    @JvmField
    val CREATE_REVIEW = "$INTERNAL_MARKETPLACE/product-review/create/{reputation_id}/{product_id}/"

    // InboxReputationActivity
    val INBOX_REPUTATION = "$INTERNAL_MARKETPLACE/review"

    //ReviewDetailActivity
    val REVIEW_DETAIL = "$INTERNAL_MARKETPLACE/review/detail/{feedback_id}/"

    //SellerReviewDetailActivity
    val SELLER_REVIEW_DETAIL = "$INTERNAL_MARKETPLACE/seller-review-detail"

    // InboxReputationDetailActivity
    val INBOX_REPUTATION_DETAIL = "$INTERNAL_MARKETPLACE/reputation/{reputation_id}/"

    @JvmField
    val HOME_RECOMMENDATION = "$INTERNAL_MARKETPLACE/rekomendasi/{id}/?ref={ref}"

    // ImageReviewGalleryActivity
    @JvmField
    val IMAGE_REVIEW_GALLERY = "$INTERNAL_MARKETPLACE/product/{id}/review/gallery"

    //ShopOpenRoutingActivity
    @JvmField
    val OPEN_SHOP = "$INTERNAL_MARKETPLACE/shop-open"

    @JvmField
    val SHOP_PAGE_BASE = "$INTERNAL_MARKETPLACE/shop-page"

    @JvmField
    val SHOP_PAGE_DOMAIN = "$SHOP_PAGE_BASE?domain={domain}"

    //ShopInfoActivity
    @JvmField
    val SHOP_INFO = "$INTERNAL_MARKETPLACE/shop-info/{shop_id}/"

    //ShopPageActivity
    @JvmField
    val SHOP_PAGE = "$SHOP_PAGE_BASE/{shop_id}/"

    @JvmField
    val SHOP_PAGE_HOME = "$SHOP_PAGE_BASE/{shop_id}/home"

    @JvmField
    val SHOP_PAGE_INFO = "$SHOP_PAGE_BASE/{shop_id}/info"

    @JvmField
    val SHOP_PAGE_REVIEW = "$SHOP_PAGE_BASE/{shop_id}/review"

    @JvmField
    val SHOP_PAGE_PRODUCT = "$SHOP_PAGE_BASE/{shop_id}/product"

    @JvmField
    val SHOP_PAGE_FEED = "$SHOP_PAGE_BASE/{shop_id}/feed"

    @JvmField
    val SHOP_PAGE_NOTE = "$SHOP_PAGE_BASE/{shop_id}/note"

    @JvmField
    val SHOP_PAGE_PRODUCT_LIST = "$SHOP_PAGE_BASE-product-list/{shop_id}/etalase/{etalase_id}/"

    // GmSubscribeHomeActivity
    @JvmField
    val GOLD_MERCHANT_SUBSCRIBE_DASHBOARD = "$INTERNAL_MARKETPLACE/gold-merchant-subscribe-dashboard"

    @JvmField
    val CONTACT_US = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://customercare/{ticket_id}"

    // GmMembershipActivity
    @JvmField
    val GOLD_MERCHANT_MEMBERSHIP = "$INTERNAL_MARKETPLACE/gold-merchant-membership"

    /**
     * This will be pattern to shop settings module
     * In the future, If there is a new shop settings deeplink, start with this base
     */
    @JvmField
    val SHOP_SETTINGS_BASE = "$INTERNAL_MARKETPLACE/shop-settings"

    //ShopSettingsInfoActivity
    @JvmField
    val SHOP_SETTINGS_INFO = "$SHOP_SETTINGS_BASE-info"

    //ShopSettingsNotesActivity
    @JvmField
    val SHOP_SETTINGS_NOTES = "$SHOP_SETTINGS_BASE-notes"

    //ShopSettingsEtalaseActivity
    @JvmField
    val SHOP_SETTINGS_ETALASE = "$SHOP_SETTINGS_BASE-etalase"

    @JvmField
    val SHOP_SETTINGS_ETALASE_ADD = "$SHOP_SETTINGS_ETALASE/add"

    //ShopSettingsAddressActivity
    @JvmField
    val SHOP_SETTINGS_ADDRESS = "$SHOP_SETTINGS_BASE-address"

    @JvmField
    val SHOP_EDIT_SCHEDULE = "$SHOP_SETTINGS_BASE-edit-schedule"

    @JvmField
    val SHOP_SETTINGS_SHIPPING = "$SHOP_SETTINGS_BASE-shipping"

    @JvmField
    val SHOP_SETTINGS_COD = "$SHOP_SETTINGS_BASE-cod"

    //DistrictRecommendationShopSettingsActivity
    @JvmField
    val DISTRICT_RECOMMENDATION_SHOP_SETTINGS = "$INTERNAL_MARKETPLACE/district-recommendation-shop-settings"

    //GeolocationActivity
    @JvmField
    val GEOLOCATION = "$INTERNAL_MARKETPLACE/geolocation"

    // OnboardingActivity
    @JvmField
    val ONBOARDING = "$INTERNAL_MARKETPLACE/onboarding"

    // SettingFieldActivity
    @JvmField
    val USER_NOTIFICATION_SETTING = "$INTERNAL_MARKETPLACE/user-notification-setting"

    @JvmField
    val CHAT_SETTING = "$INTERNAL_MARKETPLACE/chat/settings"
    @JvmField
    val CHAT_SETTING_TEMPLATE = "$INTERNAL_MARKETPLACE/chat/settings/templatechat"

    //Report Product
    @JvmField
    val REPORT_PRODUCT = "$INTERNAL_MARKETPLACE/product-report/{id}/"
    @JvmField
    val REPORT_PRODUCT_URL = "$INTERNAL_MARKETPLACE/product-report/{id}/?url={url}"

    // ShopScoreDetailActivity
    @JvmField
    val SHOP_SCORE_DETAIL = "$INTERNAL_MARKETPLACE/shop-score-detail"

    // ChatSearchActivity
    @JvmField
    val CHAT_SEARCH = "$INTERNAL_MARKETPLACE/chat-search"

    // NotificationActivity
    @JvmField
    val NOTIFICATION_CENTER = "$INTERNAL_MARKETPLACE/notification"

    @JvmField
    val NOTIFICATION_BUYER_INFO = "$INTERNAL_MARKETPLACE/notif-center"

    @JvmField
    val NOTIFICATION_BUYER_INFO_WITH_ID = "$INTERNAL_MARKETPLACE/notif-center/{id}/"

    // PowerMerchantSubscribeActivity
    @JvmField
    val POWER_MERCHANT_SUBSCRIBE = "$INTERNAL_MARKETPLACE/power-merchant-subscribe"

    @JvmField
    val GOLD_MERCHANT_REDIRECT = "$INTERNAL_MARKETPLACE/gold-merchant-redirect"

    // AttachInvoiceActivity
    @JvmField
    val ATTACH_INVOICE = "$INTERNAL_MARKETPLACE/user-attach-invoice"

    // AttachProductActivity
    @JvmField
    val ATTACH_PRODUCT = "$INTERNAL_MARKETPLACE/user-attach-product"

    @JvmField
    val SHOP_PAGE_SETTING = "$SHOP_PAGE_BASE-setting"

    // AttachVoucherActivity
    @JvmField
    val ATTACH_VOUCHER = "$INTERNAL_MARKETPLACE/user-attach-voucher"

    // OrderHistoryActivity
    @JvmField
    val ORDER_HISTORY = "$INTERNAL_MARKETPLACE/user-order-history/{shop_id}/"

    // InboxActivity
    @JvmField
    val INBOX = "$INTERNAL_MARKETPLACE/inbox"

    // DashboardActivity
    @JvmField
    val SELLER_APP_DASHBOARD = "$INTERNAL_MARKETPLACE/sellerapp-dashboard"

    // ActivitySellingTransaction
    @JvmField
    val SELLING_TRANSACTION = "$INTERNAL_MARKETPLACE/selling-transaction/{tab_position}/"

    // ProductDraftListActivity
    @JvmField
    val PRODUCT_DRAFT_LIST = "$INTERNAL_MARKETPLACE/product-draft-list"

    //MitraToppersDashboardActivity
    @JvmField
    val MITRA_TOPPERS_DASHBOARD = "$INTERNAL_MARKETPLACE/mitra-toppers-dashboard"

    //SellerInfoActivity
    @JvmField
    val SELLER_INFO = "$INTERNAL_MARKETPLACE/seller-info"

    @JvmField
    val PARAM_IS_NEED_LOC = "IS_NEED_LOC"

    @JvmField
    val PARAM_FIRST_CREATE_SHOP = "FIRST_CREATE_SHOP"

    //SellerReviewListActivity
    @JvmField
    val REVIEW_SELLER = "$INTERNAL_MARKETPLACE/seller-review-page"

    //InboxReputationReportActivity
    @JvmField
    val REVIEW_SELLER_REPORT = "$INTERNAL_MARKETPLACE/review-report"

    @JvmField
    val REVIEW_REMINDER = "$INTERNAL_MARKETPLACE/review-reminder"

    //ShopFavouriteListActivity
    @JvmField
    val SHOP_FAVOURITE_LIST = "$INTERNAL_MARKETPLACE/shop-favourites"

    @JvmField
    val SHOP_FAVOURITE_LIST_WITH_SHOP_ID = "$SHOP_PAGE_BASE/{shop_id}/shop-favourites"

    @JvmField
    val SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID = "$SHOP_PAGE_BASE/{shop_id}/settings"

    @JvmField
    val ARGS_SHOP_ID = "ARGS_SHOP_ID"

    @JvmField
    val ARGS_REVIEW_ID = "ARGS_REVIEW_ID"

    @JvmField
    val ARGS_LAYOUT_ID = "layoutID"

    @JvmField
    val ARGS_CACHE_MANAGER_ID = "cache_manager_id"
}
