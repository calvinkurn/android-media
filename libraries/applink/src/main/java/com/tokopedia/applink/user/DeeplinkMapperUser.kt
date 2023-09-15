package com.tokopedia.applink.user

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

object DeeplinkMapperUser {

    const val KEY_ROLLENCE_PROFILE_MANAGEMENT_M2 = "M2_Profile_Mgmt"
    const val ROLLENCE_GOTO_KYC = "goto_kyc_apps"
    const val ROLLENCE_PRIVACY_CENTER = "privacy_center_and_3"

    fun getRegisteredNavigationUser(deeplink: String): String {
        return when {
            deeplink.startsWith(ApplinkConst.CHANGE_INACTIVE_PHONE) -> deeplink.replace(
                ApplinkConst.CHANGE_INACTIVE_PHONE,
                ApplinkConstInternalUserPlatform.CHANGE_INACTIVE_PHONE
            )
            deeplink == ApplinkConst.ADD_PIN_ONBOARD -> ApplinkConstInternalUserPlatform.ADD_PIN_ONBOARDING
            deeplink.startsWith(ApplinkConstInternalGlobal.ADVANCED_SETTING) -> ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT
            deeplink.startsWith(ApplinkConstInternalGlobal.GENERAL_SETTING) -> ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT
            deeplink == ApplinkConst.SETTING_PROFILE -> getProfileApplink()
            deeplink == ApplinkConstInternalUserPlatform.SETTING_PROFILE -> getProfileApplink()
            deeplink == ApplinkConst.INPUT_INACTIVE_NUMBER -> ApplinkConstInternalUserPlatform.INPUT_OLD_PHONE_NUMBER
            deeplink == ApplinkConst.ADD_PHONE -> ApplinkConstInternalUserPlatform.ADD_PHONE
            deeplink == ApplinkConst.PRIVACY_CENTER -> getApplinkPrivacyCenter()
            deeplink == ApplinkConst.User.DSAR -> ApplinkConstInternalUserPlatform.DSAR
            deeplink.startsWithPattern(ApplinkConst.GOTO_KYC) || deeplink.startsWithPattern(ApplinkConstInternalUserPlatform.GOTO_KYC) -> getApplinkGotoKyc(deeplink)
            deeplink.startsWith(ApplinkConst.GOTO_KYC_WEBVIEW) -> ApplinkConstInternalUserPlatform.GOTO_KYC_WEBVIEW
            else -> deeplink
        }
    }

    private fun getProfileApplink(): String {
        return if (isProfileManagementM2Activated()) {
            ApplinkConstInternalUserPlatform.PROFILE_MANAGEMENT
        } else {
            ApplinkConstInternalUserPlatform.SETTING_PROFILE
        }
    }

    fun isProfileManagementM2Activated(): Boolean {
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString(KEY_ROLLENCE_PROFILE_MANAGEMENT_M2)
            .isNotEmpty()
    }

    private fun getApplinkGotoKyc(deeplink: String): String {
        return if (isRollenceGotoKycActivated() && !GlobalConfig.isSellerApp()) {
            deeplink.replace("${ApplinkConst.APPLINK_CUSTOMER_SCHEME}://", "${ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER}/")
        } else {
            ApplinkConstInternalUserPlatform.KYC_INFO_BASE + "?" + deeplink.substringAfter("?")
        }
    }

    fun isRollenceGotoKycActivated(): Boolean {
        val rollence = getAbTestPlatform()
            .getFilteredKeyByKeyName(ROLLENCE_GOTO_KYC)
        return if (rollence.isNotEmpty()) {
            getAbTestPlatform().getString(ROLLENCE_GOTO_KYC).isNotEmpty()
        } else {
            true
        }
    }

    private fun getApplinkPrivacyCenter(): String {
        return if (isRollencePrivacyCenterActivated()) {
            ApplinkConstInternalUserPlatform.PRIVACY_CENTER
        } else {
            ApplinkConsInternalHome.HOME_NAVIGATION
        }
    }

    fun isRollencePrivacyCenterActivated(): Boolean {
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
