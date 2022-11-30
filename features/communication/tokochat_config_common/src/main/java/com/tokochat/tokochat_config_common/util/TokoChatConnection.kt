package com.tokochat.tokochat_config_common.util

import android.content.Context
import com.gojek.conversations.ConversationsRepository
import com.gojek.courier.AppEvent.AppLogout
import com.gojek.courier.CourierConnection
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokochat.tokochat_config_common.di.component.DaggerTokoChatConfigComponent
import com.tokochat.tokochat_config_common.di.component.TokoChatConfigComponent
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigContextModule
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object TokoChatConnection {

    var tokoChatConfigComponent: TokoChatConfigComponent? = null
    var courierConnection: CourierConnection? = null

    fun init(context: Context): Boolean {
        if (!UserSession(context).isLoggedIn) return false

        // If rollence turned off, return false
        if (!isTokoChatActive()) return false

        // Initialize AndroidThreeTen for Conversation SDK
        AndroidThreeTen.init(context.applicationContext)

        tokoChatConfigComponent = DaggerTokoChatConfigComponent.builder()
            .tokoChatConfigContextModule(TokoChatConfigContextModule(context.applicationContext))
            .build()

        // Initialize Courier Connection
        courierConnection = tokoChatConfigComponent?.getCourierConnection()

        if (courierConnection != null) {
            tokoChatConfigComponent?.getTokoChatRepository()?.initConversationRepository()
        }
        return true
    }

    fun disconnect() {
        try {
            // If rollence turned off, return
            if (!isTokoChatActive()) return

            courierConnection?.handleAppEvent(AppLogout)
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    ConversationsRepository.instance?.resetConversationsData()
                    ConversationsRepository.destroy()
                }
            }
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }

    private fun isTokoChatActive(): Boolean {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.KEY_ROLLENCE_TOKOCHAT,
                ""
            ) == RollenceKey.KEY_ROLLENCE_TOKOCHAT
        } catch (e: Throwable) {
            false
        }
    }
}
