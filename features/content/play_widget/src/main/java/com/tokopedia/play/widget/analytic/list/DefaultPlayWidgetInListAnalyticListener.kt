package com.tokopedia.play.widget.analytic.list

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumOverlayUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallChannelUiModel

/**
 * Created by jegul on 02/11/20
 */
class DefaultPlayWidgetInListAnalyticListener (
        private val analytic: PlayWidgetInListAnalyticListener
) : PlayWidgetAnalyticListener {

    private var widgetPosition = RecyclerView.NO_POSITION

    override fun onImpressPlayWidget(view: PlayWidgetView, widgetPositionInList: Int) {
        widgetPosition = widgetPositionInList
        analytic.onImpressPlayWidget(view, widgetPositionInList)
    }

    override fun onClickViewAll(view: PlayWidgetSmallView) = withWidgetPosition {
        analytic.onClickViewAll(view, it)
    }

    override fun onClickViewAll(view: PlayWidgetMediumView) = withWidgetPosition {
        analytic.onClickViewAll(view, it)
    }

    override fun onClickChannelCard(view: PlayWidgetSmallView, item: PlayWidgetSmallChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) = withWidgetPosition {
        analytic.onClickChannelCard(view, item, channelPositionInList, isAutoPlay, it)
    }

    override fun onClickBannerCard(view: PlayWidgetSmallView) = withWidgetPosition {
        analytic.onClickBannerCard(view, it)
    }

    override fun onImpressChannelCard(view: PlayWidgetSmallView, item: PlayWidgetSmallChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) = withWidgetPosition {
        analytic.onImpressChannelCard(view, item, channelPositionInList, isAutoPlay, it)
    }

    override fun onImpressOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetMediumOverlayUiModel, channelPositionInList: Int) = withWidgetPosition {
        analytic.onImpressOverlayCard(view, item, channelPositionInList, it)
    }

    override fun onClickOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetMediumOverlayUiModel, channelPositionInList: Int) = withWidgetPosition {
        analytic.onClickOverlayCard(view, item, channelPositionInList, it)
    }

    override fun onClickChannelCard(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) = withWidgetPosition {
        analytic.onClickChannelCard(view, item, channelPositionInList, isAutoPlay, it)
    }

    override fun onClickToggleReminderChannel(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isRemindMe: Boolean) = withWidgetPosition {
        analytic.onClickToggleReminderChannel(view, item, channelPositionInList, isRemindMe, it)
    }

    override fun onClickMenuActionChannel(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int) = withWidgetPosition {
        analytic.onClickMoreActionChannel(view, item, channelPositionInList, it)
    }

    override fun onClickDeleteChannel(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int) = withWidgetPosition {
        analytic.onClickDeleteChannel(view, item, channelPositionInList, it)
    }

    override fun onImpressChannelCard(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) = withWidgetPosition {
        analytic.onImpressChannelCard(view, item, channelPositionInList, isAutoPlay, it)
    }

    override fun onClickBannerCard(view: PlayWidgetMediumView, item: PlayWidgetMediumBannerUiModel, channelPositionInList: Int) = withWidgetPosition {
        analytic.onClickBannerCard(view, item, channelPositionInList, it)
    }

    private fun withWidgetPosition(onTrack: (Int) -> Unit) {
        if (widgetPosition == RecyclerView.NO_POSITION) return
        onTrack(widgetPosition)
    }
}