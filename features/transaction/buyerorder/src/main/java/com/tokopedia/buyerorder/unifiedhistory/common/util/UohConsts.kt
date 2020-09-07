package com.tokopedia.buyerorder.unifiedhistory.common.util

/**
 * Created by fwidjaja on 05/07/20.
 */
object UohConsts {
    const val ALL_DATE = "Semua Tanggal"
    const val ALL_STATUS = "Semua Status"
    const val ALL_TRANSACTIONS = "Semua Transaksi"
    const val ALL_CATEGORIES = "Semua Kategori"
    const val CHOOSE_DATE = "Pilih Tanggal"
    const val OTHERS = "Lainnya"
    const val CHOOSE_FILTERS = "Pilih Status"
    const val CHOOSE_CATEGORIES = "Pilih Kategori"
    const val TYPE_FILTER_DATE = 0
    const val TYPE_FILTER_STATUS = 1
    const val TYPE_FILTER_CATEGORY = 2
    const val START_DATE = "start_date"
    const val END_DATE = "end_date"

    const val ALL_STATUS_TRANSACTION = "Semua Status Transaksi"
    const val ALL_CATEGORIES_TRANSACTION = "Semua Kategori Transaksi"

    const val TICKER_TYPE_ANNOUNCEMENT = "announcement"
    const val TICKER_TYPE_ERROR = "error"
    const val TICKER_TYPE_INFORMATION = "information"
    const val TICKER_TYPE_WARNING = "warning"

    const val DATE_LIMIT = "#date_limit"

    const val BUTTON_VARIANT_FILLED = "filled"
    const val BUTTON_VARIANT_GHOST = "ghost"
    const val BUTTON_VARIANT_TEXT_ONLY = "text_only"

    const val BUTTON_TYPE_MAIN = "main"
    const val BUTTON_TYPE_TRANSACTION = "transaction"
    const val BUTTON_TYPE_ALTERNATE = "alternate"

    const val LS_PRINT_VERTICAL_CATEGORY = "ls_print"
    const val APPLINK_BASE = "tokopedia://"
    const val APPLINK_PATH_ORDER = "order"
    const val APPLINK_PATH_UPSTREAM = "upstream="

    const val XSOURCE = "recom_widget"
    const val PAGE_NAME = "bom_empty"

    const val TYPE_LOADER = "loader"
    const val TYPE_ORDER_LIST = "list"
    const val TYPE_EMPTY = "empty"
    const val TYPE_RECOMMENDATION_TITLE = "recommendation_title"
    const val TYPE_RECOMMENDATION_ITEM = "recommendation"

    const val TYPE_ACTION_BUTTON_LINK = "link"
    const val GQL_FINISH_ORDER = "gql-mp-finish"
    const val GQL_ATC = "gql-mp-atc"
    const val GQL_TRACK = "gql-mp-track"
    const val GQL_LS_FINISH = "gql-ls-finish"
    const val GQL_LS_LACAK = "gql-ls-lacak"
    const val GQL_FLIGHT_EMAIL = "gql-flight-email"
    const val GQL_TRAIN_EMAIL = "gql-train-email"
    const val GQL_MP_REJECT = "gql-mp-reject"
    const val GQL_MP_CHAT = "gql-mp-chat"
    const val GQL_MP_FINISH = "gql-mp-finish"
    const val GQL_RECHARGE_BATALKAN = "gql-recharge-batalkan"

    const val FINISH_ORDER_BOTTOMSHEET_TITLE = "Selesaikan pesanan ini?"
    const val REPLACE_ORDER_ID = "{order_id}"

    const val PARAM_LS_PRINT_FINISH_ACTION = "FINISHED"
    const val PARAM_LS_PRINT_BUSINESS_CODE = "LS_PRINT"

    const val LS_PRINT_GQL_PARAM_BUSINESS_CODE = "businessCode"
    const val LS_PRINT_GQL_PARAM_ACTION = "action"
    const val LS_PRINT_GQL_PARAM_UUID = "uuid"
    const val LS_PRINT_GQL_PARAM_VALUE = "value"

    const val LS_LACAK_MWEB = "m.tokopedia.com/order-details/lsprint/{order_id}?track=1"
    const val WRONG_FORMAT_EMAIL = "Format email salah"
    const val EMAIL_MUST_NOT_BE_EMPTY = "E-mail harus diisi"

    const val FLIGHT_GQL_PARAM_INVOICE_ID = "invoiceID"
    const val FLIGHT_GQL_PARAM_EMAIL_ID = "email"
    const val FLIGHT_STATUS_OK = "OK"

    const val RECHARGE_GQL_PARAM_ORDER_ID = "orderId"
    const val BUSINESS_UNIT_REPLACEE = "{business_unit}"

    const val APP_LINK_TYPE = "APP_LINK"
    const val WEB_LINK_TYPE = "WEB_LINK"
    const val APPLINK_RESO = "tokopedia-android-internal://global/webview?url=https://m.tokopedia.com/resolution-center/create/{order_id}/mobile"
    const val WEBVIEW = "webview"
}