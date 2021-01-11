package com.tokopedia.seller.menu.common.constant

import androidx.annotation.StringDef

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(
        AdminFeature.SALDO,
        AdminFeature.NEW_ORDER,
        AdminFeature.READY_TO_SHIP_ORDER,
        AdminFeature.MANAGE_PRODUCT,
        AdminFeature.ADD_PRODUCT,
        AdminFeature.REVIEW,
        AdminFeature.DISCUSSION,
        AdminFeature.COMPLAINT,
        AdminFeature.MANAGE_SHOP
)
annotation class AdminFeature {
    companion object {
        const val SALDO = "SALDO"
        const val NEW_ORDER = "NEW_ORDER"
        const val READY_TO_SHIP_ORDER = "READY_ORDER"
        const val MANAGE_PRODUCT = "MANAGE_PRODUCT"
        const val ADD_PRODUCT = "ADD_PRODUCT"
        const val REVIEW = "REVIEW"
        const val DISCUSSION = "TALK"
        const val COMPLAINT = "COMPLAINT"
        const val MANAGE_SHOP = "SHOP"
    }
}