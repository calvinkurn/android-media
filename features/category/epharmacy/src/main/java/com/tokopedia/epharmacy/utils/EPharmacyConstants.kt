package com.tokopedia.epharmacy.utils

const val EXTRA_ORDER_ID_LONG = "extra_order_id_long"
const val EXTRA_CHECKOUT_ID_STRING = "extra_checkout_id_string"
const val EXTRA_ENTRY_POINT_STRING = "extra_entry_point_string"
const val EXTRA_SOURCE_STRING = "source"
const val EXTRA_CONSULTATION_WEB_LINK_STRING = "extra_consultation_web_link_string"
const val EPHARMACY_MINI_CONSULTATION_RESULT_EXTRA = "epharmacy_mini_consultation_result"

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

const val REMINDER_TYPE = "reminder_type"
const val CONSULTATION_SOURCE_ID = "consultation_source_id"
