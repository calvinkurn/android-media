package com.tokopedia.epharmacy.utils

const val EXTRA_ORDER_ID_LONG = "extra_order_id_long"
const val EXTRA_CHECKOUT_ID_STRING = "extra_checkout_id_string"
const val EXTRA_ENTRY_POINT_STRING = "extra_entry_point_string"
const val EXTRA_SOURCE_STRING = "source"

const val DEFAULT_ZERO_VALUE = 0L

const val MAX_MEDIA_ITEM = 5

const val MEDIA_PICKER_REQUEST_CODE = 10020
const val EPHARMACY_PRESCRIPTION_IDS = "epharmacy_prescription_ids"
const val EPHARMACY_GROUP_ID = "epharmacy_group_id"
const val EPHARMACY_ENABLER_NAME = "epharmacy_enabler_name"
const val EPHARMACY_CONSULTATION_SOURCE_ID = "epharmacy_consultation_source_id"

const val STATIC_INFO_COMPONENT = "static info component"
const val PRESCRIPTION_COMPONENT = "prescription component"

const val PRODUCT_COMPONENT = "product component"
const val GROUP_COMPONENT = "group component"
const val TICKER_COMPONENT = "ticker component"
const val SHIMMER_COMPONENT = "shimmer component"
const val SHIMMER_COMPONENT_1 = "shimmer 1"
const val SHIMMER_COMPONENT_2 = "shimmer 2"

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
const val EPHARMACY_CHOOSER_APPLINK = "tokopedia://epharmacy/chooser/"

const val DATA_TYPE = "data_type"
const val ENABLER_NAME = "enabler_name"

const val EPHARMACY_BOTTOM_SHEET_BOTTOM_TNC_IMAGE_URL = "https://images.tokopedia.net/img/green-waves.png"
const val EPHARMACY_BOTTOM_SHEET_BOTTOM_IMAGE_URL = "https://images.tokopedia.net/img/miniconsul-toped-illustration.png"
const val EPHARMACY_PDP_INFO_DATA_TYPE = "obat_keras_info"

const val EPHARMACY_TICKER_BACKGROUND = "https://images.tokopedia.net/img/android/res/singleDpi/epharmacy_mini_consult_ticker_background.png"

enum class EPharmacyConsultationStatus(val status: Int) {
    EXPIRED(0),
    ACTIVE(1),
    APPROVED(2),
    REJECTED(4),
    DOCTOR_NOT_AVAILABLE(5)
}

const val EPHARMACY_CHECKOUT_APPLINK = "tokopedia://checkout"
const val EPHARMACY_CART_APPLINK = "tokopedia://cart"

enum class EPharmacyButtonState(val state: String) {
    ACTIVE("ACTIVE"),
    DISABLED("DISABLED")
}

const val PRESCRIPTION_ATTACH_SUCCESS = "PRESCRIPTION_ATTACH_SUCCESS"

const val IS_OUTSIDE_WORKING_HOURS = "is_outside_working_hours"
const val OPEN_TIME = "open_time"
const val CLOSE_TIME = "close_time"
const val REMINDER_TYPE = "reminder_type"
const val CONSULTATION_SOURCE_ID = "consultation_source_id"

const val YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val HH_MM = "HH:mm"
const val NEW_DATE_FORMAT = "dd MMM yyyy, HH:mm"
const val UTC = "UTC"

const val WEB_LINK_PREFIX = "tokopedia://webview?url="
const val TYPE_DOCTOR_NOT_AVAILABLE_REMINDER = 1

const val ERROR_CODE_OUTSIDE_WORKING_HOUR = 404
const val REMINDER_ILLUSTRATION_IMAGE = "https://images.tokopedia.net/img/pharmacy-illustration.png"

const val EPHARMACY_ANDROID_SOURCE = "ANDROID"
