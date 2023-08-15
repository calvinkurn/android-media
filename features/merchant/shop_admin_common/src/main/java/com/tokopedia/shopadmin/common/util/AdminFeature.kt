package com.tokopedia.shopadmin.common.util

import androidx.annotation.StringDef

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    AdminFeature.SALDO,
    AdminFeature.NEW_ORDER,
    AdminFeature.READY_TO_SHIP_ORDER,
    AdminFeature.ORDER_HISTORY,
    AdminFeature.MANAGE_PRODUCT,
    AdminFeature.ADD_PRODUCT,
    AdminFeature.REVIEW,
    AdminFeature.DISCUSSION,
    AdminFeature.COMPLAINT,
    AdminFeature.MANAGE_SHOP,
    AdminFeature.STATISTIC,
    AdminFeature.ADS_AND_PROMOTION,
    AdminFeature.SHOP_SCORE,
    AdminFeature.SHOP_SETTINGS_INFO,
    AdminFeature.SHOP_SETTINGS_NOTES,
    AdminFeature.SHOP_OPERATIONAL_HOURS,
    AdminFeature.SHOP_SETTING_ADDR,
    AdminFeature.SHIPPING_EDITOR
)
annotation class AdminFeature {
    companion object {
        const val SALDO = "SALDO"
        const val NEW_ORDER = "NEW_ORDER"
        const val READY_TO_SHIP_ORDER = "READY_ORDER"
        const val ORDER_HISTORY = "ORDER_HISTORY"
        const val MANAGE_PRODUCT = "MANAGE_PRODUCT"
        const val ADD_PRODUCT = "ADD_PRODUCT"
        const val REVIEW = "REVIEW"
        const val DISCUSSION = "TALK"
        const val COMPLAINT = "COMPLAINT"
        const val MANAGE_SHOP = "SHOP"
        const val STATISTIC = "STATISTIC"
        const val ADS_AND_PROMOTION = "PROMO"
        const val SHOP_SCORE = "SHOP_SCORE"
        const val SHOP_SETTINGS_INFO = "SHOP_SETTINGS_INFO"
        const val SHOP_SETTINGS_NOTES = "SHOP_SETTINGS_NOTES"
        const val SHOP_OPERATIONAL_HOURS = "SHOP_OPERATIONAL_HOURS"
        const val SHOP_SETTING_ADDR = "SHOP_SETTING_ADDRESS"
        const val SHIPPING_EDITOR = "SHIPPING_EDITOR"
    }
}
