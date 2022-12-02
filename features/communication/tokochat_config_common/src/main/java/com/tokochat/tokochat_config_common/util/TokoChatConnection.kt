package com.tokochat.tokochat_config_common.util

import android.content.Context
import com.gojek.conversations.ConversationsRepository
import com.gojek.courier.AppEvent.AppLogout
import com.gojek.courier.CourierConnection
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokochat.tokochat_config_common.di.component.DaggerTokoChatConfigComponent
import com.tokochat.tokochat_config_common.di.component.TokoChatConfigComponent
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigContextModule
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

object TokoChatConnection {

    var tokoChatConfigComponent: TokoChatConfigComponent? = null
    var courierConnection: CourierConnection? = null

    @Volatile
    private var initializationStatus: Boolean = false

    fun init(context: Context) {
        // Initialize AndroidThreeTen for Conversation SDK
        AndroidThreeTen.init(context.applicationContext)

        injectTokoChatConfigComponent(context)

        // If user does not login, return
        if (!UserSession(context).isLoggedIn) return

        // If rollence turned off, return
        if (!isTokoChatActive()) return

        // Initialize Courier Connection
        courierConnection = tokoChatConfigComponent?.getCourierConnection()

        if (courierConnection != null) {
            tokoChatConfigComponent?.getTokoChatRepository()?.initConversationRepository()
        }

        // Set initialization status to success
        initializationStatus = true
    }

    private fun injectTokoChatConfigComponent(context: Context) {
        if (tokoChatConfigComponent == null) {
            tokoChatConfigComponent = DaggerTokoChatConfigComponent.builder()
                .tokoChatConfigContextModule(TokoChatConfigContextModule(context.applicationContext))
                .build()
        }
    }

    fun disconnect() {
        try {
            // If rollence turned off, return
            if (!isTokoChatActive() || !initializationStatus) return

            courierConnection?.handleAppEvent(AppLogout)
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    ConversationsRepository.instance?.resetConversationsData()
                    ConversationsRepository.destroy()
                }
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun isTokoChatActive(): Boolean {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.KEY_ROLLENCE_TOKOCHAT,
                ""
            ) == RollenceKey.KEY_ROLLENCE_TOKOCHAT && !GlobalConfig.isSellerApp()
        } catch (e: Throwable) {
            false
        }
    }

    fun hasBeenInitialized(): Boolean {
        return initializationStatus
    }
}
