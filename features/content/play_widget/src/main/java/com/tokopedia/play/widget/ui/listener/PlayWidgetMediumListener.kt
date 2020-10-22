package com.tokopedia.play.widget.ui.listener

import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel


/**
 * Created by mzennis on 21/10/20.
 */
interface PlayWidgetMediumListener : PlayWidgetListener {

    fun onToggleReminderClicked(
            channel: PlayWidgetMediumChannelUiModel,
            remind: Boolean,
            position: Int
    ) {
    }
}