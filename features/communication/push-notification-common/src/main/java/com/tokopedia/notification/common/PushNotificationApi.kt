package com.tokopedia.notification.common

import android.content.Context
import android.os.Bundle
import com.tokopedia.notification.common.service.PushNotificationService
import com.tokopedia.appaidl.AidlApi

open class PushNotificationApi(
        onAidlReceive: (tag: String, bundle: Bundle?) -> Unit,
        onAidlError: () -> Unit
) : AidlApi(onAidlReceive, onAidlError) {

    private val notificationService = "com.tokopedia.notification.common.service.PushNotificationService"

    fun bindService(context: Context) {
        bindService(
                context = context,
                serviceName = PushNotificationService::class.java.canonicalName?: notificationService
        )
    }

    companion object {
        private var pushNotificationApi: PushNotificationApi? = null

        @JvmStatic
        fun bindService(
                context: Context,
                onAidlReceive: (tag: String, bundle: Bundle?) -> Unit = { _, _ -> },
                onAidlError: () -> Unit = {}
        ) {
            pushNotificationApi = PushNotificationApi(onAidlReceive, onAidlError)
            pushNotificationApi?.bindService(context)
        }
    }

}