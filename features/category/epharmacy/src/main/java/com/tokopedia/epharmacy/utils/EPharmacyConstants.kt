package com.tokopedia.epharmacy.utils


const val EXTRA_ORDER_ID = "extra_order_id"
const val EXTRA_CHECKOUT_ID = "extra_checkout_id"

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
const val GALLERY_IMAGE_VIEW_TYPE = 2
const val OPEN_GALLERY_VIEW_TYPE = 3

const val DONE_TEXT = "Selesai"
const val UPLOAD_TEXT = "Foto Resep"

const val EPHARMACY_SCREEN_NAME = "epharmacy page"

const val UPLOAD_ORDER_ID_KEY = "order_id"
const val UPLOAD_CHECKOUT_ID_KEY = "checkout_id"

const val EPHARMACY_TNC_LINK = "https://www.tokopedia.com/help/article/syarat-dan-ketentuan-tokopedia-kesehatan"

enum class EPharmacyButtonType(val type : String) {
    PRIMARY("primary"),
    SECONDARY("secondary"),
    TERTIARY("tertiary")
}

enum class EPharmacyPrescriptionStatus(val status : String){
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    ACTIVE("ACTIVE"),
    SELECTED("SELECTED")
}
