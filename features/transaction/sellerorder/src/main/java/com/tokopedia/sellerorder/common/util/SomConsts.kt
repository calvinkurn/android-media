package com.tokopedia.sellerorder.common.util

import com.tokopedia.imageassets.TokopediaImageUrl

/**
 * Created by fwidjaja on 2019-08-30.
 */
object SomConsts {
    const val PARAM_INPUT = "input"
    const val PARAM_SELLER = "seller"
    const val PARAM_LANG_ID = "id"
    const val PARAM_ORDER_ID = "order_id"
    const val PARAM_BOOKING_CODE = "bookingCode"
    const val PARAM_BARCODE_TYPE = "barcodeType"
    const val PARAM_ORDER_CODE = "order_code"
    const val PARAM_INVOICE = "invoice"
    const val PARAM_PASS_INVOICE = "pass_invoice"
    const val PARAM_CURR_IS_CHANGE_SHIPPING = "is_change_shipping"

    const val VAR_PARAM_ORDERID = "orderID"
    const val VAR_PARAM_LANG = "lang"

    const val TAB_ACTIVE = "tab_active"
    const val TAB_STATUS = "tab_status"
    const val FILTER_STATUS_ID = "filter_status_id"
    const val FILTER_ORDER_TYPE = "filter_order_type"
    const val FROM_WIDGET_TAG = "from widget"

    const val STATUS_HISTORY = "history"
    const val STATUS_ALL_ORDER = "all_order"
    const val STATUS_NEW_ORDER = "new_order"

    const val STATUS_NAME_ALL_ORDER = "Semua Pesanan"

    const val DETAIL_HEADER_TYPE = "header"
    const val DETAIL_RESO_TYPE = "reso"
    const val DETAIL_PRODUCTS_TYPE = "products"
    const val DETAIL_SHIPPING_TYPE = "shipping"
    const val DETAIL_PAYMENT_TYPE = "payment"
    const val DETAIL_INCOME_TYPE = "income"
    const val DETAIL_MVC_USAGE_TYPE = "mvc_usage"
    const val DETAIL_POF_DATA_TYPE = "pof_data"

    const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"
    const val EXTRA_USER_MODE = "EXTRA_USER_MODE"

    const val ACTION_OK = "OK"
    const val RESULT_ACCEPT_ORDER = "result_accept_order"
    const val RESULT_REJECT_ORDER = "result_reject_order"
    const val RESULT_PROCESS_REQ_PICKUP = "result_process_req_pickup"
    const val RESULT_SET_DELIVERED = "result_set_delivered"
    const val RESULT_REFRESH_ORDER = "result_refresh_order"
    const val RESULT_CONFIRM_SHIPPING = "result_confirm_shipping"

    const val KEY_ACCEPT_ORDER = "accept_order"
    const val KEY_REJECT_ORDER = "reject_order"
    const val KEY_TRACK_SELLER = "track"
    const val KEY_REQUEST_PICKUP = "request_pickup"
    const val KEY_CONFIRM_SHIPPING = "confirm_shipping"
    const val KEY_CONFIRM_SHIPPING_AUTO = "confirm_shipping_auto"
    const val KEY_CONFIRM_SHIPPING_DROP_OFF = "confirm_shipping_drop_off"
    const val KEY_STATUS_COMPLAINT = "complaint"
    const val KEY_VIEW_COMPLAINT_SELLER = "view_complaint"
    const val KEY_SET_DELIVERED = "set_delivered"
    const val KEY_RESPOND_TO_CANCELLATION = "respond_to_cancellations"
    const val KEY_PRINT_AWB = "print"
    const val KEY_ORDER_EXTENSION_REQUEST = "request_extend_order"
    const val KEY_RESCHEDULE_PICKUP = "reschedule_pickup"
    const val KEY_RETURN_TO_SHIPPER = "confirm_return"
    const val KEY_SEARCH_NEW_DRIVER = "search_new_driver"
    const val KEY_POF = "request_partial_order_fulfillment"

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

    const val LABEL_EMPTY = "-"
    const val LABEL_COPY_BOOKING_CODE = "LABEL_COPY_BOOKING_CODE"

    const val LIST_ORDER_SCREEN_NAME = "/myorder"
    const val DETAIL_ORDER_SCREEN_NAME = "myorder/detail/"
    const val ATTRIBUTE_ID = "id"

    const val STATUS_CODE_ORDER_CANCELLED = 0
    const val STATUS_CODE_ORDER_AUTO_CANCELLED = 3
    const val STATUS_CODE_ORDER_REJECTED = 10
    const val STATUS_CODE_ORDER_CREATED = 220
    const val STATUS_CODE_ORDER_ORDER_CONFIRMED = 400
    const val STATUS_CODE_ORDER_DELIVERED = 600
    const val STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT = 699
    const val STATUS_CODE_WAITING_PICKUP = 450
    const val STATUS_CODE_READY_TO_SEND = 520
    const val STATUS_CODE_RECEIPT_CHANGED = 530

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

    // SOM FILTER LABEL
    const val FILTER_SORT = "Urutkan"
    const val FILTER_STATUS_ORDER = "Status Pesanan"
    const val FILTER_TYPE_ORDER = "Tipe Pesanan"
    const val FILTER_COURIER = "Kurir"
    const val FILTER_LABEL = "Label"
    const val FILTER_DATE = "Tanggal"
    const val PATTERN_DATE_PARAM = "dd/MM/yyyy"

    // Order list sort by value
    const val SORT_BY_PAYMENT_DATE_ASCENDING = 0L
    const val SORT_BY_TOTAL_OPEN_DESCENDING = 1L
    const val SORT_BY_PAYMENT_DATE_DESCENDING = 2L
    const val SORT_BY_DEADLINE_DATE_ASCENDING = 3L

    // Som List Illustration
    const val SOM_LIST_EMPTY_STATE_NO_FILTER_ILLUSTRATION = TokopediaImageUrl.SOM_LIST_EMPTY_STATE_NO_FILTER_ILLUSTRATION
    const val SOM_LIST_EMPTY_STATE_WITH_FILTER_ILLUSTRATION = TokopediaImageUrl.SOM_LIST_EMPTY_STATE_WITH_FILTER_ILLUSTRATION

    // Som print awb
    const val PATH_PRINT_AWB = "shipping-label"
    const val PRINT_AWB_ORDER_ID_QUERY_PARAM = "order_id"
    const val PRINT_AWB_MARK_AS_PRINTED_QUERY_PARAM = "mark_as_printed"
    const val PRINT_AWB_WEBVIEW_TITLE = "Pengaturan Label Pengiriman"

    const val PREFIX_HTTP = "http"
    const val PREFIX_HTTPS = "https://"

    // Set Delivered Consts
    const val SOM_SET_DELIVERED_SUCCESS_CODE = 1

    const val DEFAULT_INVALID_ORDER_ID = "0"

    const val ENCODING_UTF_8 = "UTF-8"

    const val DEADLINE_MORE_THAN_24_HOURS = 0
    const val DEADLINE_BETWEEN_12_TO_24_HOURS = 1
    const val DEADLINE_LOWER_THAN_12_HOURS = 2

    const val COACHMARK_KEY = "coachmark"
    const val COACHMARK_DISABLED = "disabled"

    const val SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_1 = "LG_FMD_1"
    const val SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_2 = "LG_FMD_2"
    const val SOM_DROP_OFF_BOTTOM_SHEET_TEMPLATE_3 = "LG_FMD_3"

    // Error messages
    const val ERROR_GET_ORDER_DETAIL = "Error when get order detail."
    const val ERROR_ACCEPTING_ORDER = "Error when accepting order."
    const val ERROR_GET_ORDER_REJECT_REASONS = "Error when get order reject reasons."
    const val ERROR_WHEN_SET_DELIVERED = "Error when set order status to delivered."
    const val ERROR_EDIT_AWB = "Error when edit AWB."
    const val ERROR_REJECT_ORDER = "Error when rejecting order."
}
