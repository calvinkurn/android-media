package com.tokopedia.play.widget.analytic.list.jumbo

import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by meyta.taliti on 29/01/22.
 */
interface PlayWidgetInListJumboAnalyticListener {

    fun onClickChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int,
    ) {
    }

    fun onClickToggleReminderChannel(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        verticalWidgetPosition: Int,
    ) {
    }

    fun onImpressChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int,
    ) {
    }
}