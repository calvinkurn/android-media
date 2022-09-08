package com.tokopedia.applink.account

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperAccount {

    fun getAccountInternalApplink(deeplink: String): String {
        return deeplink.replace(ApplinkConst.ACCOUNT, ApplinkConstInternalUserPlatform.NEW_HOME_ACCOUNT)
    }

    fun usingOldAccount(context: Context): Boolean{
        val remoteConfig = FirebaseRemoteConfigInstance.get(context)
        return (remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_USING_OLD_ACCOUNT))
    }

    fun getLoginByQr(uri: Uri): String = "${ApplinkConstInternalUserPlatform.QR_LOGIN}?${uri.query}"
}