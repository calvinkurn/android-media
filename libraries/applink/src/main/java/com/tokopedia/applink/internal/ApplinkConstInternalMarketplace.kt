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

    // CheckoutVariantActivity, "EXTRA_ATC_REQUEST" = AtcRequestParam
    @JvmField
    val EXPRESS_CHECKOUT = "$INTERNAL_MARKETPLACE/checkout-variant"
    // CartActivity
    @JvmField
    val CART = "$INTERNAL_MARKETPLACE/cart"
    // ShipmentActivity
    @JvmField
    val CHECKOUT = "$INTERNAL_MARKETPLACE/checkout"
    // NormalCheckoutActivity
    @JvmField
    val NORMAL_CHECKOUT = "$INTERNAL_MARKETPLACE/normal-checkout"
    // CartAddressChoiceActivity
    @JvmField
    val CHECKOUT_ADDRESS_SELECTION = "$INTERNAL_MARKETPLACE/checkout-address-selection"

    // ProductDetailActivity
    @JvmField
    val PRODUCT_ADD_ITEM = "$INTERNAL_MARKETPLACE/product-add-item"
    @JvmField
    val PRODUCT_CATEGORY_PICKER = "$INTERNAL_MARKETPLACE/product-category-picker/{id}/"
    @JvmField
    val PRODUCT_DETAIL = "$INTERNAL_MARKETPLACE/product-detail/{id}/"
    @JvmField
    val PRODUCT_DETAIL_WITH_WAREHOUSE_ID = "$INTERNAL_MARKETPLACE/product-detail/{id}/?warehouse_id={whid}"
    @JvmField
    val PRODUCT_DETAIL_DOMAIN = "$INTERNAL_MARKETPLACE/product-detail/{shop_domain}/{product_key}/"
    @JvmField
    val PRODUCT_DETAIL_DOMAIN_WITH_AFFILIATE = "$INTERNAL_MARKETPLACE/product-detail/{shop_domain}/{product_key}/?aff={affiliate_string}"
    // ProductEditActivity
    @JvmField
    val PRODUCT_EDIT_ITEM = "$INTERNAL_MARKETPLACE/product-edit-item/{id}/"
    @JvmField
    val PRODUCT_EDIT_VARIANT_DASHBOARD = "$INTERNAL_MARKETPLACE/product-edit-variant-dashboard"
    @JvmField
    val PRODUCT_ETALASE_PICKER = "$INTERNAL_MARKETPLACE/product-etalase-picker/{id}/"
    @JvmField
    val PRODUCT_MANAGE_LIST = "$INTERNAL_MARKETPLACE/product-manage-list"
    // ReviewProductActivity, "x_prd_nm" = productName
    @JvmField
    val PRODUCT_REVIEW = "$INTERNAL_MARKETPLACE/product/{id}/review"

    // CreateReviewActivity
    @JvmField
    val CREATE_REVIEW = "$INTERNAL_MARKETPLACE/product-review/create/{reputation_id}/{product_id}/"

    @JvmField
    val HOME_RECOMMENDATION = "$INTERNAL_MARKETPLACE/rekomendasi/{id}/?ref={ref}"

    // ImageReviewGalleryActivity
    @JvmField
    val IMAGE_REVIEW_GALLERY = "$INTERNAL_MARKETPLACE/product/{id}/review/gallery"

    //ShopOpenRoutingActivity
    @JvmField
    val OPEN_SHOP = "$INTERNAL_MARKETPLACE/shop-open"

    @JvmField
    val SHOP_PAGE_DOMAIN = "$INTERNAL_MARKETPLACE/shop-page/?domain={domain}"

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

    //DistrictRecommendationShopSettingsActivity
    @JvmField
    val DISTRICT_RECOMMENDATION_SHOP_SETTINGS = "$INTERNAL_MARKETPLACE/district-recommendation-shop-settings"

    //GeolocationActivity
    @JvmField
    val GEOLOCATION = "$INTERNAL_MARKETPLACE/geolocation"

    //CodActivity
    @JvmField
    val COD = "$INTERNAL_MARKETPLACE/cod"

    // OnboardingActivity
    @JvmField
    val ONBOARDING = "$INTERNAL_MARKETPLACE/onboarding"

    // SettingFieldActivity
    @JvmField
    val USER_NOTIFICATION_SETTING = "$INTERNAL_MARKETPLACE/user-notification-setting"

    //Report Product
    @JvmField
    val REPORT_PRODUCT = "$INTERNAL_MARKETPLACE/product-report/{id}/"

    // ShopScoreDetailActivity
    @JvmField
    val SHOP_SCORE_DETAIL = "$INTERNAL_MARKETPLACE/shop-score-detail"

    // ChatSearchActivity
    @JvmField
    val CHAT_SEARCH = "$INTERNAL_MARKETPLACE/chat-search"

    // PowerMerchantSubscribeActivity
    @JvmField
    val POWER_MERCHANT_SUBSCRIBE = "$INTERNAL_MARKETPLACE/power-merchant-subscribe"

}
