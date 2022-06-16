package com.tokopedia.epharmacy.utils


const val EXTRA_ORDER_ID = "extra_order_id"
const val EXTRA_CHECKOUT_ID = "extra_checkout_id"

const val MAX_MEDIA_ITEM = 3
const val ERROR_MAX_MEDIA_ITEM = 2

const val MEDIA_PICKER_REQUEST_CODE = 1020

const val PRESCRIPTION_COMPONENT = "prescription component"
const val POSITION_PRESCRIPTION_COMPONENT = 0

const val PRODUCT_COMPONENT = "product component"

const val PRESCRIPTION_VIEW_TYPE = 0
const val PRODUCT_VIEW_TYPE = 1
const val GALLERY_IMAGE_VIEW_TYPE = 2
const val OPEN_GALLERY_VIEW_TYPE = 3


enum class EPharmacyButtonType(val type : String) {
    PRIMARY("primary"),
    SECONDARY("secondary")
}

enum class EPharmacyPrescriptionStatus(val status : String){
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    ACTIVE("ACTIVE"),
    SELECTED("SELECTED")
}
