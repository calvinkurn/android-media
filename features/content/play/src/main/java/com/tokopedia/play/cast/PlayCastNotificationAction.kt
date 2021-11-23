package com.tokopedia.play.cast

import android.content.Context
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.media.MediaIntentReceiver
import com.google.android.gms.cast.framework.media.NotificationAction
import com.google.android.gms.cast.framework.media.NotificationActionsProvider
import com.tokopedia.play.R
import java.lang.Exception

class PlayCastNotificationAction(context: Context) : NotificationActionsProvider(context) {
    override fun getNotificationActions(): MutableList<NotificationAction> {
        val actions = mutableListOf<NotificationAction>()
        actions.add(
            NotificationAction.Builder()
                .setAction(MediaIntentReceiver.ACTION_TOGGLE_PLAYBACK)
                .build()
        )
        actions.add(
            NotificationAction.Builder()
                .setAction(MediaIntentReceiver.ACTION_STOP_CASTING)
                .build()
        )
        if(isShow) {
            actions.add(
                NotificationAction.Builder()
                    .setAction(ACTION_OPEN_PLAY)
                    .setIconResId(R.drawable.ic_play_open_page)
                    .setContentDescription(ACTION_OPEN_PLAY)
                    .build()
            )
        }
        return actions
    }

    override fun getCompactViewActionIndices(): IntArray {
        return if(isShow) intArrayOf(0,1,2)
                else intArrayOf(0,1)
    }

    companion object {
        const val ACTION_OPEN_PLAY = "ACTION_OPEN_PLAY"
        private var isShow = false

        fun showRedirectButton(context: Context, isShow: Boolean) {
            try {
                this.isShow = isShow
                CastContext.getSharedInstance(context.applicationContext).mediaNotificationManager.updateNotification()
            }
            catch (e: Exception) {}
        }
    }
}