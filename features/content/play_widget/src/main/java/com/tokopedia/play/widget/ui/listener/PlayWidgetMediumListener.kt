package com.tokopedia.play.widget.ui.listener


/**
 * Created by mzennis on 21/10/20.
 */
interface PlayWidgetMediumListener {

    fun onChannelClicked(appLink: String) {
    }
    fun onToggleReminderClicked(
            channelId: String,
            remind: Boolean,
            position: Int
    ) {
    }
}