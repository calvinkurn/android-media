package com.tokopedia.sellerorder.common.util

/**
 * Created by fwidjaja on 2019-08-30.
 */
object SomConsts {
    const val PARAM_INPUT = "input"
    const val PARAM_SELLER = "seller"
    const val PARAM_LANG_ID = "id"
    const val PARAM_ORDER_ID = "order_id"
    const val PARAM_CURR_IS_CHANGE_SHIPPING = "is_change_shipping"
    const val PARAM_BOOKING_CODE = "bookingCode"
    const val PARAM_BARCODE_TYPE = "barcodeType"
    const val PARAM_ORDER_CODE = "order_code"
    const val PARAM_INVOICE = "invoice"

    const val VAR_PARAM_ORDERID = "orderID"
    const val VAR_PARAM_LANG = "lang"

    const val TAB_ACTIVE = "tab_active"
    const val TAB_STATUS = "tab_status"
    const val FILTER_STATUS_ID = "filter_status_id"
    const val FILTER_ORDER_TYPE = "filter_order_type"
    const val FROM_WIDGET_TAG = "from widget"

    const val STATUS_ALL_ORDER = "all_order"
    const val STATUS_NEW_ORDER = "new_order"

    const val STATUS_NAME_ALL_ORDER = "Semua Pesanan"

    const val DETAIL_HEADER_TYPE = "header"
    const val DETAIL_PRODUCTS_TYPE = "products"
    const val DETAIL_SHIPPING_TYPE = "shipping"
    const val DETAIL_PAYMENT_TYPE = "payment"

    const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"
    const val EXTRA_USER_MODE = "EXTRA_USER_MODE"

    const val ACTION_OK = "OK"
    const val RESULT_ACCEPT_ORDER = "result_accept_order"
    const val RESULT_REJECT_ORDER = "result_reject_order"
    const val RESULT_PROCESS_REQ_PICKUP = "result_process_req_pickup"
    const val RESULT_CONFIRM_SHIPPING = "result_confirm_shipping"
    const val RESULT_SET_DELIVERED = "result_set_delivered"
    const val RESULT_REFRESH_ORDER = "result_refresh_order"

    const val KEY_ACCEPT_ORDER = "accept_order"
    const val KEY_REJECT_ORDER = "reject_order"
    const val KEY_TRACK_SELLER = "track"
    const val KEY_REQUEST_PICKUP = "request_pickup"
    const val KEY_CONFIRM_SHIPPING = "confirm_shipping"
    const val KEY_STATUS_COMPLAINT = "complaint"
    const val KEY_VIEW_COMPLAINT_SELLER = "view_complaint"
    const val KEY_SET_DELIVERED = "set_delivered"
    const val KEY_RESPOND_TO_CANCELLATION = "respond_to_cancellations"
    const val KEY_PRINT_AWB = "print"

    const val KEY_BATALKAN_PESANAN = "reject_shipping"
    const val KEY_UBAH_NO_RESI = "change_awb"
    const val KEY_UPLOAD_AWB = "upload_awb"
    const val KEY_CHANGE_COURIER = "change_courier"
    const val KEY_ASK_BUYER = "ask_buyer"

    const val VALUE_REASON_BUYER_NO_RESPONSE = "Pembeli Tidak Respons"
    const val VALUE_REASON_OTHER = "Lainnya"

    const val VALUE_COURIER_PROBLEM_OTHERS = "Lainnya"

    const val TITLE_PILIH_PENOLAKAN = "Pilih alasan penolakan"
    const val TITLE_PILIH_PRODUK_KOSONG = "Pilih produk yang kosong"
    const val TITLE_COURIER_PROBLEM = "Kendala Kurir"
    const val TITLE_ATUR_TOKO_TUTUP = "Atur tanggal toko tutup"
    const val TITLE_UBAH_RESI = "Ubah Nomor Resi"
    const val TITLE_KURIR_PENGIRIMAN = "Kurir Pengiriman"
    const val TITLE_JENIS_LAYANAN = "Jenis Layanan"

    const val INPUT_ORDER_ID = "#orderId"
    const val INPUT_SHIPPING_REF = "#shippingRef"
    const val INPUT_AGENCY_ID = "#agencyId"
    const val INPUT_SP_ID = "#spId"

    const val LABEL_EMPTY = "-"
    const val LABEL_COPY_BOOKING_CODE = "LABEL_COPY_BOOKING_CODE"

    const val LIST_ORDER_SCREEN_NAME = "/myorder"
    const val DETAIL_ORDER_SCREEN_NAME = "myorder/detail/"
    const val PARAM_SOURCE_ASK_BUYER = "tx_ask_buyer"
    const val ATTRIBUTE_ID = "id"

    const val STATUS_CODE_ORDER_CANCELLED = 0
    const val STATUS_CODE_ORDER_AUTO_CANCELLED = 3
    const val STATUS_CODE_ORDER_REJECTED = 10
    const val STATUS_CODE_ORDER_CREATED = 220
    const val STATUS_CODE_ORDER_ORDER_CONFIRMED = 400
    const val STATUS_CODE_ORDER_DELIVERED = 600
    const val STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT = 699

    const val SORT_ASCENDING = 0
    const val SORT_DESCENDING = 2
    const val CHIPS_SORT_DESC = "Paling Baru"
    const val CHIPS_SORT_ASC = "Paling Lama"

    const val NOT_YET_PRINTED_LABEL = "Belum Dicetak"
    const val ALREADY_PRINT_LABEL = "Sudah Dicetak"
    const val NOT_YET_PRINTED = 1
    const val ALREADY_PRINT = 2

    const val ERROR_GET_USER_ROLES = "Error when get user roles in %s."
    const val ERROR_REJECT_CANCEL_ORDER = "Error when rejecting cancel order."

    const val UNIFY_TICKER_TYPE_ANNOUNCEMENT = "announcement"
    const val UNIFY_TICKER_TYPE_INFO = "info"
    const val UNIFY_TICKER_TYPE_WARNING = "warning"
    const val UNIFY_TICKER_TYPE_ERROR = "error"

    const val KEY_PRIMARY_DIALOG_BUTTON = "primary"
    const val KEY_SECONDARY_DIALOG_BUTTON = "secondary"

    const val TOPADS_NO_PRODUCT = 1
    const val TOPADS_NO_ADS = 2
    const val TOPADS_MANUAL_ADS = 3
    const val TOPADS_AUTO_ADS = 4

    const val KEY_WAITING_PAYMENT_ORDER_LIST_RESULT = "waiting_payment_order_list_result"
    const val KEY_WAITING_PAYMENT_ORDER_LIST_PAGING_RESULT = "waiting_payment_order_list_paging_result"


    //SOM FILTER LABEL
    const val FILTER_SORT = "Urutkan"
    const val FILTER_STATUS_ORDER = "Status Pesanan"
    const val FILTER_TYPE_ORDER = "Tipe Pesanan"
    const val FILTER_COURIER = "Kurir"
    const val FILTER_LABEL = "Label"
    const val FILTER_DATE = "Tanggal"
    const val PATTERN_DATE_PARAM = "dd/MM/yyyy"

    // Order list sort by value
    const val SORT_BY_PAYMENT_DATE_ASCENDING = 0
    const val SORT_BY_TOTAL_OPEN_DESCENDING = 1
    const val SORT_BY_PAYMENT_DATE_DESCENDING = 2

    // Som List Illustration
    const val SOM_LIST_EMPTY_STATE_NO_FILTER_ILLUSTRATION = "https://ecs7.tokopedia.net/android/others/som_list_empty_state_illustration.png"
    const val SOM_LIST_EMPTY_STATE_WITH_FILTER_ILLUSTRATION = "https://ecs7.tokopedia.net/android/others/som_list_empty_state_with_filter_illustration.png"

    // Som print awb
    const val PATH_PRINT_AWB = "shipping-label"
    const val PRINT_AWB_ORDER_ID_QUERY_PARAM = "order_id"
    const val PRINT_AWB_MARK_AS_PRINTED_QUERY_PARAM = "mark_as_printed"
    const val PRINT_AWB_WEBVIEW_TITLE = "Pengaturan Label Pengiriman"
}