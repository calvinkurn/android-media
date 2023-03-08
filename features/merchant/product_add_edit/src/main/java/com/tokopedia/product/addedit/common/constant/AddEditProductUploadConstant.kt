package com.tokopedia.product.addedit.common.constant

/**
 * Created by faisalramd on 2020-03-24.
 */

class AddEditProductUploadConstant {
    companion object {
        const val EXTRA_CACHE_ID = "EXTRA_CACHE_ID"
        const val EXTRA_PRODUCT_INPUT_MODEL = "EXTRA_PRODUCT_INPUT_MODEL"
        const val IMAGE_SOURCE_ID = "VqbcmM"

        const val TITLE_NOTIF_PRODUCT_UPLOAD = "Upload produk"
        const val MESSAGE_NOTIF_PRODUCT_UPLOAD = "Mengupload produk"
        const val MESSAGE_NOTIF_PRODUCT_UPLOAD_SUCCESS = "Produk berhasil di-upload"
        const val MESSAGE_NOTIF_PRODUCT_UPLOAD_ERROR = "Product gagal di-upload. Coba lagi"

        const val REQUEST_DELAY_MILLIS = 500L

        const val LOGGING_TAG = "PRODUCT_UPLOAD"
        const val LOGGING_ERROR_FIELD_NAME = "type"
        val DISABLED_LOGGING_DATA_LIST = listOf(
            "Description" // Disabled because sometimes it's too long to log, or may contains PII data
        )
    }
}
