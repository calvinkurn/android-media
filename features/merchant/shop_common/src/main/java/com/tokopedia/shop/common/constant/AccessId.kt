package com.tokopedia.shop.common.constant

import androidx.annotation.IntDef

/**
 * List of RBAC Access Mapping could be seen in this sheets
 * https://docs.google.com/spreadsheets/d/1ZB5VFuZWQLi4PLFSUsnsBsiHK-6agRfKe77vMLzYqQc/edit?skip_itp2_check=true#gid=1044677632
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(
        AccessId.SOM,
        AccessId.SHOP_SETTING,
        AccessId.STATISTIC,
        AccessId.PRODUCT_LIST,
        AccessId.PRODUCT_ADD,
        AccessId.PRODUCT_EDIT,
        AccessId.PRODUCT_DUPLICATE,
        AccessId.EDIT_STOCK,
        AccessId.COMPLAINT,
        AccessId.DISCUSSION,
        AccessId.REVIEW,
        AccessId.SHOP_SCORE
)
annotation class AccessId {
    companion object {
        const val SOM = 25
        const val SHOP_SETTING = 64
        const val STATISTIC = 55
        const val PRODUCT_LIST = 100
        const val PRODUCT_ADD = 101
        const val PRODUCT_EDIT = 121
        const val PRODUCT_DUPLICATE = 123
        const val EDIT_STOCK = 124
        const val COMPLAINT = 14003
        const val DISCUSSION = 14005
        const val REVIEW = 14011
        const val SHOP_SCORE = 14006
    }
}
