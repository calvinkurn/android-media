package com.tokopedia.notification.common

import android.content.Context
import android.os.Bundle
import com.tokopedia.notification.common.service.PushNotificationService
import com.tokopedia.utils.aidl.AidlApi

open class PushNotificationApi(
        onAidlReceive: (tag: String, bundle: Bundle?) -> Unit,
        onAidlError: () -> Unit
) : AidlApi(onAidlReceive, onAidlError) {

    fun bindService(context: Context) {
        val notificationService = "com.tokopedia.notification.common.service.PushNotificationService"
        bindService(
                context = context,
                serviceName = PushNotificationService::class.java.canonicalName?: notificationService
        )
    }

    companion object {
        private var notificationApi: PushNotificationApi? = null

        @JvmStatic
        fun bindService(
                context: Context,
                onAidlReceive: (tag: String, bundle: Bundle?) -> Unit = { _, _ -> },
                onAidlError: () -> Unit = {}
        ) {
            notificationApi = PushNotificationApi(onAidlReceive, onAidlError)
            notificationApi?.bindService(context)
        }
    }

}