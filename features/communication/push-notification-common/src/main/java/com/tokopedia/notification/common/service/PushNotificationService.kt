package com.tokopedia.notification.common.service

import android.content.Intent
import android.os.Bundle
import com.tokopedia.notification.common.data.UserKey.IS_LOGIN
import com.tokopedia.notification.common.data.UserKey.NAME
import com.tokopedia.notification.common.data.UserKey.EMAIL
import com.tokopedia.notification.common.data.UserKey.USER_ID
import com.tokopedia.user.session.UserSession
import com.tokopedia.appaidl.service.AidlRemoteService

class PushNotificationService : AidlRemoteService() {

    private val userSession by lazy { UserSession(applicationContext) }

    override fun onBind(intent: Intent) = binder()

    override fun dataShared(tag: String) {
        val data = Bundle()
        if (userSession.isLoggedIn) {
            data.apply {
                putBoolean(IS_LOGIN, true)
                putString(NAME, userSession.name)
                putString(EMAIL, userSession.email)
                putString(USER_ID, userSession.userId)
            }
        } else {
            data.putBoolean(IS_LOGIN, false)
        }

        broadcastResult(tag, data)
    }

}