package com.tokopedia.inbox.view.ext

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.user.session.UserSessionInterface

fun UserSessionInterface.getRoleName(@RoleType role: Int): String {
    return when (role) {
        RoleType.BUYER -> name
        RoleType.SELLER -> shopName
        else -> ""
    }
}