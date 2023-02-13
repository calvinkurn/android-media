package com.tokopedia.applink.user

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

object DeeplinkMapperUser {

    private const val ROLLENCE_PRIVACY_CENTER = "privacy_center_and"

    fun getRegisteredNavigationUser(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return when {
            deeplink.startsWith(ApplinkConst.CHANGE_INACTIVE_PHONE) -> deeplink.replace(
                ApplinkConst.CHANGE_INACTIVE_PHONE,
                ApplinkConstInternalUserPlatform.CHANGE_INACTIVE_PHONE
            )
            deeplink == ApplinkConst.ADD_PIN_ONBOARD -> ApplinkConstInternalUserPlatform.ADD_PIN_ONBOARDING
            deeplink.startsWith(ApplinkConstInternalGlobal.ADVANCED_SETTING) -> ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT
            deeplink.startsWith(ApplinkConstInternalGlobal.GENERAL_SETTING) -> ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT
            deeplink == ApplinkConst.SETTING_PROFILE -> ApplinkConstInternalUserPlatform.SETTING_PROFILE
            deeplink == ApplinkConst.INPUT_INACTIVE_NUMBER -> ApplinkConstInternalUserPlatform.INPUT_OLD_PHONE_NUMBER
            deeplink == ApplinkConst.ADD_PHONE -> ApplinkConstInternalUserPlatform.ADD_PHONE
            deeplink == ApplinkConst.PRIVACY_CENTER -> getApplinkPrivacyCenter()
            deeplink.startsWithPattern(ApplinkConst.GOTO_KYC) -> ApplinkConstInternalUserPlatform.GOTO_KYC
            else -> deeplink
        }
    }

    private fun getApplinkPrivacyCenter(): String {
        return if (isRollencePrivacyCenterActivated()) {
            ApplinkConstInternalUserPlatform.PRIVACY_CENTER
        } else {
            ApplinkConsInternalHome.HOME_NAVIGATION
        }
    }

    private fun isRollencePrivacyCenterActivated(): Boolean {
        return getAbTestPlatform()
            .getString(ROLLENCE_PRIVACY_CENTER)
            .isNotEmpty()
    }

    fun getRegisteredUserNavigation(deeplink: String): String {
        return deeplink.replace(
            DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH,
            ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER+"/")
    }

    private fun getAbTestPlatform(): AbTestPlatform =
        RemoteConfigInstance.getInstance().abTestPlatform


}
