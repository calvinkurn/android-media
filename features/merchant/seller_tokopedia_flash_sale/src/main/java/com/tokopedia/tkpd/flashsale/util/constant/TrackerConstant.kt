package com.tokopedia.tkpd.flashsale.util.constant

object TrackerConstant {
    const val BUSINESS_UNIT = "physical goods"
    const val CURRENT_SITE = "tokopediaseller"
    const val EVENT = "clickPG"
    object Key{
        const val TRACKER_ID = "trackerId"
    }
    object EventActionValue {
        const val CLICK_MANAGE_PRODUCT_DISCOUNT = "click atur diskon"
        const val CLICK_APPLY_PRODUCT_DISCOUNT = "click ajukan"
    }
    object EventCategoryValue{
        const val EVENT_CATEGORY_FLASH_SALE_MANAGE_PRODUCT = "flash sale - atur produk"
    }
    object TrackerIdValue{
        const val TRACKER_ID_CLICK_MANAGE_PRODUCT_DISCOUNT = "37216"
        const val TRACKER_ID_APPLY_MANAGE_PRODUCT_DISCOUNT = "37217"
    }
    const val SINGLE_LOCATION = "single loc"
    const val MULTI_LOCATION = "multilocation"
}
