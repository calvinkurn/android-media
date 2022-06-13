package com.tokopedia.inbox.view.ext

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.user.session.UserSessionInterface

fun UserSessionInterface.getRoleName(@RoleType role: Int): String {
    val enRoleName = when (role) {
        RoleType.BUYER -> name
        RoleType.SELLER -> shopName
        else -> ""
    }
    return MethodChecker.fromHtml(enRoleName).toString()
}