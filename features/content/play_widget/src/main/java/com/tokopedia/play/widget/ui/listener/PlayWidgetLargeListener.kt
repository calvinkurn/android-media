package com.tokopedia.play.widget.ui.listener

import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType

/**
 * @author by astidhiyaa on 11/01/22
 */
interface PlayWidgetLargeListener : PlayWidgetRouterListener {

    fun onToggleReminderClicked(
        view: PlayWidgetLargeView,
        channelId: String,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {}

    fun onMenuActionButtonClicked(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        position: Int
    ) {}

    fun onDeleteFailedTranscodingChannel(
        view: PlayWidgetLargeView,
        channelId: String
    ) {}
}
