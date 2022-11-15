package com.tokopedia.epharmacy.utils

const val EXTRA_ORDER_ID_LONG = "extra_order_id_long"
const val EXTRA_CHECKOUT_ID_STRING = "extra_checkout_id_string"
const val EXTRA_ENTRY_POINT_STRING = "extra_entry_point_string"
const val EXTRA_SOURCE_STRING = "source"
const val EXTRA_ORIGIN_STRING = "extra_origin_string"
const val EXTRA_CONSULTATION_WEB_LINK_STRING = "extra_consultation_web_link_string"

const val DEFAULT_ZERO_VALUE = 0L

const val MAX_MEDIA_ITEM = 5

const val MEDIA_PICKER_REQUEST_CODE = 10020
const val EPHARMACY_UPLOAD_REQUEST_CODE = 10021
const val EPHARMACY_CHOOSER_REQUEST_CODE = 10022
const val EPHARMACY_MINI_CONSULTATION_REQUEST_CODE = 10023
const val EPHARMACY_PRESCRIPTION_IDS = "epharmacy_prescription_ids"
const val EPHARMACY_GROUP_ID = "epharmacy_group_id"

const val STATIC_INFO_COMPONENT = "static info component"
const val PRESCRIPTION_COMPONENT = "prescription component"

const val PRODUCT_COMPONENT = "product component"
const val GROUP_COMPONENT = "group component"
const val TICKER_COMPONENT = "ticker component"

const val EPHARMACY_SCREEN_NAME = "epharmacy page"

const val EPHARMACY_TNC_LINK = "https://www.tokopedia.com/help/article/syarat-dan-ketentuan-tokopedia-kesehatan"

enum class EPharmacyButtonKey(val key: String) {
    CHECK("epharmacy_check_prescription"),
    RE_UPLOAD("epharmacy_reupload_prescription"),
    DONE("epharmacy_done_prescription"),
    DONE_DISABLED("epharmacy_done_disabled")
}

enum class EPharmacyPrescriptionStatus(val status: String) {
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    ACTIVE("ACTIVE"),
    SELECTED("SELECTED")
}

const val EPharmacyImageQuality = 60

const val FIRST_INDEX = 0
const val MAX_MEDIA_SIZE_PICKER = 4_000_000L

const val ENTRY_POINT_ORDER = "Order"
const val ENTRY_POINT_CHECKOUT = "Checkout"

const val ENABLER_IMAGE_URL = "enabler_image_url"
const val UPLOAD_CHOOSER_IMAGE_URL = "https://images.tokopedia.net/img/android/res/singleDpi/epharmacy_chooser_mini_cons.jpg"
const val MINI_CONS_CHOOSER_IMAGE_URL = "https://images.tokopedia.net/img/android/res/singleDpi/epharmacy_chooser_mini_cons_doc.png"

const val EPHARMACY_APPLINK = "tokopedia://epharmacy/"
const val EPHARMACY_ATTACH_PRESCRIPTION_APPLINK = "tokopedia://epharmacy/attach-prescription"

const val DATA_TYPE = "data_type"
const val ENABLER_NAME = "enabler_name"

const val EPHARMACY_BOTTOM_SHEET_BOTTOM_TNC_IMAGE_URL = "https://images.tokopedia.net/img/green-waves.png"
const val EPHARMACY_BOTTOM_SHEET_BOTTOM_IMAGE_URL = "https://images.tokopedia.net/img/miniconsul-toped-illustration.png"
const val EPHARMACY_PDP_INFO_DATA_TYPE = "obat_keras_info"
const val EPHARMACY_CHOOSER_APPLINK = "tokopedia://epharmacy/chooser/"

const val EPHARMACY_TICKER_ICON = "https://images.tokopedia.net/img/android/res/singleDpi/epharmacy_chooser_mini_cons_doc.png"
const val EPHARMACY_TICKER_BACKGROUND = "https://images.tokopedia.net/img/android/res/singleDpi/epharmacy_mini_consult_ticker_background.png"

enum class EPharmacyConsultationStatus(val status: Int) {
    EXPIRED(0),
    ACTIVE(1),
    APPROVED(2),
    REJECTED(4)
}

const val EPHARMACY_RX_IMAGE = "https://images.tokopedia.net/img/android/res/singleDpi/epharmacy_sucess_image_uploaded_count.png"
