package com.tokochat.tokochat_config_common.util

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import com.gojek.courier.CourierConnection
import com.tokochat.tokochat_config_common.di.component.DaggerTokoChatConfigComponent
import com.tokochat.tokochat_config_common.di.component.TokoChatConfigComponent
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigContextModule

object TokoChatConnection {

    var tokoChatConfigComponent: TokoChatConfigComponent? = null
    var courierConnection: CourierConnection? = null

    fun init(context: Context) {
        tokoChatConfigComponent = DaggerTokoChatConfigComponent.builder()
            .tokoChatConfigContextModule(TokoChatConfigContextModule(context.applicationContext))
            .build()

        // Initialize Courier Connection
        val courierComponent =
            tokoChatConfigComponent?.getCourierClientProvider()?.initializeCourierComponent()
        if (courierComponent != null) {
            courierConnection = courierComponent.courierConnection()
        }

        if (courierConnection != null) {
            // Attach observer lifecycle
            ProcessLifecycleOwner.get().lifecycle.addObserver(TokoChatProcessLifecycleObserver())
        }
    }
}
