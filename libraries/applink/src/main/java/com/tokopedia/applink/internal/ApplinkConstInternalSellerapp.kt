package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://sellerapp".
 * Order by name
 * Only create "tokopedia-android-internal://" if this deeplink is used only for android app, and not shared to iOs and web.
 * If the deeplink is shared between iOS and web, it should use "tokopedia://" scheme.
 */
object ApplinkConstInternalSellerapp {

    const val HOST_SELLERAPP = "sellerapp"


    const val INTERNAL_SELLERAPP = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_SELLERAPP}"

    const val EXTERNAL_SELLERAPP = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${HOST_SELLERAPP}"

    //SellerHomeActivity
    const val SELLER_HOME = "$INTERNAL_SELLERAPP/sellerhome"

    //InitialSearchActivity
    const val SELLER_SEARCH = "$INTERNAL_SELLERAPP/seller-search"

    const val SELLER_HOME_PRODUCT_MANAGE_LIST = "$INTERNAL_SELLERAPP/sellerhome-product-list"

    const val SELLER_HOME_CHAT = "$INTERNAL_SELLERAPP/sellerhome-chat"

    const val SELLER_HOME_SOM_ALL = "$INTERNAL_SELLERAPP/sellerhome-som-allorder"

    const val SELLER_HOME_SOM_NEW_ORDER = "$INTERNAL_SELLERAPP/sellerhome-som-neworder"

    const val SELLER_HOME_SOM_READY_TO_SHIP = "$INTERNAL_SELLERAPP/sellerhome-som-readytoship"

    const val SELLER_HOME_SOM_SHIPPED = "$INTERNAL_SELLERAPP/sellerhome-som-inshipping"

    const val SELLER_HOME_SOM_DONE = "$INTERNAL_SELLERAPP/sellerhome-som-done"

    const val SELLER_HOME_SOM_CANCELLED = "$INTERNAL_SELLERAPP/sellerhome-som-cancelled"

    const val SELLER_HOME_SOM_CANCELLATION_REQUEST = "$INTERNAL_SELLERAPP/sellerhome-som-cancellation-request"

    //SellerOnboardingActivity
    const val WELCOME = "$INTERNAL_SELLERAPP/welcome"

    //CentralizedPromoActivity
    const val CENTRALIZED_PROMO = "$INTERNAL_SELLERAPP/centralized-promo"

    //FirstVoucherActivity
    const val CENTRALIZED_PROMO_FIRST_TIME = "$INTERNAL_SELLERAPP/first-time-centralized-promo"

    //MenuSettingActivity
    const val MENU_SETTING = "$INTERNAL_SELLERAPP/menu-setting"

    //SellerSeamlessLoginFragment
    const val SEAMLESS_CHOOSE_ACCOUNT = "$INTERNAL_SELLERAPP/login-seamless-choose-account"

    //CreateMerchantVoucherStepsActivity
    const val CREATE_VOUCHER = "$INTERNAL_SELLERAPP/create-voucher"
    const val CREATE_VOUCHER_PRODUCT = "$INTERNAL_SELLERAPP/create-voucher-product/{product_id}/"

    //VoucherListActivity
    const val VOUCHER_LIST = "$INTERNAL_SELLERAPP/voucher-list"
    const val VOUCHER_ACTIVE = "$INTERNAL_SELLERAPP/voucher-list/active"
    const val VOUCHER_HISTORY = "$INTERNAL_SELLERAPP/voucher-list/history"
    const val VOUCHER_PRODUCT_LIST = "$INTERNAL_SELLERAPP/voucher-product-list/{mode}/"

    //VoucherDetailActivity
    const val VOUCHER_DETAIL = "$INTERNAL_SELLERAPP/voucher-detail"
    const val VOUCHER_PRODUCT_DETAIL = "$INTERNAL_SELLERAPP/voucher-product-detail/{voucher_id}/"

    // Seller Shop Flash Sale - CampaignListActivity
    const val SELLER_SHOP_FLASH_SALE = "$INTERNAL_SELLERAPP/shop-flash-sale/{filter_mode}/"

    // CampaignListActivity
    const val CAMPAIGN_LIST = "$INTERNAL_SELLERAPP/campaign-list"

    //SellerMenuActivity
    const val SELLER_MENU = "$INTERNAL_SELLERAPP/seller-menu"

    // SellerSettingsActivity
    const val SELLER_SETTINGS = "$INTERNAL_SELLERAPP/seller-settings"

    //AdminRoleAuthorizeActivity
    const val ADMIN_AUTHORIZE = "$INTERNAL_SELLERAPP/admin-authorize/{feature}/"

    //SellerFeedbackActivity
    const val SELLER_FEEDBACK = "$INTERNAL_SELLERAPP/seller-feedback"

    const val REVIEW_REMINDER = "$INTERNAL_SELLERAPP/review-reminder"
    //AdminRestrictionActivity
    const val ADMIN_RESTRICTION = "$INTERNAL_SELLERAPP/admin-restriction"
    const val PARAM_ARTICLE_URL = "article_url"

    //ShopDiscount
    const val SHOP_DISCOUNT = "$INTERNAL_SELLERAPP/shop-discount"
    const val SHOP_DISCOUNT_MANAGE_DISCOUNT = "$INTERNAL_SELLERAPP/shop-discount/manage-discount"
    const val SHOP_DISCOUNT_MANAGE_PRODUCT_DISCOUNT = "$INTERNAL_SELLERAPP/shop-discount/manage-discount/product"
    const val SHOP_DISCOUNT_MANAGE_PRODUCT_VARIANT_DISCOUNT = "$INTERNAL_SELLERAPP/shop-discount/manage-discount/product/variant"
    const val SHOP_DISCOUNT_MANAGE_PRODUCT_MULTI_LOC_DISCOUNT = "$INTERNAL_SELLERAPP/shop-discount/manage-discount/product/multi-loc"

    const val TOKOMEMBER = "$INTERNAL_SELLERAPP/tokomember"
}
