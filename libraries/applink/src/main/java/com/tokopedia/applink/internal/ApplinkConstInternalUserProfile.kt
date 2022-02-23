package com.tokopedia.applink.internal

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalUserProfile {
    fun getUserProfileLanding(deeplink: String): String {
        val regexExp = "${ApplinkConst.USER_PROFILE_LANDING}/?".toRegex()
        return deeplink.replace(regexExp, ApplinkConstInternalGlobal.USER_PROFILE_LANDING)
    }

    fun getUserProfileFollowers(deeplink: String): String {
        val regexExp = "${ApplinkConst.USER_PROFILE_FOLLOWERS}/?".toRegex()
        return deeplink.replace(regexExp, ApplinkConstInternalGlobal.USER_PROFILE_FOLLOWERS)
    }
}