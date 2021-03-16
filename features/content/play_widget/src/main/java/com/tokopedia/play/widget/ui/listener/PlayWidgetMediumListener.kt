package com.tokopedia.play.widget.ui.listener

import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel


/**
 * Created by mzennis on 21/10/20.
 */
interface PlayWidgetMediumListener : PlayWidgetRouterListener {

    fun onToggleReminderClicked(
            view: PlayWidgetMediumView,
            channelId: String,
            reminderType: PlayWidgetReminderType,
            position: Int
    ) {}

    fun onMenuActionButtonClicked(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            position: Int
    ) {}

    fun onDeleteFailedTranscodingChannel(
            view: PlayWidgetMediumView,
            channelId: String
    ) {}
}