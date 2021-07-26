package com.tokopedia.notification.common

import android.content.Context
import android.os.Bundle
import com.tokopedia.appaidl.AidlApi
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notification.common.data.TIMBER_MAX_CHAR_LIMIT
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
        } catch (ignored: Exception) {
            ServerLogger.log(Priority.P2, "AIDL",
                    mapOf("type" to "AIDL", "reason" to "cannot_bind_service", "data" to ignored.toString().take(TIMBER_MAX_CHAR_LIMIT)))
        }
    }

    companion object {
        private var pushNotificationApi: PushNotificationApi? = null

        @JvmStatic
        fun bindService(
                context: Context,
                onAidlReceive: (tag: String, bundle: Bundle?) -> Unit = { _, _bundle ->
                    ServerLogger.log(Priority.P2, "AIDL",
                            mapOf("type" to "receive", "reason" to "onAidlReceive", "data" to _bundle.toString().
                            take(TIMBER_MAX_CHAR_LIMIT)))
                },
                onAidlError: () -> Unit = {}
        ) {
            pushNotificationApi = PushNotificationApi(onAidlReceive, onAidlError)
            pushNotificationApi?.bindService(context)
        }
    }

}