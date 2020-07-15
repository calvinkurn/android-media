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

    //SellerOnboardingActivity
    @JvmField
    val WELCOME = "$INTERNAL_SELLERAPP/welcome"

    //CentralizedPromoActivity
    @JvmField
    val CENTRALIZED_PROMO = "$INTERNAL_SELLERAPP/centralized-promo"

    //MenuSettingActivity
    @JvmField
    val MENU_SETTING = "$INTERNAL_SELLERAPP/menu-setting"
}
