package com.tokochat.tokochat_config_common.util

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import com.gojek.conversations.ConversationsRepository
import com.gojek.courier.AppEvent.AppLogout
import com.gojek.courier.CourierConnection
import com.tokochat.tokochat_config_common.di.component.DaggerTokoChatConfigComponent
import com.tokochat.tokochat_config_common.di.component.TokoChatConfigComponent
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigContextModule

class TokoChatConnection {

    var tokoChatConfigComponent: TokoChatConfigComponent? = null
    var courierConnection: CourierConnection? = null

    fun init(context: Context) {
        tokoChatConfigComponent = DaggerTokoChatConfigComponent.builder()
            .tokoChatConfigContextModule(TokoChatConfigContextModule(context.applicationContext))
            .build()
        tokoChatConfigComponent?.inject(this)

        // Initialize Courier Connection
        courierConnection = tokoChatConfigComponent?.getCourierConnection()

        if (courierConnection != null) {
            // Attach observer lifecycle
            ProcessLifecycleOwner.get().lifecycle.addObserver(TokoChatProcessLifecycleObserver())
            tokoChatConfigComponent?.getTokoChatRepository()?.initConversationRepository()
        }
    }

    fun disconnect() {
        try {
            courierConnection?.handleAppEvent(AppLogout)
            Thread {
                ConversationsRepository.instance?.resetConversationsData()
                ConversationsRepository.destroy()
            }.start()
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }
}
