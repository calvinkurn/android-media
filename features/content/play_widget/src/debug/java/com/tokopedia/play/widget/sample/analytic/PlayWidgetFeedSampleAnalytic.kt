package com.tokopedia.play.widget.sample.analytic

import android.util.Log
import com.tokopedia.play.widget.analytic.list.PlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

/**
 * Created by meyta.taliti on 01/02/22.
 */
class PlayWidgetFeedSampleAnalytic : PlayWidgetInListAnalyticListener {

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        Log.d("FeedPlayWidget","onImpressChannelCard PlayWidgetLargeView ${item.channelId} $channelPositionInList")
    }

    override fun onClickChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        Log.d("FeedPlayWidget","onClickChannelCard PlayWidgetLargeView ${item.channelId} $channelPositionInList")
    }

    override fun onImpressChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        Log.d("FeedPlayWidget","onImpressChannelCard PlayWidgetMediumView ${item.channelId} $channelPositionInList")
    }

    override fun onClickChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        Log.d("FeedPlayWidget","onClickChannelCard PlayWidgetMediumView ${item.channelId} $channelPositionInList")
    }
}
