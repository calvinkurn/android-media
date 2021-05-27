package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://marketplace".
 * Order by name
 * Only create "tokopedia-android-internal://" if this deeplink is used only for android app, and not shared to iOs and web.
 * If the deeplink is shared between iOS and web, it should use "tokopedia://" scheme.
 */
object ApplinkConstInternalMarketplace {

    const val HOST_MARKETPLACE = "marketplace"

    const val INTERNAL_MARKETPLACE = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_MARKETPLACE}"

    // StoreSettingActivity
    const val STORE_SETTING = "$INTERNAL_MARKETPLACE/store-setting"

    // QrScannerActivity
    const val QR_SCANNEER = "$INTERNAL_MARKETPLACE/qr-scanner/{need_result}/"

    // IntermediaryActivity
    const val DISCOVERY_CATEGORY_DETAIL = "$INTERNAL_MARKETPLACE/category/{DEPARTMENT_ID}/"

    const val DISCOVERY_CATEGORY_DETAIL_QUERY = "$INTERNAL_MARKETPLACE/category/{DEPARTMENT_ID}/?{query_param}"

    // CheckoutVariantActivity, "EXTRA_ATC_REQUEST" = AtcRequestParam
    const val EXPRESS_CHECKOUT = "$INTERNAL_MARKETPLACE/checkout-variant"
    // CartActivity
    const val CART = "$INTERNAL_MARKETPLACE/cart"
    // ShipmentActivity
    const val CHECKOUT = "$INTERNAL_MARKETPLACE/checkout"
    // CartAddressChoiceActivity
    const val CHECKOUT_ADDRESS_SELECTION = "$INTERNAL_MARKETPLACE/checkout-address-selection"
    // PreferenceListActivity
    const val PREFERENCE_LIST = "$INTERNAL_MARKETPLACE/preference-list"
    // PreferenceEditActivity
    const val PREFERENCE_EDIT = "$INTERNAL_MARKETPLACE/preference-edit"
    // OrderSummaryPageActivity
    const val ONE_CLICK_CHECKOUT = "$INTERNAL_MARKETPLACE/one-click-checkout"

    // AddEditProductCategoryActivity
    const val PRODUCT_CATEGORY_PICKER = "$INTERNAL_MARKETPLACE/product-category-picker/{id}/"

    // ProductDetailActivity
    const val PRODUCT_DETAIL = "$INTERNAL_MARKETPLACE/product-detail/{id}/"
    const val PRODUCT_DETAIL_WITH_AFFILIATE = "$INTERNAL_MARKETPLACE/product-detail/{product_id}/?is_from_explore_affiliate={isAffiliate}"
    const val PRODUCT_DETAIL_WITH_WAREHOUSE_ID = "$INTERNAL_MARKETPLACE/product-detail/{id}/?warehouse_id={whid}"
    const val PRODUCT_DETAIL_DOMAIN = "$INTERNAL_MARKETPLACE/product-detail/{shop_domain}/{product_key}/"
    const val PRODUCT_DETAIL_DOMAIN_WITH_AFFILIATE = "$INTERNAL_MARKETPLACE/product-detail/{shop_domain}/{product_key}/?aff={affiliate_string}"

    // ProductManageActivity
    const val PRODUCT_MANAGE_LIST = "$INTERNAL_MARKETPLACE/product-manage-list"

    // ReviewProductActivity, "x_prd_nm" = productName
    const val PRODUCT_REVIEW = "$INTERNAL_MARKETPLACE/product/{id}/review"

    // StockReminderActivity
    const val STOCK_REMINDER = "$INTERNAL_MARKETPLACE/stock-reminder/{productId}/{productName}/{stock}/"

    // SetCashbackActivity
    const val SET_CASHBACK = "$INTERNAL_MARKETPLACE/set-cashback/{productId}/"

    // CampaignStockActivity
    const val RESERVED_STOCK = "$INTERNAL_MARKETPLACE/reserved-stock/{productId}/{shopId}/"

    // CreateReviewActivity
    const val CREATE_REVIEW = "$INTERNAL_MARKETPLACE/product-review/create/{reputation_id}/{product_id}/"

    // InboxReputationActivity
    const val INBOX_REPUTATION = "$INTERNAL_MARKETPLACE/review"

    //ReviewDetailActivity
    const val REVIEW_DETAIL = "$INTERNAL_MARKETPLACE/review/detail/{feedback_id}/"

    //SellerReviewDetailActivity
    const val SELLER_REVIEW_DETAIL = "$INTERNAL_MARKETPLACE/seller-review-detail"

    // InboxReputationDetailActivity
    const val INBOX_REPUTATION_DETAIL = "$INTERNAL_MARKETPLACE/reputation/{reputation_id}/"

    const val HOME_RECOMMENDATION = "$INTERNAL_MARKETPLACE/rekomendasi/{id}/?ref={ref}"

    // ImageReviewGalleryActivity
    const val IMAGE_REVIEW_GALLERY = "$INTERNAL_MARKETPLACE/product/{id}/review/gallery"

    //ShopOpenRoutingActivity
    const val OPEN_SHOP = "$INTERNAL_MARKETPLACE/shop-open"

    const val SHOP_PAGE_BASE = "$INTERNAL_MARKETPLACE/shop-page"

    const val SHOP_PAGE_DOMAIN = "$SHOP_PAGE_BASE?domain={domain}"

    //ShopInfoActivity
    const val SHOP_INFO = "$INTERNAL_MARKETPLACE/shop-info/{shop_id}/"

    //ShopPageActivity
    const val SHOP_PAGE = "$SHOP_PAGE_BASE/{shop_id}/"

    const val SHOP_PAGE_HOME = "$SHOP_PAGE_BASE/{shop_id}/home"

    const val SHOP_PAGE_INFO = "$SHOP_PAGE_BASE/{shop_id}/info"

    const val SHOP_PAGE_REVIEW = "$SHOP_PAGE_BASE/{shop_id}/review"

    const val SHOP_PAGE_PRODUCT = "$SHOP_PAGE_BASE/{shop_id}/product"

    const val SHOP_PAGE_FEED = "$SHOP_PAGE_BASE/{shop_id}/feed"

    const val SHOP_PAGE_NOTE = "$SHOP_PAGE_BASE/{shop_id}/note"

    const val SHOP_PAGE_PRODUCT_LIST = "$SHOP_PAGE_BASE-product-list/{shop_id}/etalase/{etalase_id}/"

    // GmSubscribeHomeActivity
    const val GOLD_MERCHANT_SUBSCRIBE_DASHBOARD = "$INTERNAL_MARKETPLACE/gold-merchant-subscribe-dashboard"

    const val CONTACT_US = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://customercare/{ticket_id}"

    // GmMembershipActivity
    const val GOLD_MERCHANT_MEMBERSHIP = "$INTERNAL_MARKETPLACE/gold-merchant-membership"

    /**
     * This will be pattern to shop settings module
     * In the future, If there is a new shop settings deeplink, start with this base
     */
    const val SHOP_SETTINGS_BASE = "$INTERNAL_MARKETPLACE/shop-settings"

    //ShopSettingsInfoActivity
    const val SHOP_SETTINGS_INFO = "$SHOP_SETTINGS_BASE-info"

    //ShopSettingsNotesActivity
    const val SHOP_SETTINGS_NOTES = "$SHOP_SETTINGS_BASE-notes"

    //ShopSettingsEtalaseActivity
    const val SHOP_SETTINGS_ETALASE = "$SHOP_SETTINGS_BASE-etalase"

    const val SHOP_SETTINGS_ETALASE_ADD = "$SHOP_SETTINGS_ETALASE/add"

    //ShopSettingsAddressActivity
    const val SHOP_SETTINGS_ADDRESS = "$SHOP_SETTINGS_BASE-address"

    const val SHOP_EDIT_SCHEDULE = "$SHOP_SETTINGS_BASE-edit-schedule"

    const val SHOP_SETTINGS_SHIPPING = "$SHOP_SETTINGS_BASE-shipping"

    const val SHOP_SETTINGS_COD = "$SHOP_SETTINGS_BASE-cod"

    //DistrictRecommendationShopSettingsActivity
    const val DISTRICT_RECOMMENDATION_SHOP_SETTINGS = "$INTERNAL_MARKETPLACE/district-recommendation-shop-settings"

    //GeolocationActivity
    const val GEOLOCATION = "$INTERNAL_MARKETPLACE/geolocation"

    // OnboardingActivity
    const val ONBOARDING = "$INTERNAL_MARKETPLACE/onboarding"

    // SettingFieldActivity
    const val USER_NOTIFICATION_SETTING = "$INTERNAL_MARKETPLACE/user-notification-setting"

    const val CHAT_SETTING = "$INTERNAL_MARKETPLACE/chat/settings"
    const val CHAT_SETTING_TEMPLATE = "$INTERNAL_MARKETPLACE/chat/settings/templatechat"

    //Report Product
    const val REPORT_PRODUCT = "$INTERNAL_MARKETPLACE/product-report/{id}/"
    const val REPORT_PRODUCT_URL = "$INTERNAL_MARKETPLACE/product-report/{id}/?url={url}"

    // ShopScoreDetailActivity
    const val SHOP_SCORE_DETAIL = "$INTERNAL_MARKETPLACE/shop-score-detail"

    // ChatSearchActivity
    const val CHAT_SEARCH = "$INTERNAL_MARKETPLACE/chat-search"

    // NotificationActivity
    const val NOTIFICATION_CENTER = "$INTERNAL_MARKETPLACE/notification"

    const val NOTIFICATION_BUYER_INFO = "$INTERNAL_MARKETPLACE/notif-center"

    const val NOTIFICATION_BUYER_INFO_WITH_ID = "$INTERNAL_MARKETPLACE/notif-center/{id}/"

    // PowerMerchantSubscribeActivity
    const val POWER_MERCHANT_SUBSCRIBE = "$INTERNAL_MARKETPLACE/power-merchant-subscribe"

    const val GOLD_MERCHANT_REDIRECT = "$INTERNAL_MARKETPLACE/gold-merchant-redirect"

    // AttachInvoiceActivity
    const val ATTACH_INVOICE = "$INTERNAL_MARKETPLACE/user-attach-invoice"

    // AttachProductActivity
    const val ATTACH_PRODUCT = "$INTERNAL_MARKETPLACE/user-attach-product"

    const val SHOP_PAGE_SETTING = "$SHOP_PAGE_BASE-setting"

    // AttachVoucherActivity
    const val ATTACH_VOUCHER = "$INTERNAL_MARKETPLACE/user-attach-voucher"

    // OrderHistoryActivity
    const val ORDER_HISTORY = "$INTERNAL_MARKETPLACE/user-order-history/{shop_id}/"

    // InboxActivity
    const val INBOX = "$INTERNAL_MARKETPLACE/inbox"

    // DashboardActivity
    const val SELLER_APP_DASHBOARD = "$INTERNAL_MARKETPLACE/sellerapp-dashboard"

    // ActivitySellingTransaction
    const val SELLING_TRANSACTION = "$INTERNAL_MARKETPLACE/selling-transaction/{tab_position}/"

    // ProductDraftListActivity
    const val PRODUCT_DRAFT_LIST = "$INTERNAL_MARKETPLACE/product-draft-list"

    //MitraToppersDashboardActivity
    const val MITRA_TOPPERS_DASHBOARD = "$INTERNAL_MARKETPLACE/mitra-toppers-dashboard"

    //SellerInfoActivity
    const val SELLER_INFO = "$INTERNAL_MARKETPLACE/seller-info"

    const val PARAM_IS_NEED_LOC = "IS_NEED_LOC"

    const val PARAM_FIRST_CREATE_SHOP = "FIRST_CREATE_SHOP"

    //SellerReviewListActivity
    const val REVIEW_SELLER = "$INTERNAL_MARKETPLACE/seller-review-page"

    //InboxReputationReportActivity
    const val REVIEW_SELLER_REPORT = "$INTERNAL_MARKETPLACE/review-report"

    const val REVIEW_REMINDER = "$INTERNAL_MARKETPLACE/review-reminder"

    //ShopFavouriteListActivity
    const val SHOP_FAVOURITE_LIST = "$INTERNAL_MARKETPLACE/shop-favourites"

    const val SHOP_FAVOURITE_LIST_WITH_SHOP_ID = "$SHOP_PAGE_BASE/{shop_id}/shop-favourites"

    const val SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID = "$SHOP_PAGE_BASE/{shop_id}/settings"

    const val ARGS_SHOP_ID = "ARGS_SHOP_ID"

    const val ARGS_REVIEW_ID = "ARGS_REVIEW_ID"

    const val ARGS_LAYOUT_ID = "layoutID"

    const val ARGS_CACHE_MANAGER_ID = "cache_manager_id"
}
