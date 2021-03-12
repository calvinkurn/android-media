package com.tokopedia.notification.common

import android.content.Context
import android.os.Bundle
import com.tokopedia.appaidl.AidlApi
import com.tokopedia.notification.common.service.PushNotificationService

open class PushNotificationApi(
        onAidlReceive: (tag: String, bundle: Bundle?) -> Unit,
        onAidlError: () -> Unit
) : AidlApi(onAidlReceive, onAidlError) {

    fun bindService(context: Context) {
        try {
            bindService(
                    context = context,
                    serviceName = PushNotificationService::class.java.name
            )
        } catch (ignored: Exception) { }
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