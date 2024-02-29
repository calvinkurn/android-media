package com.tokopedia.applink.user

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

object DeeplinkMapperUser {

    const val ROLLENCE_GOTO_KYC_MA = "goto_kyc_apps"
    const val ROLLENCE_GOTO_KYC_SA = "goto_kyc_sellerapp"
    const val ROLLENCE_GOTO_LOGIN = "scp_goto_login_and"
    const val KEY_SCP_DEBUG = "key_force_scp"
    const val PREF_SCP_DEBUG = "scp_goto_login_and"

    const val ROLLENCE_CVSDK_INTEGRATION = "and_cvsdk_intg"
    private const val REGISTER_PHONE_NUMBER = 116
    private const val REGISTER_EMAIL = 126
    private const val SQCP = 169
    private val WHITELISTED_SCP_OTP_TYPE = listOf<Int>(REGISTER_EMAIL, REGISTER_PHONE_NUMBER, SQCP)

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
            deeplink == ApplinkConst.LOGIN -> getLoginApplink()
            deeplink == ApplinkConst.REGISTER_INIT -> getRegisterApplink()
            deeplink == ApplinkConst.REGISTER -> getRegisterApplink()
            deeplink.startsWithPattern(ApplinkConst.GOTO_KYC) || deeplink.startsWithPattern(ApplinkConstInternalUserPlatform.GOTO_KYC) -> getApplinkGotoKyc(deeplink)
            deeplink.startsWith(ApplinkConst.GOTO_KYC_WEBVIEW) -> ApplinkConstInternalUserPlatform.GOTO_KYC_WEBVIEW
            deeplink.startsWithPattern(ApplinkConst.OTP) || deeplink.startsWithPattern(ApplinkConstInternalUserPlatform.COTP) -> getOtpApplink(deeplink)
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

    private fun getOtpApplink(deeplink: String): String {
        val uriMap = UriUtil.uriQueryParamsToMap(Uri.parse(deeplink))
        val otpType = (uriMap[ApplinkConstInternalUserPlatform.PARAM_OTP_TYPE] ?: "-1").toIntSafely()
        return if (isGotoVerificationEnabled(otpType)) {
            ApplinkConstInternalUserPlatform.SCP_OTP
        } else {
            ApplinkConstInternalUserPlatform.COTP
        }
    }

    private fun isGotoVerificationEnabled(otpType: Int): Boolean {
        return isRollenceGotoVerificationEnabled() && isOtpTypeWhitelisted(otpType)
    }

    private fun isRollenceGotoVerificationEnabled(): Boolean {
        return getAbTestPlatform().getString(ROLLENCE_CVSDK_INTEGRATION).isNotEmpty()
    }

    private fun isOtpTypeWhitelisted(otpType: Int): Boolean {
        return WHITELISTED_SCP_OTP_TYPE.contains(otpType)
    }

    fun isGotoLoginEnabled(): Boolean {
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString(ROLLENCE_GOTO_LOGIN)
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

    fun getRegisteredUserNavigation(deeplink: String): String {
        return deeplink.replace(
            DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH,
            ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER + "/"
        )
    }

    fun isGotoLoginDisabled(): Boolean {
        return RemoteConfigInstance.getInstance()
            .abTestPlatform
            .getString(ROLLENCE_GOTO_LOGIN)
            .isEmpty()
    }

    private fun getAbTestPlatform(): AbTestPlatform =
        RemoteConfigInstance.getInstance().abTestPlatform
}
