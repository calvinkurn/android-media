package com.tokopedia.play.widget.ui.listener

import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType

/**
 * @author by astidhiyaa on 12/01/22
 */
interface PlayWidgetJumboListener : PlayWidgetRouterListener {

    fun onToggleReminderClicked(
        view: PlayWidgetJumboView,
        channelId: String,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {
    }
}