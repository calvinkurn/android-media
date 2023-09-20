package com.tokopedia.editshipping.util

object EditShippingConstant {
    const val PARAM_VALIDATE_SHIPPING = "inputShippingEditorMobilePopup"

    const val DEFAULT_ERROR_MESSAGE = "Terjadi kesalahan pada server. Ulangi beberapa saat lagi"
    const val DEFAULT_ERROR_SHIPPING_EDITOR = "Kamu harus pilih minimal 1 layanan pengiriman, ya!"

    const val EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW"
    const val EXTRA_LAT = "EXTRA_LAT"
    const val EXTRA_LONG = "EXTRA_LONG"
    const val EXTRA_WAREHOUSE_DATA = "EXTRA_WAREHOUSE_DATA"
    const val EXTRA_IS_EDIT_WAREHOUSE = "EXTRA_IS_EDIT_WAREHOUSE"
    const val DEFAULT_LAT: Double = -6.175794
    const val DEFAULT_LONG: Double = 106.826457

    const val TICKER_STATE_UNAVAILABLE = 1

    const val TICKER_STATE_ERROR = 1
    const val TICKER_STATE_WARNING = 2

    const val BOTTOMSHEET_SHIPPER_DETAIL_TITLE = "Detail Kurir Pengiriman"

    const val SHIPPER_ID_INSTANT: Long = 1000
    const val SHIPPER_ID_SAMEDAY_ON_DEMAND: Long = 1006
    const val KURIR_REKOMENDASI_SHIPPER_ID = "26"
}
