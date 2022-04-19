package com.tokopedia.topchat.chatsearch.util

import android.content.Intent
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.topchat.common.Constant

object Utils {

    fun putExtraForFoldable(intent: Intent, msgId: Long, destUserRole: String) {
        intent.putExtra(Constant.CHAT_CURRENT_ACTIVE, msgId.toString())
        val roleType = if(destUserRole == Constant.ROLE_SHOP) {
            RoleType.BUYER
        } else {
            RoleType.SELLER
        }
        intent.putExtra(Constant.CHAT_USER_ROLE_KEY, roleType)
    }
}