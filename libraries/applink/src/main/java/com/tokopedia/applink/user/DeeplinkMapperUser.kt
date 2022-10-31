package com.tokopedia.applink.user

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_ALA_CARTE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_ONLY_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_CALL_BACK
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_KYC_TYPE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_REDIRECT_URL
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_SHOW_INTRO
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

object DeeplinkMapperUser {

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
            else -> deeplink
        }
    }

    // add kyc deeplink mapper if needed
    fun getKycInternalApplink(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val projectId = uri.getQueryParameter(PARAM_PROJECT_ID).orEmpty()
        val showIntro = uri.getQueryParameter(PARAM_SHOW_INTRO).orEmpty()
        val redirectUrl = uri.getQueryParameter(PARAM_REDIRECT_URL).orEmpty()
        val callBack = uri.getQueryParameter(PARAM_CALL_BACK).orEmpty()
        val type = uri.getQueryParameter(PARAM_KYC_TYPE).orEmpty()

        val params = mapOf<String, Any>(
            PARAM_PROJECT_ID to projectId,
            PARAM_SHOW_INTRO to showIntro,
            PARAM_REDIRECT_URL to redirectUrl,
            PARAM_CALL_BACK to callBack,
            PARAM_KYC_TYPE to type,
        )

        val internal =  getRegisteredUserNavigation(deeplink)
        return when {
            internal.startsWith(KYC_ONLY_BASE) -> {
                UriUtil.buildUriAppendParams(KYC_ALA_CARTE, params)
            }
            else -> internal
        }
    }

    fun getRegisteredUserNavigation(deeplink: String): String {
        return deeplink.replace(
            DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH,
            ApplinkConstInternalUserPlatform.NEW_INTERNAL_USER+"/")
    }

    private fun getAbTestPlatform(): AbTestPlatform =
        RemoteConfigInstance.getInstance().abTestPlatform


}
