package com.tokopedia.applink.internal

import com.tokopedia.applink.ApplinkConst.TokopediaNow
import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://marketplace".
 * Order by name
 * Only create "tokopedia-android-internal://" if this deeplink is used only for android app, and not shared to iOs and web.
 * If the deeplink is shared between iOS and web, it should use "tokopedia://" scheme.
 */
object ApplinkConstInternalMarketplace {

    const val HOST_MARKETPLACE = "marketplace"
    const val HOST_HOME = "home"

    const val INTERNAL_MARKETPLACE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_MARKETPLACE"

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
    const val CHECKOUT_WITH_SPECIFIC_PAYMENT = "$INTERNAL_MARKETPLACE/checkout?" +
        "gateway_code={gateway_code}&" +
        "tenure_type={tenure_type}&" +
        "source={source}"

    // OrderSummaryPageActivity
    const val ONE_CLICK_CHECKOUT = "$INTERNAL_MARKETPLACE/one-click-checkout"
    const val ONE_CLICK_CHECKOUT_WITH_SPECIFIC_PAYMENT = "$INTERNAL_MARKETPLACE/one-click-checkout?" +
        "gateway_code={gateway_code}&" +
        "tenure_type={tenure_type}&" +
        "source={source}"
    const val ADD_ON_GIFTING = "$INTERNAL_MARKETPLACE/add-on-gifting"

    // AddEditProductCategoryActivity
    const val PRODUCT_CATEGORY_PICKER = "$INTERNAL_MARKETPLACE/product-category-picker/{id}/"

    // ProductDetailActivity
    const val PRODUCT_DETAIL = "$INTERNAL_MARKETPLACE/product-detail/{id}/"
    const val PRODUCT_DETAIL_WITH_AFFILIATE_UUID = "$INTERNAL_MARKETPLACE/product-detail/{id}/?aff_unique_id={affiliate_uuid}"
    const val PRODUCT_DETAIL_WITH_WAREHOUSE_ID = "$INTERNAL_MARKETPLACE/product-detail/{id}/?warehouse_id={whid}"
    const val PRODUCT_DETAIL_DOMAIN = "$INTERNAL_MARKETPLACE/product-detail/{shop_domain}/{product_key}/"
    const val PRODUCT_DETAIL_DOMAIN_WITH_AFFILIATE = "$INTERNAL_MARKETPLACE/product-detail/{shop_domain}/{product_key}/?aff={affiliate_string}&aff_unique_id={affiliate_uuid}"
    const val PRODUCT_AR = "$INTERNAL_MARKETPLACE/productar/{product_id}/"

    const val PRODUCT_DETAIL_EDUCATIONAL = "$INTERNAL_MARKETPLACE/product-edu/{type}/"
    const val POST_ATC = "$INTERNAL_MARKETPLACE/post-atc/{productId}/"
    const val PRODUCT_WEBVIEW_BS = "$INTERNAL_MARKETPLACE/product-webview-bs"

    // AtcVariantActivity
    @JvmField
    val ATC_VARIANT = "$INTERNAL_MARKETPLACE/atc-variant/{product_id}/{shop_id}/" +
        "?pageSource={pageSource}&" +
        "isTokoNow={isTokoNow}&" +
        "cdListName={cdListName}"

    // ProductManageActivity
    const val PRODUCT_MANAGE_LIST = "$INTERNAL_MARKETPLACE/product-manage-list"

    // ReadReviewActivity, "x_prd_nm" = productName
    const val PRODUCT_REVIEW = "$INTERNAL_MARKETPLACE/product/{id}/review"

    // ReadReviewActivity
    const val SHOP_REVIEW_FULL_PAGE = "$INTERNAL_MARKETPLACE/shop/{id}/review"

    // ReviewCredibilityActivity
    const val REVIEW_CREDIBILITY = "$INTERNAL_MARKETPLACE/review/credibility/{userId}/{source}/"

    // DetailedReviewMediaGalleryActivity
    const val REVIEW_MEDIA_GALLERY = "$INTERNAL_MARKETPLACE/review/media-gallery"

    // StockReminderActivity
    const val STOCK_REMINDER_BASE = "$INTERNAL_MARKETPLACE/stock-reminder/"

    const val STOCK_REMINDER = "$STOCK_REMINDER_BASE{productId}/{isVariant}/"

    // CampaignStockActivity
    const val RESERVED_STOCK_BASE = "$INTERNAL_MARKETPLACE/reserved-stock"

    const val RESERVED_STOCK = "$RESERVED_STOCK_BASE/{productId}/{shopId}/"

    // CreateReviewActivity
    const val CREATE_REVIEW = "$INTERNAL_MARKETPLACE/product-review/create/{reputation_id}/{product_id}/"
    const val CREATE_REVIEW_APP_LINK_PARAM_RATING = "rating"

    // BulkReviewActivity
    const val BULK_CREATE_REVIEW = "$INTERNAL_MARKETPLACE/product-review/bulk-create"
    const val BULK_CREATE_REVIEW_MESSAGE = "bulk_create_review_message"

    // EditReviewActivity
    const val EDIT_REVIEW = "$INTERNAL_MARKETPLACE/product-review/edit/{reputation_id}/{product_id}/"

    // InboxReputationActivity
    const val INBOX_REPUTATION = "$INTERNAL_MARKETPLACE/review"

    // ReviewDetailActivity
    const val REVIEW_DETAIL = "$INTERNAL_MARKETPLACE/review/detail/{feedback_id}/"

    // SellerReviewDetailActivity
    const val SELLER_REVIEW_DETAIL = "$INTERNAL_MARKETPLACE/seller-review-detail"

    // InboxReputationDetailActivity
    const val INBOX_REPUTATION_DETAIL = "$INTERNAL_MARKETPLACE/reputation/{reputation_id}/"

    const val HOME_RECOMMENDATION = "$INTERNAL_MARKETPLACE/rekomendasi/{id}/?ref={ref}"

    // ReviewGalleryActivity
    const val IMAGE_REVIEW_GALLERY = "$INTERNAL_MARKETPLACE/product/{id}/review/gallery"

    // ImageReviewGalleryActivity
    const val IMAGE_REVIEW_GALLERY_OLD = "$INTERNAL_MARKETPLACE/product/{id}/review/gallery-old"

    // ShopOpenRoutingActivity
    const val OPEN_SHOP = "$INTERNAL_MARKETPLACE/shop-open"

    const val SHOP_PAGE_BASE = "$INTERNAL_MARKETPLACE/shop-page"

    const val SHOP_PAGE_DOMAIN = "$SHOP_PAGE_BASE?domain={domain}"

    // ShopInfoActivity
    const val SHOP_INFO = "$INTERNAL_MARKETPLACE/shop-info/{shop_id}/"

    // ShopPageActivity
    const val SHOP_PAGE = "$SHOP_PAGE_BASE/{shop_id}/"

    const val SHOP_PAGE_HOME = "$SHOP_PAGE_BASE/{shop_id}/home"

    const val SHOP_PAGE_INFO = "$SHOP_PAGE_BASE/{shop_id}/info"

    const val SHOP_PAGE_REVIEW = "$SHOP_PAGE_BASE/{shop_id}/review"

    const val SHOP_PAGE_PRODUCT = "$SHOP_PAGE_BASE/{shop_id}/product"

    const val SHOP_PAGE_FEED = "$SHOP_PAGE_BASE/{shop_id}/feed"

    const val SHOP_PAGE_NOTE = "$SHOP_PAGE_BASE/{shop_id}/note"

    const val SHOP_PAGE_PRODUCT_LIST = "$SHOP_PAGE_BASE-product-list/{shop_id}/etalase/{etalase_id}/"

    const val SHOP_PAGE_NOW_1 = "$SHOP_PAGE_BASE/${TokopediaNow.TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_1}"

    const val SHOP_PAGE_NOW_2 = "$SHOP_PAGE_BASE/${TokopediaNow.TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_2}"

    const val SHOP_PAGE_NOW_STAGING = "$SHOP_PAGE_BASE/${TokopediaNow.TOKOPEDIA_NOW_STAGING_SHOP_ID}"

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

    // ShopSettingsInfoActivity
    const val SHOP_SETTINGS_INFO = "$SHOP_SETTINGS_BASE-info"

    // ShopSettingsNotesActivity
    const val SHOP_SETTINGS_NOTES = "$SHOP_SETTINGS_BASE-notes"

    // ShopSettingsEtalaseActivity
    const val SHOP_SETTINGS_ETALASE = "$SHOP_SETTINGS_BASE-etalase"

    const val SHOP_SETTINGS_ETALASE_ADD = "$SHOP_SETTINGS_ETALASE/add"

    // ShopSettingsAddressActivity
    const val SHOP_SETTINGS_ADDRESS = "$SHOP_SETTINGS_BASE-address"

    const val SHOP_EDIT_SCHEDULE = "$SHOP_SETTINGS_BASE-edit-schedule"

    const val SHOP_SETTINGS_OPERATIONAL_HOURS = "$SHOP_SETTINGS_BASE-operational-hours"

    const val SHOP_SETTINGS_SHIPPING = "$SHOP_SETTINGS_BASE-shipping"

    const val SHOP_SETTINGS_COD = "$SHOP_SETTINGS_BASE-cod"

    // DistrictRecommendationShopSettingsActivity
    const val DISTRICT_RECOMMENDATION_SHOP_SETTINGS = "$INTERNAL_MARKETPLACE/district-recommendation-shop-settings"

    // OnboardingActivity
    const val ONBOARDING = "$INTERNAL_MARKETPLACE/onboarding"

    // SettingFieldActivity
    const val USER_NOTIFICATION_SETTING = "$INTERNAL_MARKETPLACE/user-notification-setting"

    const val DEVICE_NOTIFICATION_SETTING = "$INTERNAL_MARKETPLACE/device-notification-setting"

    const val CHAT_SETTING = "$INTERNAL_MARKETPLACE/chat/settings"
    const val CHAT_SETTING_TEMPLATE = "$INTERNAL_MARKETPLACE/chat/settings/templatechat"

    // Report Product
    const val REPORT_PRODUCT = "$INTERNAL_MARKETPLACE/product-report/{id}/"
    const val REPORT_PRODUCT_URL = "$INTERNAL_MARKETPLACE/product-report/{id}/?url={url}"

    // ChatSearchActivity
    const val CHAT_SEARCH = "$INTERNAL_MARKETPLACE/chat-search"

    // TopchatReportWebViewActivity
    const val TOPCHAT_REPORT = "$INTERNAL_MARKETPLACE/chat/report"

    // BubbleChatActivationActivity
    const val TOPCHAT_BUBBLE_ACTIVATION = "$INTERNAL_MARKETPLACE/bubble-activation"

    // PowerMerchantSubscribeActivity
    const val POWER_MERCHANT_SUBSCRIBE = "$INTERNAL_MARKETPLACE/power-merchant-subscribe"

    // MembershipDetailActivity
    const val PM_BENEFIT_PACKAGE = "$INTERNAL_MARKETPLACE/pm-benefit-package"

    // AdminInvitationConfirmationActivity
    @JvmField
    val ADMIN_INVITATION = "$INTERNAL_MARKETPLACE/shop-admin/invitation-page"

    // AdminInvitationAcceptedActivity
    @JvmField
    val ADMIN_ACCEPTED = "$INTERNAL_MARKETPLACE/shop-admin/accepted-page"

    // ShopAdminRedirectionActivity
    @JvmField
    val ADMIN_REDIRECTION = "$INTERNAL_MARKETPLACE/shop-admin/redirection-page"

    // ShopScorePerformanceActivity
    @JvmField
    val SHOP_PERFORMANCE = "$INTERNAL_MARKETPLACE/shop/performance"

    // ShopPenaltyActivity
    const val SHOP_PENALTY = "$INTERNAL_MARKETPLACE/shop-penalty"

    // ShopPenaltyDetailPageActivity
    @JvmField
    val SHOP_PENALTY_DETAIL = "$INTERNAL_MARKETPLACE/shop-penalty-detail"

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

    // MitraToppersDashboardActivity
    const val MITRA_TOPPERS_DASHBOARD = "$INTERNAL_MARKETPLACE/mitra-toppers-dashboard"

    // SellerInfoActivity
    const val SELLER_INFO = "$INTERNAL_MARKETPLACE/seller-info"

    const val PARAM_IS_NEED_LOC = "IS_NEED_LOC"

    const val PARAM_FIRST_CREATE_SHOP = "FIRST_CREATE_SHOP"

    // SellerReviewListActivity
    const val REVIEW_SELLER = "$INTERNAL_MARKETPLACE/seller-review-page"

    // InboxReputationReportActivity
    const val REVIEW_SELLER_REPORT = "$INTERNAL_MARKETPLACE/review-report"

    const val REVIEW_REMINDER = "$INTERNAL_MARKETPLACE/review-reminder"

    // ShopFavouriteListActivity
    const val SHOP_FAVOURITE_LIST = "$INTERNAL_MARKETPLACE/shop-favourites"

    const val SHOP_FAVOURITE_LIST_WITH_SHOP_ID = "$SHOP_PAGE_BASE/{shop_id}/shop-favourites"

    const val SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID = "$SHOP_PAGE_BASE/{shop_id}/settings"

    const val ARGS_SHOP_ID = "ARGS_SHOP_ID"

    const val ARGS_REVIEW_ID = "ARGS_REVIEW_ID"

    const val ARGS_LAYOUT_ID = "layoutID"

    const val ARGS_CACHE_MANAGER_ID = "cache_manager_id"

    const val ARGS_IS_UPGRADE = "is_upgrade"

    const val SHOP_OPERATIONAL_HOUR_BOTTOM_SHEET = "$INTERNAL_MARKETPLACE/shop/widget/operational-hour/{shop_id}/"

    const val SHOP_MVC_LOCKED_TO_PRODUCT = "$INTERNAL_MARKETPLACE/shop/widget/voucher/shop_id/{shop_id}/{voucher_id}/"

    /**
     * Go to chat list
     */
    const val TOPCHAT = "$INTERNAL_MARKETPLACE/topchat"

    /**
     * Go to chatroom with the provided {message_id}
     * If you want to use {shopId} to chatroom use external applink
     */
    const val TOPCHAT_ROOM = "$INTERNAL_MARKETPLACE/topchat/{message_id}"

    // NotificationAffiliateActivity
    const val AFFILIATE_NOTIFICATION = "${DeeplinkConstant.SCHEME_INTERNAL}://notif-center/affiliate"
}
