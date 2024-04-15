package com.tokopedia.notification.common.service

import android.content.Intent
import android.os.Bundle
import com.tokopedia.appaidl.data.componentTargetName
import com.tokopedia.appaidl.service.AidlRemoteService
import com.tokopedia.notification.common.data.UserKey.IS_LOGIN
import com.tokopedia.notification.common.data.UserKey.USER_ID
import com.tokopedia.notification.common.data.UserKey.IV_KEY_PUSH_NOTIF
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.util.EncoderDecoder
import com.tokopedia.utils.appsignature.AppSignatureUtil

class PushNotificationService : AidlRemoteService() {

    private val userSession by lazy { UserSession(applicationContext) }

    override fun onBind(intent: Intent) = binder()

    override fun dataShared(tag: String) {
        val data = Bundle()
        if (userSession.isLoggedIn && isMatchedWithTokopediaAppSignature()) {
            val userIdEncrypted = EncoderDecoder.Encrypt(userSession.userId, IV_KEY_PUSH_NOTIF)
            data.apply {
                putBoolean(IS_LOGIN, true)
                putString(USER_ID, userIdEncrypted)
            }
        } else {
            data.putBoolean(IS_LOGIN, false)
        }

        broadcastResult(tag, data)
    }

    private fun isMatchedWithTokopediaAppSignature(): Boolean {
        return AppSignatureUtil.isSignatureMatch(
            AppSignatureUtil.getAppSignature(
                applicationContext,
                componentTargetName()
            ),
            AppSignatureUtil.TOKO_APP_SIGNATURE
        )
    }
}
