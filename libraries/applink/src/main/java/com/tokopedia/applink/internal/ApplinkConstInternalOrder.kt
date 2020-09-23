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
    const val PARAM_DIGITAL = "digital"
    const val PARAM_EVENTS = "events"
    const val PARAM_DEALS = "deals"
    const val PARAM_PESAWAT = "pesawat"
    const val PARAM_GIFTCARDS = "giftcards"
    const val PARAM_INSURANCE = "insurance"
    const val PARAM_MODALTOKO = "modaltoko"
    const val PARAM_HOTEL = "hotel"

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

    @JvmField
    val UNIFY_ORDER_DIGITAL = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_DIGITAL"

    @JvmField
    val UNIFY_ORDER_EVENTS = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_EVENTS"

    @JvmField
    val UNIFY_ORDER_DEALS = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_DEALS"

    @JvmField
    val UNIFY_ORDER_PESAWAT = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_PESAWAT"

    @JvmField
    val UNIFY_ORDER_GIFTCARDS = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_GIFTCARDS"

    @JvmField
    val UNIFY_ORDER_INSURANCE = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_INSURANCE"

    @JvmField
    val UNIFY_ORDER_MODALTOKO = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_MODALTOKO"

    @JvmField
    val UNIFY_ORDER_HOTEL = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_HOTEL"
}