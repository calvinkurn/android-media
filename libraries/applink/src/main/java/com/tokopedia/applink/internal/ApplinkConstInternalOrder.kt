package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

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
    const val PARAM_UOH_ONGOING = "uoh_ongoing"
    const val PARAM_UOH_WAITING_CONFIRMATION = "uoh_waiting_confirmation"
    const val PARAM_UOH_PROCESSED = "uoh_processed"
    const val PARAM_UOH_SENT = "uoh_sent"
    const val PARAM_UOH_DELIVERED = "uoh_delivered"
    const val PARAM_DIGITAL = "digital"
    const val PARAM_EVENTS = "events"
    const val PARAM_DEALS = "deals"
    const val PARAM_PESAWAT = "pesawat"
    const val PARAM_TRAIN = "kereta"
    const val PARAM_BELANJA = "belanja"
    const val PARAM_GIFTCARDS = "giftcards"
    const val PARAM_INSURANCE = "insurance"
    const val PARAM_MODALTOKO = "modaltoko"
    const val PARAM_HOTEL = "hotel"
    const val PARAM_TRAVEL_ENTERTAINMENT = "travelent"
    const val PARAM_HISTORY = "history"
    const val PARAM_FILTER_ID = "filter_id"
    const val PARAM_ORDER_LIST = "order_list"
    const val PARAM_ORDER_ID = "order_id"
    const val KEY_LABEL = "orderCategory"
    const val PAGE = "Page"
    const val PER_PAGE = "PerPage"
    const val DIGITAL = "DIGITAL"
    const val FLIGHTS = "FLIGHTS"
    const val HOTELS = "HOTELS"
    const val MARKETPLACE = "MARKETPLACE"
    const val SURVEY_PARAM = "params"
    const val EVENTS = "EVENTS"
    const val EVENT = "EVENT"
    const val ORDER_FILTER_ID = "filter_id"
    const val PESANAN_DIBATALKAN = "16"
    const val PESANAN_DIPROSES = "12"
    const val PESANAN_TIBA = "14"
    const val PESANAN_SELESAI = "7"
    const val MENUNGGU_KONFIRMASI = "5"
    const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"
    const val EXTRA_USER_MODE = "EXTRA_USER_MODE"
    const val PATH_REQUEST_CANCEL = "request-cancel"

    // snapshot
    const val PARAM_ORDER_DETAIL_ID = "order_detail_id"
    const val IS_SNAPSHOT_FROM_SOM = "is_snapshot_from_som"

    @JvmField
    val HOST_SELLER = "seller"

    @JvmField
    val HOST_BUYER = "buyer"

    @JvmField
    val HOST_TRANSACTION = "transaction"

    @JvmField
    val HOST_ORDER = "order"

    @JvmField
    val HOST_ORDERLIST = "orderlist"

    @JvmField
    val INTERNAL_SELLER = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_SELLER"

    @JvmField
    val INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_ORDER"

    @JvmField
    val INTERNAL_BUYER = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_BUYER"

    @JvmField
    val INTERNAL_TRANSACTION_ORDERLIST = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_TRANSACTION/$PARAM_ORDER_LIST"

    @JvmField
    val NEW_ORDER = "$INTERNAL_SELLER/new-order"

    @JvmField
    val READY_TO_SHIP = "$INTERNAL_SELLER/ready-to-ship"

    @JvmField
    val SHIPPED = "$INTERNAL_SELLER/shipped"

    @JvmField
    val DELIVERED = "$INTERNAL_SELLER/delivered"

    @JvmField
    val HISTORY = "$INTERNAL_SELLER/$PARAM_HISTORY"

    @JvmField
    val TRACK = "$INTERNAL_ORDER/track"

    @JvmField
    val CANCELLED = "$INTERNAL_SELLER/cancelled"

    @JvmField
    val CANCELLATION_REQUEST = "$INTERNAL_SELLER/cancellationrequest"

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
    val ORDER_DETAIL = "$INTERNAL_SELLER/order?order_id={order_id}"

    @JvmField
    val INVOICE = "$INTERNAL_ORDER/invoice"

    @JvmField
    val UNIFY_ORDER = "$INTERNAL_ORDER/$UNIFIED"

    @JvmField
    val UNIFY_ORDER_MARKETPLACE = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_MARKETPLACE"

    @JvmField
    val UNIFY_ORDER_MARKETPLACE_IN_PROCESS = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_MARKETPLACE_DALAM_PROSES"

    @JvmField
    val UNIFY_ORDER_ONGOING = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_UOH_ONGOING"

    @JvmField
    val UNIFY_ORDER_WAITING_CONFIRMATION = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_UOH_WAITING_CONFIRMATION"

    @JvmField
    val UNIFY_ORDER_PROCESSED = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_UOH_PROCESSED"

    @JvmField
    val UNIFY_ORDER_SENT = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_UOH_SENT"

    @JvmField
    val UNIFY_ORDER_DELIVERED = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_UOH_DELIVERED"

    @JvmField
    val UNIFY_ORDER_DIGITAL = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_DIGITAL"

    @JvmField
    val UNIFY_ORDER_EVENTS = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_EVENTS"

    @JvmField
    val UNIFY_ORDER_DEALS = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_DEALS"

    @JvmField
    val UNIFY_ORDER_PESAWAT = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_PESAWAT"

    @JvmField
    val UNIFY_ORDER_TRAIN = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_TRAIN"

    @JvmField
    val UNIFY_ORDER_GIFTCARDS = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_GIFTCARDS"

    @JvmField
    val UNIFY_ORDER_INSURANCE = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_INSURANCE"

    @JvmField
    val UNIFY_ORDER_MODALTOKO = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_MODALTOKO"

    @JvmField
    val UNIFY_ORDER_HOTEL = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_HOTEL"

    @JvmField
    val UNIFY_ORDER_TRAVEL_ENTERTAINMENT = "$INTERNAL_ORDER/$UNIFIED?$FILTER=$PARAM_TRAVEL_ENTERTAINMENT"

    @JvmField
    val MARKETPLACE_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_MARKETPLACE/$HOST_ORDER"

    @JvmField
    val DIGITAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_DIGITAL/$HOST_ORDER"

    @JvmField
    val DIGITAL_ORDER_LIST_INTERNAL = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_DIGITAL/$HOST_ORDERLIST"

    @JvmField
    val ORDERLIST_DIGITAL_INTERNAL = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_ORDERLIST/$PARAM_DIGITAL"

    @JvmField
    val OMS_INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_ORDER"

    @JvmField
    val DEALS_INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_DEALS/$HOST_ORDER"

    @JvmField
    val EVENTS_INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_EVENTS/$HOST_ORDER"

    @JvmField
    val GIFTCARDS_INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_GIFTCARDS/$HOST_ORDER"

    @JvmField
    val INSURANCE_INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_INSURANCE/$HOST_ORDER"

    @JvmField
    val MODALTOKO_INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_MODALTOKO/$HOST_ORDER"

    @JvmField
    val HOTEL_INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_HOTEL/$HOST_ORDER"

    @JvmField
    val PESAWAT_INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_PESAWAT/$HOST_ORDER"

    @JvmField
    val BELANJA_INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_BELANJA/$HOST_ORDER"

    @JvmField
    val MARKETPLACE_INTERNAL_ORDER = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_MARKETPLACE/$HOST_ORDER"

    @JvmField
    val ORDER_LIST_INTERNAL = "${DeeplinkConstant.SCHEME_INTERNAL}://$PARAM_ORDER_LIST"

    @JvmField
    val MP_INTERNAL_CONFIRMED = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_BUYER/$PARAM_HISTORY?$PARAM_FILTER_ID=5"

    @JvmField
    val MP_INTERNAL_PROCESSED = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_BUYER/$PARAM_HISTORY?$PARAM_FILTER_ID=12"

    @JvmField
    val MP_INTERNAL_SHIPPED = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_BUYER/$PARAM_HISTORY?$PARAM_FILTER_ID=13"

    @JvmField
    val MP_INTERNAL_DELIVERED = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_BUYER/$PARAM_HISTORY?$PARAM_FILTER_ID=14"

    @JvmField
    val MP_INTERNAL_REQUEST_CANCEL = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_ORDER/$PATH_REQUEST_CANCEL"


    // order snapshot
    @JvmField
    val PATH_SNAPSHOT = "snapshot"

    @JvmField
    val INTERNAL_ORDER_SNAPSHOT = "${DeeplinkConstant.SCHEME_INTERNAL}://$PATH_SNAPSHOT/$HOST_ORDER"
}