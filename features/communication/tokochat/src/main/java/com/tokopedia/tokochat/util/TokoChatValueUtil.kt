package com.tokopedia.tokochat.util

import android.os.Build
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

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

    fun shouldShowBubblesAwareness(): Boolean {
        return shouldShowBubblesAwarenessRollence() && shouldShowBubblesAwarenessOS()
    }
    private fun shouldShowBubblesAwarenessOS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }
    private fun shouldShowBubblesAwarenessRollence(): Boolean {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.TOKOCHAT_BUBBLES,
                ""
            ) == RollenceKey.TOKOCHAT_BUBBLES
        } catch (e: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(e)
            false
        }
    }
}
