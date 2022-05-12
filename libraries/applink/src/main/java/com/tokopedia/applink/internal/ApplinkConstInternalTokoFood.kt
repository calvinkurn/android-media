package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalTokoFood {

    @JvmField
    val HOST_FOOD = "food"

    @JvmField
    val INTERNAL_TOKO_FOOD = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_FOOD"

    //TokoFoodOrderTrackingActivity
    @JvmField
    val POST_PURCHASE = "$INTERNAL_TOKO_FOOD/postpurchase"
}