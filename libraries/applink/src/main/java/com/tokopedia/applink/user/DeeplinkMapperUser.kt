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
    const val ROLLENCE_GOTO_KYC_MA = "goto_kyc_apps"
    const val ROLLENCE_GOTO_KYC_SA = "goto_kyc_sellerapp"
    const val ROLLENCE_PRIVACY_CENTER = "privacy_center_and_3"
    const val ROLLENCE_GOTO_LOGIN = "scp_goto_login_and"
    const val ROLLENCE_FUNDS_AND_INVESTMENT_COMPOSE = "android_fundinvest"

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
            deeplink == ApplinkConst.LOGIN -> getLoginApplink()
            deeplink == ApplinkConst.REGISTER_INIT -> getRegisterApplink()
            deeplink == ApplinkConst.REGISTER -> getRegisterApplink()
            deeplink.startsWithPattern(ApplinkConst.GOTO_KYC) || deeplink.startsWithPattern(ApplinkConstInternalUserPlatform.GOTO_KYC) -> getApplinkGotoKyc(deeplink)
            deeplink.startsWith(ApplinkConst.GOTO_KYC_WEBVIEW) -> ApplinkConstInternalUserPlatform.GOTO_KYC_WEBVIEW
            deeplink == ApplinkConst.OTP -> getOtpApplink()
            else -> deeplink
        }
    }

    private fun getRegisterApplink(): String {
        return if (isGotoLoginEnabled() && GlobalConfig.isSellerApp().not()) {
            ApplinkConstInternalUserPlatform.SCP_LOGIN
        } else {
            ApplinkConstInternalUserPlatform.INIT_REGISTER
        }
    }

    private fun getLoginApplink(): String {
        return if (isGotoLoginEnabled() && GlobalConfig.isSellerApp().not()) {
            ApplinkConstInternalUserPlatform.SCP_LOGIN
        } else {
            ApplinkConstInternalUserPlatform.LOGIN
        }
    }

    private fun getOtpApplink(): String {
        return if (isGotoVerificationEnabled()) {
            ApplinkConstInternalUserPlatform.SCP_OTP
        } else {
            ApplinkConstInternalUserPlatform.COTP
        }
    }

    private fun isGotoVerificationEnabled(): Boolean {
        return true
    }

    private fun getProfileApplink(): String {
        return if (isProfileManagementM2Activated()) {
            ApplinkConstInternalUserPlatform.PROFILE_MANAGEMENT
        } else {
            ApplinkConstInternalUserPlatform.SETTING_PROFILE
        }
    }

    fun isGotoLoginEnabled(): Boolean {
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString(ROLLENCE_GOTO_LOGIN)
            .isNotEmpty()
    }

    fun isProfileManagementM2Activated(): Boolean {
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString(KEY_ROLLENCE_PROFILE_MANAGEMENT_M2)
            .isNotEmpty()
    }

    private fun getApplinkGotoKyc(deeplink: String): String {
        return if (isRollenceGotoKycActivated()) {
            deeplink.replace("${ApplinkConst.APPLINK_CUSTOMER_SCHEME}://goto-kyc", "${ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER}/goto-kyc")
        } else {
            ApplinkConstInternalUserPlatform.KYC_INFO_BASE + "?" + deeplink.substringAfter("?")
        }
    }

    fun isRollenceGotoKycActivated(): Boolean {
        val rollenceKey = if (GlobalConfig.isSellerApp()) {
            ROLLENCE_GOTO_KYC_SA
        } else {
            ROLLENCE_GOTO_KYC_MA
        }

        val rollence = getAbTestPlatform()
            .getFilteredKeyByKeyName(rollenceKey)
        return if (rollence.isNotEmpty()) {
            getAbTestPlatform().getString(rollenceKey).isNotEmpty()
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

    fun isGotoLoginDisabled(): Boolean {
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString(ROLLENCE_GOTO_LOGIN)
            .isEmpty()
    }

    fun isFundsAndInvestmentComposeActivated(): Boolean {
        return getAbTestPlatform()
            .getString(ROLLENCE_FUNDS_AND_INVESTMENT_COMPOSE)
            .isNotEmpty()
    }

    private fun getAbTestPlatform(): AbTestPlatform =
        RemoteConfigInstance.getInstance().abTestPlatform


}
