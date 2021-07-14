package com.tokopedia.play.cast

import android.content.Context
import com.google.android.gms.cast.framework.media.MediaIntentReceiver
import com.google.android.gms.cast.framework.media.NotificationAction
import com.google.android.gms.cast.framework.media.NotificationActionsProvider
import com.tokopedia.play.R

class PlayCastNotificationAction(context: Context) : NotificationActionsProvider(context) {
    override fun getNotificationActions(): MutableList<NotificationAction> {
        val actions = arrayListOf<NotificationAction>()
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
        actions.add(
            NotificationAction.Builder()
                .setAction("ACTION_OPEN_PLAY")
                .setIconResId(R.drawable.ic_play_arrow_up)
                .setContentDescription("ACTION_OPEN_PLAY")
                .build()
        )
        return actions
    }

    override fun getCompactViewActionIndices(): IntArray {
        return intArrayOf(0,1,2)
    }
}