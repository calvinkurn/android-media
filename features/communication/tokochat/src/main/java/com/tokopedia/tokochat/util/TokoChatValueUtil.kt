package com.tokopedia.tokochat.util

import android.content.Context
import android.os.Build
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

object TokoChatValueUtil {
    /**
     * Extension values
     */
    const val VOICE_NOTES = "gochat.voicenotes"
    const val PICTURE = "gochat.picture"
    const val STICKER = "gochat.sticker"

    const val IMAGE_EXTENSION = "jpeg"

    /**
     * Error values
     */
    const val CHAT_CLOSED_CODE = "CHAT:SERVICE:CHAT_CLOSED"
    const val CHAT_DOES_NOT_EXIST = "conversation_service:messages_does_not_exists"

    /**
     * Push Notif values
     */
    const val NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY = "nc_template_key"

    /**
     * Censor values
     */
    const val CENSOR_TEXT = "******"

    /**
     * Bubbles
     */
    const val BUBBLES_NOTIF = "bubbles_notif"
    const val BUBBLES_PREF = "tokochat_bubbles_awareness"

    private var remoteConfig: RemoteConfig? = null

    fun shouldShowBubblesAwareness(context: Context?): Boolean {
        return shouldShowBubblesAwarenessRollence(context) && shouldShowBubblesAwarenessOS()
    }
    private fun shouldShowBubblesAwarenessOS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }
    private fun shouldShowBubblesAwarenessRollence(context: Context?): Boolean {
        return try {
            context?.let {
                getRemoteConfig(it).getBoolean(
                    RemoteConfigKey.IS_TOKOCHAT_BUBBLES_ENABLED,
                    true
                )
            }.orTrue()
        } catch (ignore: Throwable) {
            true
        }
    }

    private fun getRemoteConfig(context: Context): RemoteConfig {
        val rc = remoteConfig
        return if (rc == null) {
            val newRc = FirebaseRemoteConfigImpl(context)
            remoteConfig = newRc
            newRc
        } else {
            rc
        }
    }

}
