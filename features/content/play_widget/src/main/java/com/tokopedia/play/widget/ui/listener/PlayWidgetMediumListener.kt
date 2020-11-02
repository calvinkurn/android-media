package com.tokopedia.play.widget.ui.listener

import com.tokopedia.play.widget.ui.PlayWidgetMediumView


/**
 * Created by mzennis on 21/10/20.
 */
interface PlayWidgetMediumListener : PlayWidgetRouterListener {

    fun onToggleReminderClicked(
            view: PlayWidgetMediumView,
            channelId: String,
            remind: Boolean,
            position: Int
    ) {}
}