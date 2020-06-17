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
    val TRACK = "$INTERNAL_ORDER/track"

    @JvmField
    val CANCELLED = "$INTERNAL_SELLER/cancelled"

    @JvmField
    val WAITING_PICKUP = "$INTERNAL_SELLER/waiting-pickup"

    @JvmField
    val WAITING_AWB = "$INTERNAL_SELLER/waiting-awb"

    @JvmField
    val AWB_INVALID = "$INTERNAL_SELLER/awb-invalid"

    @JvmField
    val AWB_CHANGE = "$INTERNAL_SELLER/awb-change"

    @JvmField
    val RETUR = "$INTERNAL_SELLER/retur"

    @JvmField
    val COMPLAINT = "$INTERNAL_SELLER/complaint"

    @JvmField
    val FINISHED = "$INTERNAL_SELLER/finished"

    @JvmField
    val ORDER_DETAIL = "$INTERNAL_SELLER/order/{order_id}"

    @JvmField
    val INVOICE = "$INTERNAL_ORDER/invoice"

}