package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://sellerapp".
 * Order by name
 * Only create "tokopedia-android-internal://" if this deeplink is used only for android app, and not shared to iOs and web.
 * If the deeplink is shared between iOS and web, it should use "tokopedia://" scheme.
 */
object ApplinkConstInternalSellerapp {

    @JvmField
    val HOST_SELLERAPP = "sellerapp"


    @JvmField
    val INTERNAL_SELLERAPP = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_SELLERAPP}"

    @JvmField
    val EXTERNAL_SELLERAPP = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${HOST_SELLERAPP}"

    //SellerHomeActivity
    @JvmField
    val SELLER_HOME = "$INTERNAL_SELLERAPP/sellerhome"

    @JvmField
    val SELLER_HOME_PRODUCT_MANAGE_LIST = "$INTERNAL_SELLERAPP/sellerhome-product-list"

    @JvmField
    val SELLER_HOME_CHAT = "$INTERNAL_SELLERAPP/sellerhome-chat"

    @JvmField
    val SELLER_HOME_SOM_ALL = "$INTERNAL_SELLERAPP/sellerhome-som-allorder"

    @JvmField
    val SELLER_HOME_SOM_NEW_ORDER = "$INTERNAL_SELLERAPP/sellerhome-som-neworder"

    @JvmField
    val SELLER_HOME_SOM_READY_TO_SHIP = "$INTERNAL_SELLERAPP/sellerhome-som-readytoship"

    @JvmField
    val SELLER_HOME_SOM_SHIPPED = "$INTERNAL_SELLERAPP/sellerhome-som-inshipping"

    @JvmField
    val SELLER_HOME_SOM_DONE = "$INTERNAL_SELLERAPP/sellerhome-som-done"

    @JvmField
    val SELLER_HOME_SOM_CANCELLED = "$INTERNAL_SELLERAPP/sellerhome-som-cancelled"

    @JvmField
    val SELLER_HOME_SOM_CANCELLATION_REQUEST = "$INTERNAL_SELLERAPP/sellerhome-som-cancellation-request"

    //SellerOnboardingActivity
    @JvmField
    val WELCOME = "$INTERNAL_SELLERAPP/welcome"

    //CentralizedPromoActivity
    @JvmField
    val CENTRALIZED_PROMO = "$INTERNAL_SELLERAPP/centralized-promo"
    @JvmField
    val CENTRALIZED_PROMO_FIRST_VOUCHER = "$INTERNAL_SELLERAPP/first-voucher-centralized-promo"

    //MenuSettingActivity
    @JvmField
    val MENU_SETTING = "$INTERNAL_SELLERAPP/menu-setting"

    //SellerSeamlessLoginFragment
    @JvmField
    val SEAMLESS_CHOOSE_ACCOUNT = "$INTERNAL_SELLERAPP/login-seamless-choose-account"


    //CreateMerchantVoucherStepsActivity
    @JvmField
    val CREATE_VOUCHER = "$INTERNAL_SELLERAPP/create-voucher"

    //VoucherListActivity
    @JvmField
    val VOUCHER_LIST = "$INTERNAL_SELLERAPP/voucher-list"
    @JvmField
    val VOUCHER_ACTIVE = "$INTERNAL_SELLERAPP/voucher-list/active"
    @JvmField
    val VOUCHER_HISTORY = "$INTERNAL_SELLERAPP/voucher-list/history"

    @JvmStatic
    val VOUCHER_DETAIL = "$INTERNAL_SELLERAPP/voucher-detail"

    //SellerMenuActivity
    @JvmField
    val SELLER_MENU = "$INTERNAL_SELLERAPP/seller-menu"

    // SellerActionActivity
    @JvmField
    val SELLER_ACTION = "$INTERNAL_SELLERAPP/seller-action"

    // SellerSettingsActivity
    @JvmField
    val SELLER_SETTINGS = "$INTERNAL_SELLERAPP/seller-settings"

    //AdminRoleAuthorizeActivity
    @JvmField
    val ADMIN_AUTHORIZE = "$INTERNAL_SELLERAPP/admin-authorize/{feature}/"
}
