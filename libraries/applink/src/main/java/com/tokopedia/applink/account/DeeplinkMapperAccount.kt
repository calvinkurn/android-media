package com.tokopedia.applink.account

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.url.TokopediaUrl

object DeeplinkMapperAccount {

    fun getAccountInternalApplink(context: Context, deeplink: String): String {
        return if (usingOldAccount(context)) {
            deeplink.replace(ApplinkConst.ACCOUNT, ApplinkConstInternalGlobal.OLD_HOME_ACCOUNT)
        } else {
            deeplink.replace(ApplinkConst.ACCOUNT, ApplinkConstInternalGlobal.NEW_HOME_ACCOUNT)
        }
    }

    fun usingOldAccount(context: Context): Boolean{
        val remoteConfig = FirebaseRemoteConfigInstance.get(context)
        return (remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_USING_OLD_ACCOUNT))
    }

    fun getLoginByQrNavigationFromHttp(): String = "${TokopediaUrl.getInstance().WEB}qrcode-login"

    fun getLoginByQr(uri: Uri): String = "${ApplinkConstInternalGlobal.QR_LOGIN}?${uri.query}"
}