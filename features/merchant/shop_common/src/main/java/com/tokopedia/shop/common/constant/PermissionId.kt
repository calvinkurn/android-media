package com.tokopedia.shop.common.constant

import androidx.annotation.StringDef

/**
 * List of Permission Id mapping could be seen in this sheets
 * https://docs.google.com/spreadsheets/d/1ZB5VFuZWQLi4PLFSUsnsBsiHK-6agRfKe77vMLzYqQc/edit?pli=1#gid=233951167
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(

)
annotation class PermissionId {
    companion object {
        const val MANAGE_ORDER = "620"
        const val RESPOND_COMPLAINTS = "630"
        const val MANAGE_FINANCE = "650"
        const val REPLY_REVIEW = "710"
        const val REPLY_DISCUSSION = "720"
        const val REPLY_CHAT = "740"
        const val MANAGE_PRODUCT = "750"
        const val MANAGE_SHOP = "770"
        const val EDIT_STOCK = "800"
    }
}