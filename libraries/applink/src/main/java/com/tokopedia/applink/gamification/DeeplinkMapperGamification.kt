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

    fun getDailyGiftBoxDeeplink(deeplink: String): String {
        return deeplink.replace(ApplinkConst.Gamification.DAILY_GIFT_BOX, ApplinkConstInternalGlobal.GAMIFICATION_DAILY_GIFT)
    }

    fun getGiftBoxTapTapDeeplink(deeplink: String): String {
        return deeplink.replace(ApplinkConst.Gamification.GIFT_TAP_TAP, ApplinkConstInternalGlobal.GAMIFICATION_TAP_TAP_GIFT)
    }
}