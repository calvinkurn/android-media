package com.tokopedia.webview

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Helper to log if page in webview loads too long
 */
class PageLoadLogger(val context:Context) {

    init {
        val remoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
        webviewDelay = remoteConfig.getLong(REMOTE_CONFIG_WEBVIEW_LOG_DELAY, DEFAULT_WEBVIEW_LOG_DELAY)
    }

    companion object {
        private var webviewDelay: Long = 0

        private val REMOTE_CONFIG_WEBVIEW_LOG_DELAY = "android_webview_log_delay"
        private val DEFAULT_WEBVIEW_LOG_DELAY: Long = 10
    }

    private var checkLoadJob: Job? = null

    fun triggerCheckPageLoad(loadedUrl: String?, argUrl: String) {
        checkLoadJob?.cancel()
        if (webviewDelay > 0 && context is LifecycleOwner) {
            checkLoadJob =
                context.lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        delay(webviewDelay * 1000)
                        context.logUrlTooLongToOpen(
                            argUrl,
                            loadedUrl, webviewDelay
                        )
                    } catch (ignored: Throwable) {
                    }
                }
        }
    }

    fun stop() {
        checkLoadJob?.cancel()
        checkLoadJob = null
    }

}