package com.tokopedia.notification.common

import android.content.Context
import android.os.Bundle
import com.tokopedia.notification.common.service.PushNotificationService
import com.tokopedia.appaidl.AidlApi

open class PushNotificationAidlApi(
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

}

enum class PushNotificationApi {
    INSTANCE;
    private lateinit var notificationApi: PushNotificationAidlApi

    fun bindService(
            context: Context,
            onAidlReceive: (tag: String, bundle: Bundle?) -> Unit = { _, _ -> },
            onAidlError: () -> Unit = {}
    ) {
        if (::notificationApi.isInitialized) {
            notificationApi = PushNotificationAidlApi(onAidlReceive, onAidlError)
        }

        notificationApi.bindService(context)
    }
}