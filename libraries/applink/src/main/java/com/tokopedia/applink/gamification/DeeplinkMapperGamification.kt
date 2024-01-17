package com.tokopedia.applink.gamification

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object DeeplinkMapperGamification {
    const val KEY_APP_UPDATE_PAGE_URL = "android_app_update_page_url"
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

    fun getKetupatFallBackLink(deeplink: String, context: Context): String {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getString(KEY_APP_UPDATE_PAGE_URL)
    }
}
