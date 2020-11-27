package com.tokopedia.shop.common.constant

import androidx.annotation.StringDef

/**
 * List of RBAC Access Mapping could be seen in this sheets
 * https://docs.google.com/spreadsheets/d/1ZB5VFuZWQLi4PLFSUsnsBsiHK-6agRfKe77vMLzYqQc/edit#gid=233951167
 */

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(
        AdminPermissionGroup.STOCK,
        AdminPermissionGroup.SHOP,
        AdminPermissionGroup.PRODUCT,
        AdminPermissionGroup.CHAT,
        AdminPermissionGroup.STATISTIC,
        AdminPermissionGroup.ADS_PROMOTION,
        AdminPermissionGroup.FINANCIAL,
        AdminPermissionGroup.ORDER,
        AdminPermissionGroup.OWNER_MANAGE
)
annotation class AdminPermissionGroup {
    companion object {
        const val STOCK = "790"
        const val SHOP = "780"
        const val PRODUCT = "760"
        const val CHAT = "740"
        const val STATISTIC = "700"
        const val ADS_PROMOTION = "680"
        const val FINANCIAL = "660"
        const val ORDER = "640"
        const val OWNER_MANAGE = "600"
    }
}