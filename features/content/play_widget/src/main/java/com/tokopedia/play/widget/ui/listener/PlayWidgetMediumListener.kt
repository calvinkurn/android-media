package com.tokopedia.play.widget.ui.listener

import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetActionReminder
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType


/**
 * Created by mzennis on 21/10/20.
 */
interface PlayWidgetMediumListener : PlayWidgetRouterListener {

    fun onToggleReminderClicked(
            view: PlayWidgetMediumView,
            channelId: String,
            actionReminder: PlayWidgetActionReminder,
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