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
    val QR_SCANNEER = "$INTERNAL_MARKETPLACE/qr-scanner"

    // IntermediaryActivity
    @JvmField
    val DISCOVERY_CATEGORY_DETAIL = "$INTERNAL_MARKETPLACE/category/{DEPARTMENT_ID}/"

    // CheckoutVariantActivity, "EXTRA_ATC_REQUEST" = AtcRequestParam
    @JvmField
    val EXPRESS_CHECKOUT = "$INTERNAL_MARKETPLACE/checkout-variant"
    // CartActivity
    @JvmField
    val CART = "$INTERNAL_MARKETPLACE/cart"

    // ProductDetailActivity
    @JvmField
    val PRODUCT_ADD_ITEM = "$INTERNAL_MARKETPLACE/product-add-item"
    @JvmField
    val PRODUCT_CATEGORY_PICKER = "$INTERNAL_MARKETPLACE/product-category-picker/{id}/"
    @JvmField
    val PRODUCT_DETAIL = "$INTERNAL_MARKETPLACE/product-detail/{id}/"
    @JvmField
    val PRODUCT_DETAIL_DOMAIN = "$INTERNAL_MARKETPLACE/product-detail/{shop_domain}/{product_key}/"
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

    @JvmField
    val DEFAULT_HOME_RECOMMENDATION = "$INTERNAL_MARKETPLACE/rekomendasi"

    @JvmField
    val HOME_RECOMMENDATION = "$INTERNAL_MARKETPLACE/rekomendasi/{id}/"

    // ImageReviewGalleryActivity
    @JvmField
    val IMAGE_REVIEW_GALLERY = "$INTERNAL_MARKETPLACE/product/{id}/review/gallery"

    //ShopOpenRoutingActivity
    @JvmField
    val OPEN_SHOP = "$INTERNAL_MARKETPLACE/shop-open"

    // GmSubscribeHomeActivity
    @JvmField
    val GOLD_MERCHANT_SUBSCRIBE_DASHBOARD = "$INTERNAL_MARKETPLACE/gold-merchant-subscribe-dashboard"


    @JvmField
    val CONTACT_US = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://customercare/{ticket_id}"

    // GmMembershipActivity
    @JvmField
    val GOLD_MERCHANT_MEMBERSHIP = "$INTERNAL_MARKETPLACE/gold-merchant-membership"

    // CustomerApp only: GoldMerchantRedirectActivity
    @JvmField
    val GOLD_MERCHANT_REDIRECT = "$INTERNAL_MARKETPLACE/gold-merchant-redirect"

    //ShopSettingsInfoActivity
    @JvmField
    val SHOP_SETTINGS = "$INTERNAL_MARKETPLACE/shop/setting"

    //ShopSettingsNoteActivity
    @JvmField
    val SHOP_NOTE_SETTING = "$INTERNAL_MARKETPLACE/shop/setting/notes"

    @JvmField
    val SHOP_SHIPPING_SETTING = "$INTERNAL_MARKETPLACE/shop/setting/shipping"

    //ShopSettingsEtalaseActivity
    @JvmField
    val SHOP_SETTINGS_ETALASE = "$INTERNAL_MARKETPLACE/shop/setting/etalase"

    //ShopSettingsAddressActivity
    @JvmField
    val SHOP_SETTINGS_ADDRESS = "$INTERNAL_MARKETPLACE/shop/setting/address"

    //DistrictRecommendationShopSettingsActivity
    @JvmField
    val DISTRICT_RECOMMENDATION_SHOP_SETTINGS = "$INTERNAL_MARKETPLACE/district-recommendation-shop-settings"

    // OnboardingActivity
    @JvmField
    val ONBOARDING = "$INTERNAL_MARKETPLACE/onboarding"

    // SettingFieldActivity
    @JvmField
    val USER_NOTIFICATION_SETTING = "$INTERNAL_MARKETPLACE/user-notification-setting"

    // SettingFieldActivity
    @JvmField
    val USER_PUSH_NOTIFICATION_SETTING = "$INTERNAL_MARKETPLACE/user-push-notification-setting"

    // ShopScoreDetailActivity
    @JvmField
    val SHOP_SCORE_DETAIL = "$INTERNAL_MARKETPLACE/shop-score-detail"

}
