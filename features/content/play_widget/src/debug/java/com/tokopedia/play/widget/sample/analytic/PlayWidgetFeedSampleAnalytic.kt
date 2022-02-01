package com.tokopedia.play.widget.sample.analytic

import android.util.Log
import com.tokopedia.play.widget.analytic.list.PlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by meyta.taliti on 01/02/22.
 */
class PlayWidgetFeedSampleAnalytic : PlayWidgetInListAnalyticListener {

    override fun onImpressPlayWidget(
        view: PlayWidgetView,
        item: PlayWidgetUiModel,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int
    ) {
        Log.d("FeedPlayWidget","onImpressPlayWidget $verticalWidgetPosition")
    }

    override fun onClickChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int,
    ) {
        Log.d("FeedPlayWidget","onClickChannelCard PlayWidgetJumboView ${item.channelId} $channelPositionInList")
    }

    override fun onImpressChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int,
    ) {
        Log.d("FeedPlayWidget","onImpressChannelCard PlayWidgetJumboView ${item.channelId} $channelPositionInList")
    }

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int
    ) {
        Log.d("FeedPlayWidget","onImpressChannelCard PlayWidgetLargeView ${item.channelId} $channelPositionInList")
    }

    override fun onClickChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int
    ) {
        Log.d("FeedPlayWidget","onClickChannelCard PlayWidgetLargeView ${item.channelId} $channelPositionInList")
    }

    override fun onImpressChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int
    ) {
        Log.d("FeedPlayWidget","onImpressChannelCard PlayWidgetMediumView ${item.channelId} $channelPositionInList")
    }

    override fun onClickChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int
    ) {
        Log.d("FeedPlayWidget","onClickChannelCard PlayWidgetMediumView ${item.channelId} $channelPositionInList")
    }
}