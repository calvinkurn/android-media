package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConsInternalHome {
    const val HOST_HOME = "home"
    const val INTERNAL_HOME = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_HOME"

    const val MANAGE_SHOP = "$INTERNAL_HOME/manage-shop"

    //temp manage shop for sellerapp
    const val MANAGE_SHOP_SELLERAPP_TEMP = "$INTERNAL_HOME/seller/manage-shop"

    const val HOME_RECOMMENDATION = "$INTERNAL_HOME/rekomendasi/.*\\/"

    const val DEFAULT_HOME_RECOMMENDATION = "$INTERNAL_HOME/rekomendasi"

    const val HOME_SIMILAR_PRODUCT = "$INTERNAL_HOME/rekomendasi/d/.*"
}