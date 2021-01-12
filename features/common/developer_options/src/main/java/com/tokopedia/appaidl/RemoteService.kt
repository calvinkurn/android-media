package com.tokopedia.appaidl

import android.app.Service
import android.content.Intent
import android.os.Bundle
import com.tokopedia.config.GlobalConfig
import com.tokopedia.user.session.UserSession

class RemoteService: Service() {

    private val userSession by lazy { UserSession(applicationContext) }

    override fun onBind(intent: Intent) = binder
    
    private fun broadcastResult(tag: String, data: Bundle) {
        sendBroadcast(Intent().apply {
            action = tag
            putExtras(data)
        })
    }
    
    private fun getUserSession(tag: String) {
        val data = Bundle()
//        if (userSession.isLoggedIn) {
//            data.apply {
//                data.putBoolean("islogin", true)
//                putString("name", userSession.name)
//                putString("email", userSession.email)
//            }
//        } else {
//            data.putBoolean("islogin", false)
//        }

        val sampleData = if (GlobalConfig.isSellerApp()) {
            "ini dari sellerapp, namaku: ${userSession.name}"
        } else {
            "ini dari mainapp, namaku: ${userSession.name}"
        }

        data.apply {
            putString(DATA_INTENT_TEST, sampleData)
        }

        broadcastResult(tag, data)
    }

    private val binder = object : AppApi.Stub() {
        override fun send(tag: String) {
            getUserSession(tag)
        }
    }

    companion object {
        const val DATA_INTENT_TEST = "data_intent"
    }

}