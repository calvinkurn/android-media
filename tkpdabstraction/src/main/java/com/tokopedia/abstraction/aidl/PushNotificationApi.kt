package com.tokopedia.abstraction.aidl

import android.content.Context
import android.os.Bundle
import com.tokopedia.appaidl.AidlApi

class PushNotificationApi(
        context: Context,
        listener: ReceiverListener
) : AidlApi(context, listener) {

    private val notificationService = "com.tokopedia.abstraction.aidl.service.PushNotificationService"

    fun bindService() {
        bindService(serviceName = notificationService)
    }

    companion object {
        private val listener = object : ReceiverListener {
            override fun onAidlReceive(tag: String, bundle: Bundle?) {}
            override fun onAidlError() {}
        }

        private var notificationApi: PushNotificationApi? = null

        fun bindService(context: Context) {
            notificationApi = PushNotificationApi(context, listener)
            notificationApi?.bindService()
        }

        fun unbindService() {
            notificationApi?.unbindService()
        }
    }

}