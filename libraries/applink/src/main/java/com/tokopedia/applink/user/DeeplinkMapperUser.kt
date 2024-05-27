package com.tokopedia.applink.user

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

object DeeplinkMapperUser {

    const val ROLLENCE_GOTO_KYC_MA = "goto_kyc_apps"
    const val ROLLENCE_GOTO_KYC_SA = "goto_kyc_sellerapp"

    fun getRegisteredNavigationUser(deeplink: String): String {
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
            deeplink == ApplinkConst.PRIVACY_CENTER -> ApplinkConstInternalUserPlatform.PRIVACY_CENTER
            deeplink == ApplinkConst.User.DSAR -> ApplinkConstInternalUserPlatform.DSAR
            deeplink.startsWith(ApplinkConst.LOGIN) -> getLoginApplink()
            deeplink.startsWith(ApplinkConst.REGISTER_INIT) -> getRegisterApplink()
            deeplink.startsWith(ApplinkConst.REGISTER) -> getRegisterApplink()
            deeplink.startsWithPattern(ApplinkConst.GOTO_KYC) || deeplink.startsWithPattern(ApplinkConstInternalUserPlatform.GOTO_KYC) -> getApplinkGotoKyc(deeplink)
            deeplink.startsWith(ApplinkConst.GOTO_KYC_WEBVIEW) -> ApplinkConstInternalUserPlatform.GOTO_KYC_WEBVIEW
            deeplink.startsWithPattern(ApplinkConst.OTP) || deeplink.startsWithPattern(ApplinkConstInternalUserPlatform.COTP) -> getOtpApplink()
            else -> deeplink
        }
    }

    private fun getRegisterApplink(): String = ApplinkConstInternalUserPlatform.INIT_REGISTER

    private fun getLoginApplink(): String = ApplinkConstInternalUserPlatform.LOGIN

    private fun getOtpApplink(): String = ApplinkConstInternalUserPlatform.COTP

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

    fun getRegisteredUserNavigation(deeplink: String): String {
        return deeplink.replace(
            DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH,
            ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER + "/"
        )
    }

    private fun getAbTestPlatform(): AbTestPlatform =
        RemoteConfigInstance.getInstance().abTestPlatform
}
