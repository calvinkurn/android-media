package com.tokopedia.notification.common

import android.content.Context
import android.os.Bundle
import com.tokopedia.utils.aidl.AidlApi

open class PushNotificationApi(
        context: Context,
        onAidlReceive: (tag: String, bundle: Bundle?) -> Unit,
        onAidlError: () -> Unit
) : AidlApi(context, onAidlReceive, onAidlError) {

    private val notificationService = "com.tokopedia.notification.common.service.PushNotificationService"

    fun bindService() {
        bindService(serviceName = notificationService)
    }

    companion object {
        private var notificationApi: PushNotificationApi? = null

        @JvmStatic
        fun bindService(
                context: Context,
                onAidlReceive: (tag: String, bundle: Bundle?) -> Unit = { _, _ -> },
                onAidlError: () -> Unit = {}
        ) {
            notificationApi = PushNotificationApi(context, onAidlReceive, onAidlError)
            notificationApi?.bindService()
        }
    }

}