package com.tokopedia.applink.gamification

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal

object DeeplinkMapperGamification {

    fun getGamificationDeeplink(deeplink: String): String {
        return deeplink.replace(ApplinkConst.Gamification.CRACK, ApplinkConstInternalGlobal.GAMIFICATION)
    }

    fun getGamificationTapTapDeeplink(deeplink: String): String {
        return deeplink.replace(ApplinkConst.Gamification.TAP_TAP_MANTAP, ApplinkConstInternalGlobal.GAMIFICATION_TAP_TAP_MANTAP)
    }
}