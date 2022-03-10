package com.tokopedia.applink.user

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform

object DeeplinkMapperUser {

    fun getRegisteredNavigationUser(context: Context, deeplink: String): String {
	val uri = Uri.parse(deeplink)
	return when {
	    deeplink.startsWith(ApplinkConst.CHANGE_INACTIVE_PHONE) -> deeplink.replace(ApplinkConst.CHANGE_INACTIVE_PHONE, ApplinkConstInternalUserPlatform.CHANGE_INACTIVE_PHONE)
	    deeplink == ApplinkConst.ADD_PIN_ONBOARD -> ApplinkConstInternalUserPlatform.ADD_PIN_ONBOARDING
	    deeplink.startsWith(ApplinkConstInternalGlobal.ADVANCED_SETTING) -> ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT
	    deeplink.startsWith(ApplinkConstInternalGlobal.GENERAL_SETTING) -> ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT
	    else -> deeplink
	}
    }


}