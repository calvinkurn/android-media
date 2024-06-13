package com.tokopedia.applink.user

import android.net.Uri
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

            deeplink.startsWith(ApplinkConstInternalGlobal.ADVANCED_SETTING) -> deeplink.replace(ApplinkConstInternalGlobal.ADVANCED_SETTING, ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT)
            deeplink.startsWith(ApplinkConstInternalGlobal.GENERAL_SETTING) -> deeplink.replace(ApplinkConstInternalGlobal.GENERAL_SETTING, ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT)

            deeplink == ApplinkConst.User.ADD_PIN_ONBOARD -> ApplinkConstInternalUserPlatform.ADD_PIN_ONBOARDING
            deeplink == ApplinkConst.SETTING_PROFILE -> ApplinkConstInternalUserPlatform.SETTING_PROFILE
            deeplink == ApplinkConst.User.SETTING_ACCOUNT -> ApplinkConstInternalUserPlatform.ACCOUNT_SETTING
            deeplink == ApplinkConst.User.INPUT_INACTIVE_NUMBER -> ApplinkConstInternalUserPlatform.INPUT_OLD_PHONE_NUMBER
            deeplink == ApplinkConst.User.ADD_PHONE -> ApplinkConstInternalUserPlatform.ADD_PHONE
            deeplink == ApplinkConst.PRIVACY_CENTER -> ApplinkConstInternalUserPlatform.PRIVACY_CENTER
            deeplink == ApplinkConst.User.DSAR -> ApplinkConstInternalUserPlatform.DSAR
            deeplink.startsWith(ApplinkConst.LOGIN) -> ApplinkConstInternalUserPlatform.LOGIN
            deeplink == ApplinkConst.User.QR_LOGIN -> ApplinkConstInternalUserPlatform.QR_LOGIN
            deeplink.startsWith(ApplinkConst.User.REGISTER_INIT) || deeplink.startsWith(ApplinkConst.User.REGISTER) -> ApplinkConstInternalUserPlatform.INIT_REGISTER
            deeplink == ApplinkConst.User.RESET_PASSWORD -> ApplinkConstInternalUserPlatform.FORGOT_PASSWORD
            deeplink == ApplinkConst.User.HAS_PASSWORD -> ApplinkConstInternalUserPlatform.HAS_PASSWORD
            deeplink == ApplinkConst.ADD_NAME_PROFILE -> ApplinkConstInternalUserPlatform.MANAGE_NAME
            deeplink == ApplinkConst.User.PROFILE_COMPLETION -> ApplinkConstInternalUserPlatform.PROFILE_COMPLETION
            deeplink == ApplinkConst.User.OTP_PUSH_NOTIF_RECEIVER -> ApplinkConstInternalUserPlatform.OTP_PUSH_NOTIF_RECEIVER

            deeplink.startsWithPattern(ApplinkConst.OTP) || deeplink.startsWithPattern(
                ApplinkConstInternalUserPlatform.COTP
            ) -> ApplinkConstInternalUserPlatform.COTP

            deeplink == ApplinkConst.CREATE_SHOP -> ApplinkConstInternalUserPlatform.LANDING_SHOP_CREATION

            deeplink.startsWithPattern(ApplinkConst.GOTO_KYC) || deeplink.startsWithPattern(
                ApplinkConstInternalUserPlatform.GOTO_KYC
            ) -> getApplinkGotoKyc(deeplink)

            deeplink.startsWith(ApplinkConst.GOTO_KYC_WEBVIEW) -> ApplinkConstInternalUserPlatform.GOTO_KYC_WEBVIEW
            else -> deeplink
        }
    }

    fun getAccountInternalApplink(deeplink: String): String {
        return deeplink.replace(ApplinkConst.User.ACCOUNT, ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT)
    }

    private fun getApplinkGotoKyc(deeplink: String): String {
        return if (isRollenceGotoKycActivated()) {
            deeplink.replace(
                "${ApplinkConst.APPLINK_CUSTOMER_SCHEME}://goto-kyc",
                "${ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER}/goto-kyc"
            )
        } else {
            ApplinkConstInternalUserPlatform.KYC_INFO_BASE + "?" + deeplink.substringAfter("?")
        }
    }

    fun getLoginByQr(uri: Uri): String = "${ApplinkConstInternalUserPlatform.QR_LOGIN}?${uri.query}"

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

    fun getKYCInternalApplink(): String {
        return ApplinkConstInternalUserPlatform.KYC_INFO_BASE
    }

    fun getKYCFormInternalApplink(): String {
        return ApplinkConstInternalUserPlatform.KYC_FORM_BASE
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
