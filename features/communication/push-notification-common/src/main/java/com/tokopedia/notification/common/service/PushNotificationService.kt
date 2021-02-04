package com.tokopedia.notification.common.service

import android.content.Intent
import android.os.Bundle
import com.tokopedia.notification.common.data.UserKey
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.aidl.service.AidlRemoteService

class PushNotificationService: AidlRemoteService() {

    private val userSession by lazy { UserSession(applicationContext) }

    override fun onBind(intent: Intent) = binder()

    override fun dataShared(tag: String) {
        val data = Bundle()
        if (userSession.isLoggedIn) {
            data.apply {
                putBoolean(UserKey.IS_LOGIN, true)
                putString(UserKey.NAME, userSession.name)
                putString(UserKey.EMAIL, userSession.email)
                putString(UserKey.USER_ID, userSession.userId)
            }
        } else {
            data.putBoolean(UserKey.IS_LOGIN, false)
        }

        broadcastResult(tag, data)
    }

}