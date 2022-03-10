package com.tokopedia.applink.user

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperUser {

    fun getRegisteredNavigationUser(context: Context, deeplink: String): String {
	val uri = Uri.parse(deeplink)
	return when {
	    deeplink.startsWith(ApplinkConst.CHANGE_INACTIVE_PHONE) -> deeplink.replace(ApplinkConst.CHANGE_INACTIVE_PHONE, ApplinkConstInternalUserPlatform.CHANGE_INACTIVE_PHONE)
	    deeplink == ApplinkConst.ADD_PIN_ONBOARD -> ApplinkConstInternalUserPlatform.ADD_PIN_ONBOARDING
	    deeplink.startsWith(ApplinkConstInternalGlobal.ADVANCED_SETTING) -> ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT
	    deeplink.startsWith(ApplinkConstInternalGlobal.GENERAL_SETTING) -> ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT
	    deeplink == ApplinkConst.SETTING_PROFILE -> getSettingProfileApplink(context)
	    else -> deeplink
	}
    }

    private fun getSettingProfileApplink(context: Context): String {
        return if(isUseNewProfile(context)) {
            ApplinkConstInternalUserPlatform.NEW_PROFILE_INFO
	} else ApplinkConstInternalUserPlatform.SETTING_PROFILE
    }

    private fun isUseNewProfile(context: Context): Boolean {
	val remoteConfig = FirebaseRemoteConfigInstance.get(context)
	return (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_NEW_PROFILE_INFO , false))
    }
}