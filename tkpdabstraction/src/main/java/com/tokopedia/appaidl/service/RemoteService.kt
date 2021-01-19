package com.tokopedia.appaidl.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import com.tokopedia.appaidl.AppApi
import com.tokopedia.appaidl.data.UserKey
import com.tokopedia.appaidl.data.componentTargetName
import com.tokopedia.user.session.UserSession

class RemoteService: Service() {

    private val userSession by lazy { UserSession(applicationContext) }

    override fun onBind(intent: Intent) = binder
    
    private fun broadcastResult(tag: String, data: Bundle) {
        sendBroadcast(Intent().apply {
            `package` = componentTargetName()
            action = tag
            putExtras(data)
        })
    }
    
    private fun getUserSession(tag: String) {
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

    private val binder = object : AppApi.Stub() {
        override fun send(tag: String) {
            getUserSession(tag)
        }
    }

}