package com.tokopedia.play.widget.analytic.list

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.*

/**
 * Created by jegul on 02/11/20
 */
class DefaultPlayWidgetInListAnalyticListener (
        private val analytic: PlayWidgetInListAnalyticListener
) : PlayWidgetAnalyticListener {

    private var businessWidgetPosition = 0
    private var verticalWidgetPosition = RecyclerView.NO_POSITION

    override fun onImpressPlayWidget(view: PlayWidgetView, item: PlayWidgetUiModel, widgetPositionInList: Int) {
        verticalWidgetPosition = widgetPositionInList
        if (item is PlayWidgetConfigProvider) businessWidgetPosition = item.config.businessWidgetPosition
        analytic.onImpressPlayWidget(view, item, widgetPositionInList, businessWidgetPosition)
    }

    override fun onClickViewAll(view: PlayWidgetSmallView) {
        analytic.onClickViewAll(view, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickViewAll(view: PlayWidgetMediumView) {
        analytic.onClickViewAll(view, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickChannelCard(view: PlayWidgetSmallView, item: PlayWidgetSmallChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) {
        analytic.onClickChannelCard(view, item, channelPositionInList, isAutoPlay, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickBannerCard(view: PlayWidgetSmallView) {
        analytic.onClickBannerCard(view, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onImpressChannelCard(view: PlayWidgetSmallView, item: PlayWidgetSmallChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) {
        analytic.onImpressChannelCard(view, item, channelPositionInList, isAutoPlay, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onImpressOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetMediumOverlayUiModel, channelPositionInList: Int) {
        analytic.onImpressOverlayCard(view, item, channelPositionInList, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetMediumOverlayUiModel, channelPositionInList: Int) {
        analytic.onClickOverlayCard(view, item, channelPositionInList, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickChannelCard(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) {
        analytic.onClickChannelCard(view, item, channelPositionInList, isAutoPlay, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickToggleReminderChannel(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isRemindMe: Boolean) {
        analytic.onClickToggleReminderChannel(view, item, channelPositionInList, isRemindMe, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickMenuActionChannel(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int) {
        analytic.onClickMoreActionChannel(view, item, channelPositionInList, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickDeleteChannel(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int) {
        analytic.onClickDeleteChannel(view, item, channelPositionInList, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onImpressChannelCard(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) {
        analytic.onImpressChannelCard(view, item, channelPositionInList, isAutoPlay, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickBannerCard(view: PlayWidgetMediumView, item: PlayWidgetMediumBannerUiModel, channelPositionInList: Int) {
        analytic.onClickBannerCard(view, item, channelPositionInList, verticalWidgetPosition, businessWidgetPosition)
    }
}