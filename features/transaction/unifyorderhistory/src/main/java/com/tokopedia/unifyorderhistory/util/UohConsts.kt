package com.tokopedia.unifyorderhistory.util

/**
 * Created by fwidjaja on 05/07/20.
 */
object UohConsts {
    const val ALL_DATE = "Semua Tanggal"
    const val ALL_STATUS = "Semua Status"
    const val SEMUA_TRANSAKSI = "Semua Transaksi"
    const val TRANSAKSI_BERLANGSUNG = "Transaksi Berlangsung"
    const val DIKIRIM = "Dikirim"
    const val TIBA_DI_TUJUAN = "Tiba Di Tujuan"
    const val MENUNGGU_KONFIRMASI = "Menunggu Konfirmasi"
    const val DIPROSES = "Diproses"
    const val SEMUA_TRANSAKSI_BERLANGSUNG = "Semua Transaksi Berlangsung"
    const val ALL_PRODUCTS = "Semua Produk"
    const val CHOOSE_DATE = "Pilih Tanggal"
    const val OTHERS = "Lainnya"
    const val CHOOSE_STATUS = "Mau lihat status apa?"
    const val CHOOSE_PRODUCT = "Mau lihat produk apa?"
    const val TYPE_FILTER_DATE = 0
    const val TYPE_FILTER_STATUS = 1
    const val TYPE_FILTER_PRODUCT = 2
    const val START_DATE = "start_date"
    const val END_DATE = "end_date"
    const val CATEGORY_BELANJA = "marketplace"
    const val ALL_STATUS_TRANSACTION = "Semua Status Transaksi"
    const val TDN_INDEX = 6

    const val TICKER_TYPE_ANNOUNCEMENT = "announcement"
    const val TICKER_TYPE_ERROR = "error"
    const val TICKER_TYPE_INFORMATION = "information"
    const val TICKER_TYPE_INFO = "info"
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
    const val TYPE_LOADER_PMS_BUTTON = "loader_pms_button"
    const val TYPE_TICKER = "ticker"
    const val TYPE_ORDER_LIST = "list"
    const val TYPE_EMPTY = "empty"
    const val TYPE_RECOMMENDATION_TITLE = "recommendation_title"
    const val TYPE_RECOMMENDATION_ITEM = "recommendation"
    const val TDN_BANNER = "tdn"
    const val TYPE_PMS_BUTTON = "pms_button"

    const val TYPE_ACTION_BUTTON_LINK = "link"
    const val TYPE_ACTION_CANCEL_ORDER = "cancelOrder"
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
    const val GQL_MP_EXTEND = "gql-mp-extend"
    const val GQL_RECHARGE_BATALKAN = "gql-recharge-batalkan"

    const val FINISH_ORDER_BOTTOMSHEET_TITLE = "Selesaikan pesanan ini?"
    const val RESEND_ETICKET_BOTTOMSHEET_TITLE = "Kirim E-tiket"
    const val REPLACE_ORDER_ID = "{order_id}"

    const val PARAM_LS_PRINT_FINISH_ACTION = "FINISHED"
    const val PARAM_LS_PRINT_BUSINESS_CODE = "LS_PRINT"

    const val LS_PRINT_GQL_PARAM_BUSINESS_CODE = "businessCode"
    const val LS_PRINT_GQL_PARAM_ACTION = "action"
    const val LS_PRINT_GQL_PARAM_UUID = "uuid"
    const val LS_PRINT_GQL_PARAM_VALUE = "value"

    const val WRONG_FORMAT_EMAIL = "Format email salah"
    const val EMAIL_MUST_NOT_BE_EMPTY = "E-mail harus diisi"
    const val CTA_ATC = "Lihat"

    const val FLIGHT_GQL_PARAM_INVOICE_ID = "invoiceID"
    const val FLIGHT_GQL_PARAM_EMAIL_ID = "email"
    const val FLIGHT_STATUS_OK = "OK"

    const val RECHARGE_GQL_PARAM_ORDER_ID = "orderId"
    const val BUSINESS_UNIT_REPLACEE = "{business_unit}"

    const val APP_LINK_TYPE = "APP_LINK"
    const val WEB_LINK_TYPE = "WEB_LINK"
    const val URL_RESO = "https://m.tokopedia.com/resolution-center/create/{order_id}"
    const val WEBVIEW = "webview"
    const val UTF_8 = "UTF-8"

    const val EE_PRODUCT_ID = "product_id"
    const val EE_PRODUCT_PRICE = "product_price"
    const val EE_QUANTITY = "quantity"
    const val EE_SHOP_ID = "shop_id"
    const val EE_SHOP_TYPE = "shop_type"
    const val EE_SHOP_NAME = "shop_name"
    const val RECOMMENDATION_LIST_TRACK = "/my_purchase_list - rekomendasi untuk anda"
    const val RECOMMENDATION_LIST_TOPADS_TRACK = " - product topads"

    const val TX_ASK_SELLER = "tx_ask_seller"

    const val E_TIKET = "E-tiket & E-voucher Aktif"
    const val DALAM_PROSES = "Dalam Proses"
    const val VERTICAL_CATEGORY_EVENTS = "event"
    const val VERTICAL_CATEGORY_DEALS = "deals"
    const val VERTICAL_CATEGORY_FLIGHT = "flight"
    const val VERTICAL_CATEGORY_TRAIN = "train"
    const val VERTICAL_CATEGORY_GIFTCARD = "gift_card"
    const val VERTICAL_CATEGORY_INSURANCE = "insurance"
    const val VERTICAL_CATEGORY_MODALTOKO = "modal_toko"
    const val VERTICAL_CATEGORY_HOTEL = "hotel"
    const val VERTICAL_CATEGORY_TOKOFOOD = "tokofood"
    const val VERTICAL_CATEGORY_PLUS = "plus"

    const val QUERY_PARAM_INVOICE = "invoice"
    const val QUERY_PARAM_INVOICE_URL = "invoice_url"

    const val PRODUCT_ID = "product_id"
    const val QUANTITY = "quantity"
    const val NOTES = "notes"
    const val SHOP_ID = "shop_id"
    const val CUSTOMER_ID = "customer_id"
    const val WAREHOUSE_ID = "warehouse_id"
    const val PRODUCT_PRICE = "product_price"
    const val PRODUCT_NAME = "product_name"
    const val CATEGORY = "category"

    const val ACTION_FINISH_ORDER = "event_dialog_deliver_finish"

    const val STATUS_MENUNGGU_KONFIRMASI = "menunggu_konfirmasi"
    const val STATUS_DIPROSES = "diproses"
    const val STATUS_DIKIRIM = "dikirim"
    const val STATUS_TIBA_DI_TUJUAN = "tiba_di_tujuan"

    const val PARAM_INPUT = "input"
    const val TICKER_URL = "#ticker_url"
    const val TICKER_LABEL = "#ticker_label"

    const val PARAM_SHOP_NAME = "param_shop_name"
    const val PARAM_ORDER_ID = "order_id"
    const val PARAM_INVOICE = "param_invoice"
    const val PARAM_SERIALIZABLE_LIST_PRODUCT = "param_serializable_list_product"
    const val PARAM_SHOP_ID = "shop_id"
    const val PARAM_BOUGHT_DATE = "bought_date"
    const val PARAM_INVOICE_URL = "invoice_url"
    const val PARAM_STATUS_ID = "status_id"
    const val PARAM_SOURCE_UOH = "source_uoh"
    const val PARAM_HELP_LINK_URL = "help_link_url"

    const val DATE_FORMAT_DDMMMYYYY = "dd MMM yyyy"
    const val DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd"

    const val TDN_INVENTORY_ID = "17"
    const val TDN_ADS_COUNT = 1
    const val TDN_DIMEN_ID = 3
    const val TDN_RADIUS = 16

    const val PMS_IMAGE_URL = "https://images.tokopedia.net/img/android/uoh/saldo_tempo.png"
}
