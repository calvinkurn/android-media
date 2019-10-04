package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * Created by fwidjaja on 2019-09-05.
 */
object ApplinkConstInternalOrder {

    @JvmField
    val HOST_SELLER = "seller"

    @JvmField
    val HOST_ORDER = "order"

    @JvmField
    val INTERNAL_SELLER = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_SELLER"

    @JvmField
    val INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_ORDER"

    @JvmField
    val NEW_ORDER = "$INTERNAL_SELLER/new-order"

    @JvmField
    val READY_TO_SHIP = "$INTERNAL_SELLER/ready-to-ship"

    @JvmField
    val SHIPPED = "$INTERNAL_SELLER/shipped"

    @JvmField
    val DELIVERED = "$INTERNAL_SELLER/delivered"

    @JvmField
    val HISTORY = "$INTERNAL_SELLER/history"

    @JvmField
    val OPPORTUNITY = "$INTERNAL_SELLER/opportunity"

    @JvmField
    val HISTORY_ORDER = "$INTERNAL_ORDER/history"
}