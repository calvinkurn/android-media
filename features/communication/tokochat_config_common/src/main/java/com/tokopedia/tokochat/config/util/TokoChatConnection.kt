package com.tokopedia.tokochat.config.util

import android.content.Context
import com.gojek.conversations.ConversationsRepository
import com.gojek.courier.AppEvent.AppLogout
import com.gojek.courier.CourierConnection
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokopedia.tokochat.config.di.component.TokoChatConfigComponent
import com.tokopedia.tokochat.config.di.module.TokoChatConfigContextModule
import com.tokopedia.tokochat.config.repository.courier.TokoChatBabbleCourierImpl
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.tokochat.config.di.component.DaggerTokoChatConfigComponent
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

object TokoChatConnection {

    @Volatile
    var tokoChatConfigComponent: TokoChatConfigComponent? = null

    @Volatile
    var courierConnection: CourierConnection? = null

    @Volatile
    private var hasBeenInitialized: Boolean = false

    const val TOKOCHAT_REMOTE_CONFIG = "android_enable_tokochat"

    @JvmStatic
    fun init(context: Context, isFromLoginFlow: Boolean = false) {
        // Initialize AndroidThreeTen for Conversation SDK
        AndroidThreeTen.init(context.applicationContext)

        injectTokoChatConfigComponent(context)

        /**
         * If from login, fetch the AB test first
         * We need it in here because the callback from fetch won't be called in login page
         * Login page will immediately finish the page after login
         */
        if (isFromLoginFlow) {
            fetchABTest(context)
        } else {
            initializeCourierConnection(context)
        }
    }

    private fun initializeCourierConnection(context: Context) {
        // If user does not login or
        // If rollence turned off or seller app or
        // has been initialized, return
        if (!UserSession(context).isLoggedIn || !isTokoChatActive() || hasBeenInitialized) return

        // Initialize Courier Connection
        courierConnection = tokoChatConfigComponent?.getCourierConnection()

        if (courierConnection != null) {
            try {
                // Init courier connection
                if (shouldInitCourier()) {
                    courierConnection?.init(TokoChatBabbleCourierImpl.SOURCE_APP_INIT)
                }

                // Init conversation repository
                tokoChatConfigComponent?.getTokoChatRepository()?.initConversationRepository()
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                logToServer(throwable)
            }
        }

        // Set initialization status to success
        hasBeenInitialized = true
    }

    private fun injectTokoChatConfigComponent(context: Context) {
        if (tokoChatConfigComponent == null) {
            tokoChatConfigComponent = DaggerTokoChatConfigComponent.builder()
                .tokoChatConfigContextModule(TokoChatConfigContextModule(context.applicationContext))
                .build()
        }
    }

    private fun fetchABTest(context: Context) {
        AbTestPlatform(context).fetch(object : RemoteConfig.Listener {
            override fun onComplete(remoteConfig: RemoteConfig?) {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        initializeCourierConnection(context)
                    }
                }
            }

            override fun onError(e: Exception?) {
                // do nothing, tokochat will be inactive without rollence
            }
        })
    }

    @JvmStatic
    fun disconnect() {
        try {
            // If rollence turned off or not initialized, return
            if (!isTokoChatActive() || !hasBeenInitialized) return

            courierConnection?.handleAppEvent(AppLogout)
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    ConversationsRepository.instance?.resetConversationsData()
                    ConversationsRepository.destroy()
                }
            }
            hasBeenInitialized = false
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            logToServer(throwable)
        }
    }

    fun getComponent(context: Context): TokoChatConfigComponent {
        return tokoChatConfigComponent
            ?: DaggerTokoChatConfigComponent.builder()
                .tokoChatConfigContextModule(TokoChatConfigContextModule(context.applicationContext))
                .build().also {
                    tokoChatConfigComponent = it
                }
    }

    fun isTokoChatActive(): Boolean {
        return try {
            tokoChatConfigComponent?.getRemoteConfig()?.getBoolean(
                TOKOCHAT_REMOTE_CONFIG,
                true
            ) == true && !GlobalConfig.isSellerApp()
        } catch (e: Throwable) {
            false
        }
    }

    /**
     * Should init courier here or inside conversation repository
     */
    private fun shouldInitCourier(): Boolean {
        return tokoChatConfigComponent?.getRemoteConfig()?.getBoolean(
            TokoChatBabbleCourierImpl.COURIER_CONVERSATION_INIT
        ) == true
    }

    private fun logToServer(throwable: Throwable) {
        CourierEventLogger.logCourierErrorToServerLogger(throwable)
    }
}
