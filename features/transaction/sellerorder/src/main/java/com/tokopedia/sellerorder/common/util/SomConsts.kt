package com.tokopedia.sellerorder.common.util

/**
 * Created by fwidjaja on 2019-08-30.
 */
object SomConsts {
    const val PARAM_INPUT = "input"
    const val PARAM_SELLER = "seller"
    const val PARAM_CLIENT = "android"
    const val PARAM_LIST_ORDER = "param_list_order"
    const val PARAM_LANG_ID = "id"
    const val PARAM_ORDER_ID = "order_id"
    const val PARAM_SHOP_ID = "shop_id"
    const val PARAM_IS_FROM_FINTECH = "is_from_fintech"

    const val VAR_PARAM_ORDERID = "orderID"
    const val VAR_PARAM_LANG = "lang"

    const val CATEGORY_ORDER_STATUS = "status"
    const val CATEGORY_ORDER_TYPE = "orderType"
    const val CATEGORY_COURIER_TYPE = "courier"

    const val TAB_ACTIVE = "tab_active"
    const val FILTER_TYPE_CHECKBOX = "checkbox"
    const val FILTER_TYPE_RADIO = "radio"
    const val FILTER_TYPE_LABEL = "label"
    const val FILTER_TYPE_SEPARATOR = "separator"

    const val STATUS_ALL_ORDER = "all_order"

    const val DETAIL_HEADER_TYPE = "header"
    const val DETAIL_PRODUCTS_TYPE = "products"
    const val DETAIL_SHIPPING_TYPE = "shipping"
    const val DETAIL_PAYMENT_TYPE = "payment"
    const val RECEIVER_NOTES_START = "[Tokopedia Note: "
    const val RECEIVER_NOTES_END = "]"
    const val RECEIVER_NOTES_COLON = ":"

    const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"
    const val EXTRA_USER_MODE = "EXTRA_USER_MODE"

    const val ACTION_OK = "OK"
    const val RESULT_ACCEPT_ORDER = "result_accept_order"
    const val RESULT_REJECT_ORDER = "result_reject_order"

    const val KEY_ACCEPT_ORDER = "accept_order"
    const val KEY_REJECT_ORDER = "reject_order"
    const val KEY_TRACK_SELLER = "track_seller"

    const val KEY_REASON_EMPTY_STOCK = "empty_stock"
    const val KEY_REASON_SHOP_CLOSED = "shop_closed"
    const val KEY_REASON_COURIER_PROBLEM = "courier_problem"
    const val KEY_REASON_BUYER_NO_RESPONSE = "buyer_no_resp"
    const val KEY_REASON_OTHER = "other_reason"

    const val VALUE_REASON_EMPTY_STOCK = "Stok Produk Kosong"
    const val VALUE_REASON_SHOP_CLOSED = "Toko Sedang Tutup"
    const val VALUE_REASON_COURIER_PROBLEM = "Kendala Kurir"
    const val VALUE_REASON_BUYER_NO_RESPONSE = "Pembeli Tidak Respons"
    const val VALUE_REASON_OTHER = "Lainnya"

    const val KEY_COURIER_PROBLEM_OFFICE_CLOSED = "office_closed"
    const val KEY_COURIER_PROBLEM_UNMATCHED_COST = "unmatched_cost"
    const val KEY_COURIER_PROBLEM_CHOOSEN_COURIER = "choosen_courier"
    const val KEY_COURIER_PROBLEM_OTHERS = "other_problem"

    const val VALUE_COURIER_PROBLEM_OFFICE_CLOSED = "Kantor Kurir Tutup"
    const val VALUE_COURIER_PROBLEM_UNMATCHED_COST = "Biaya Pengiriman Tidak Sesuai"
    const val VALUE_COURIER_PROBLEM_CHOOSEN_COURIER = "Tidak dapat mengirim dengan kurir yang dipilih"
    const val VALUE_COURIER_PROBLEM_OTHERS = "Lainnya"

    const val TITLE_PILIH_PENOLAKAN = "Pilih alasan penolakan"
    const val TITLE_PILIH_PRODUK_KOSONG = "Pilih produk yang kosong"
    const val TITLE_COURIER_PROBLEM = "Kendala Kurir"
    const val TITLE_ATUR_TOKO_TUTUP = "Atur tanggal toko tutup"

    const val BOTTOMSHEET_TEXT_ONLY_TYPE = "text_only"
    const val BOTTOMSHEET_TEXT_RADIO_TYPE = "text_radio"
    const val BOTTOMSHEET_TEXT_RADIO_WITH_REASON_TYPE = "text_radio_reason"

    const val START_DATE = "start_date"
    const val END_DATE = "end_date"

    const val BASE_URL_UPLOAD_PROOF_AWB = "https://m.tokopedia.com/myorder/list/uploadawb/"
    const val QUERY_INVOICE_URL_UPLOAD_AWB = "?invoice="
}