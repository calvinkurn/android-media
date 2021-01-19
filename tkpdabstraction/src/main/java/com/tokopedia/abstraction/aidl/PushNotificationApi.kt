package com.tokopedia.abstraction.aidl

import android.content.Context
import com.tokopedia.appaidl.AidlApi

class PushNotificationApi(
        context: Context,
        listener: ReceiverListener
) : AidlApi(context, listener) {

    private val notificationService = "com.tokopedia.abstraction.aidl.service.PushNotificationService"

    fun bindService() {
        bindService(serviceName = notificationService)
    }

}