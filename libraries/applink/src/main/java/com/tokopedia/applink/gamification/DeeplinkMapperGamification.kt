package com.tokopedia.applink.gamification

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal

object DeeplinkMapperGamification {

    fun getGamificationDeeplink(deeplink: String): String {
        if (deeplink.startsWith(ApplinkConst.Gamification.CRACK, true))
            return deeplink.replace(ApplinkConst.Gamification.CRACK, ApplinkConstInternalGlobal.GAMIFICATION)
        else if (deeplink.startsWith(ApplinkConst.Gamification.TAP_TAP_MANTAP, true)) {
            return deeplink.replace(ApplinkConst.Gamification.TAP_TAP_MANTAP, ApplinkConstInternalGlobal.GAMIFICATION_TAP_TAP_MANTAP)
        }
        return deeplink
    }
}