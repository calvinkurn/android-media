package com.tokopedia.shop.common.constant

import androidx.annotation.IntDef

/**
 * List of RBAC Access Mapping could be seen in this sheets
 * https://docs.google.com/spreadsheets/d/1ZB5VFuZWQLi4PLFSUsnsBsiHK-6agRfKe77vMLzYqQc/edit?skip_itp2_check=true#gid=1044677632
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(
        AccessId.ADS_AND_PROMO,
        AccessId.SHOP_SETTING_ADDRESS,
        AccessId.SHOP_SETTING_ETALASE,
        AccessId.SHOP_SETTING_INFO,
        AccessId.SHOP_SETTING_NOTES,
        AccessId.SHOP_SETTING_SERVICES,
        AccessId.SHOP_SETTING_SHIPMENT,
        AccessId.STATISTIC,
        AccessId.PRODUCT_LIST,
        AccessId.SOM_LIST,
        AccessId.SOM_DETAIL,
        AccessId.SOM_MULTI_ACCEPT,
        AccessId.CHAT_LIST,
        AccessId.CHAT_REPLY,
        AccessId.PRODUCT_ADD,
        AccessId.PRODUCT_EDIT,
        AccessId.PRODUCT_DUPLICATE,
        AccessId.EDIT_STOCK,
        AccessId.CHAT,
        AccessId.COMPLAINT,
        AccessId.DISCUSSION,
        AccessId.REVIEW,
        AccessId.SHOP_SCORE
)
annotation class AccessId {
    companion object {
        const val ADS_AND_PROMO = 49
        const val STATISTIC = 55
        const val SHOP_SETTING_ADDRESS = 61
        const val SHOP_SETTING_ETALASE = 62
        const val SHOP_SETTING_INFO = 64
        const val SHOP_SETTING_NOTES = 65
        const val SHOP_SETTING_SERVICES = 67
        const val SHOP_SETTING_SHIPMENT = 68
        const val PRODUCT_LIST = 100
        const val SOM_LIST = 25
        const val SOM_DETAIL = 26
        const val SOM_MULTI_ACCEPT = 28
        const val CHAT_LIST = 76
        const val CHAT_REPLY = 82
        const val PRODUCT_ADD = 101
        const val PRODUCT_EDIT = 121
        const val PRODUCT_DUPLICATE = 123
        const val EDIT_STOCK = 124
        const val CHAT = 207
        const val COMPLAINT = 14003
        const val DISCUSSION = 14005
        const val REVIEW = 14011
        const val SHOP_SCORE = 14006
    }
}