package com.tokopedia.epharmacy.utils


const val EXTRA_ORDER_ID_LONG = "extra_order_id_long"
const val EXTRA_CHECKOUT_ID = "extra_checkout_id"
const val DEFAULT_ZERO_VALUE = 0L

const val MAX_MEDIA_ITEM = 5

const val MEDIA_PICKER_REQUEST_CODE = 10020
const val EPHARMACY_REQUEST_CODE = 10021
const val EPHARMACY_PRESCRIPTION_IDS = "epharmacy_prescription_ids"

const val STATIC_INFO_COMPONENT = "static info component"
const val PRESCRIPTION_COMPONENT = "prescription component"
const val POSITION_PRESCRIPTION_COMPONENT = 0

const val PRODUCT_COMPONENT = "product component"

const val PRESCRIPTION_VIEW_TYPE = 0
const val PRODUCT_VIEW_TYPE = 1

const val EPHARMACY_SCREEN_NAME = "epharmacy page"

const val UPLOAD_ORDER_ID_KEY = "order_id"
const val UPLOAD_CHECKOUT_ID_KEY = "checkout_id"

const val EPHARMACY_TNC_LINK = "https://www.tokopedia.com/help/article/syarat-dan-ketentuan-tokopedia-kesehatan"

const val EPHARMACY_CHECK_BUTTON_KEY ="epharmacy_check_prescription"

enum class EPharmacyButtonKey(val key : String) {
    CHECK("epharmacy_check_prescription"),
    RE_UPLOAD("epharmacy_reupload_prescription"),
    DONE("epharmacy_done_prescription"),
    DONE_DISABLED("epharmacy_done_disabled")
}

enum class EPharmacyPrescriptionStatus(val status : String){
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    ACTIVE("ACTIVE"),
    SELECTED("SELECTED")
}

const val EPharmacyImageQuality = 100
const val EPharmacyMinImageQuality = 80
const val EPharmacyImageQualityDecreaseFactor = 0.8

const val FIRST_INDEX = 0
const val MAX_BYTES = 6_000_000L
const val MAX_MEDIA_SIZE_PICKER = 4_000_000L
