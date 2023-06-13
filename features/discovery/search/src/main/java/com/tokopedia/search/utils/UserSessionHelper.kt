package com.tokopedia.search.utils

import com.tokopedia.user.session.UserSessionInterface

internal const val DEFAULT_USER_ID = "0"

internal fun getUserId(userSession: UserSessionInterface) =
    if (userSession.isLoggedIn) userSession.userId
    else DEFAULT_USER_ID
