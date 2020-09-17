package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant
import java.io.FilterReader

/**
 * Created by fwidjaja on 2019-09-05.
 */
object ApplinkConstInternalOrder {

    const val UNIFIED = "unified"
    const val FILTER = "filter"
    const val UNIFY_ORDER_STATUS = "tokopedia-android-internal://order/unified?filter={customFilter}"
    const val PARAM_CUSTOM_FILTER = "{customFilter}"
    const val SOURCE_FILTER = "source_filter"
    const val PARAM_DALAM_PROSES = "dalam_proses"
    const val PARAM_E_TIKET = "etiket"
    const val PARAM_SEMUA_TRANSAKSI = "semua_transaksi"
    const val PARAM_MARKETPLACE = "marketplace"
    const val PARAM_MARKETPLACE_DALAM_PROSES = "marketplace_dalam_proses"

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

    @JvmField
    val UNIFY_ORDER = "$INTERNAL_ORDER/$UNIFIED"

    @JvmField
    val UNIFY_ORDER_MARKETPLACE = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_MARKETPLACE"

    @JvmField
    val UNIFY_ORDER_MARKETPLACE_IN_PROCESS = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_MARKETPLACE_DALAM_PROSES"

}