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

    //SellerHomeActivity
    @JvmField
    val SELLER_HOME = "$INTERNAL_SELLERAPP/sellerhome"

}
