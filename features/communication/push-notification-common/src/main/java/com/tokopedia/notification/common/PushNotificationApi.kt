package com.tokopedia.notification.common

import android.content.Context
import android.os.Bundle
import com.tokopedia.appaidl.AidlApi
import com.tokopedia.notification.common.data.TIMBER_MAX_CHAR_LIMIT
import com.tokopedia.notification.common.data.TIMBER_TAG
import com.tokopedia.notification.common.service.PushNotificationService
import timber.log.Timber

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
            Timber.w("${TIMBER_TAG}bind;reason='cannot_bind_service';data='${ignored.toString().
            take(TIMBER_MAX_CHAR_LIMIT)}'")
        }
    }

    companion object {
        private var pushNotificationApi: PushNotificationApi? = null

        @JvmStatic
        fun bindService(
                context: Context,
                onAidlReceive: (tag: String, bundle: Bundle?) -> Unit = { _, _bundle ->
                    Timber.w("${TIMBER_TAG}receive;reason='onAidlReceive';data='${_bundle.toString().
                    take(TIMBER_MAX_CHAR_LIMIT)}'")
                },
                onAidlError: () -> Unit = {}
        ) {
            pushNotificationApi = PushNotificationApi(onAidlReceive, onAidlError)
            pushNotificationApi?.bindService(context)
        }
    }

}