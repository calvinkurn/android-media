package com.tokopedia.play.widget.analytic.list

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.*
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 02/11/20
 */
class DefaultPlayWidgetInListAnalyticListener(
    private val analytic: PlayWidgetInListAnalyticListener
) : PlayWidgetAnalyticListener {

    private var businessWidgetPosition = 0
    private var verticalWidgetPosition = RecyclerView.NO_POSITION

    override fun onImpressPlayWidget(
        view: PlayWidgetView,
        item: PlayWidgetUiModel,
        widgetPositionInList: Int
    ) {
        verticalWidgetPosition = widgetPositionInList
        businessWidgetPosition = item.config.businessWidgetPosition
        analytic.onImpressPlayWidget(view, item, widgetPositionInList, businessWidgetPosition)
    }

    override fun onClickViewAll(view: PlayWidgetSmallView) {
        analytic.onClickViewAll(view, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onImpressViewAll(view: PlayWidgetMediumView) {
        analytic.onImpressViewAll(view, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickViewAll(view: PlayWidgetMediumView) {
        analytic.onClickViewAll(view, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickChannelCard(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        analytic.onClickChannelCard(
            view,
            item,
            config,
            channelPositionInList,
            verticalWidgetPosition,
            businessWidgetPosition
        )
    }

    override fun onClickBannerCard(view: PlayWidgetSmallView) {
        analytic.onClickBannerCard(view, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onImpressChannelCard(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        analytic.onImpressChannelCard(
            view,
            item,
            config,
            channelPositionInList,
            verticalWidgetPosition,
            businessWidgetPosition
        )
    }

    override fun onImpressOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetBackgroundUiModel, channelPositionInList: Int) {
        analytic.onImpressOverlayCard(view, item, channelPositionInList, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetBackgroundUiModel, channelPositionInList: Int) {
        analytic.onClickOverlayCard(view, item, channelPositionInList, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        analytic.onClickChannelCard(
            view,
            item,
            config,
            channelPositionInList,
            verticalWidgetPosition,
            businessWidgetPosition
        )
    }

    override fun onImpressReminderIcon(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean
    ) {
        analytic.onImpressReminderIcon(view, item, channelPositionInList, isRemindMe, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickToggleReminderChannel(view: PlayWidgetMediumView, item: PlayWidgetChannelUiModel, channelPositionInList: Int, isRemindMe: Boolean) {
        analytic.onClickToggleReminderChannel(view, item, channelPositionInList, isRemindMe, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickMenuActionChannel(view: PlayWidgetMediumView, item: PlayWidgetChannelUiModel, channelPositionInList: Int) {
        analytic.onClickMoreActionChannel(view, item, channelPositionInList, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickDeleteChannel(view: PlayWidgetMediumView, item: PlayWidgetChannelUiModel, channelPositionInList: Int) {
        analytic.onClickDeleteChannel(view, item, channelPositionInList, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onImpressChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        analytic.onImpressChannelCard(
            view,
            item,
            config,
            channelPositionInList,
            verticalWidgetPosition,
            businessWidgetPosition
        )
    }

    override fun onImpressBannerCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBannerUiModel,
        channelPositionInList: Int
    ) {
        analytic.onImpressBannerCard(view, item, channelPositionInList, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickBannerCard(view: PlayWidgetMediumView, item: PlayWidgetBannerUiModel, channelPositionInList: Int) {
        analytic.onClickBannerCard(view, item, channelPositionInList, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onClickBannerCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetBannerUiModel,
        channelPositionInList: Int
    ) {
        analytic.onClickBannerCard(
            view,
            item,
            channelPositionInList,
            verticalWidgetPosition,
            businessWidgetPosition,
        )
    }

    override fun onClickChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        analytic.onClickChannelCard(
            view,
            item,
            config,
            channelPositionInList,
            verticalWidgetPosition,
            businessWidgetPosition,
        )
    }

    override fun onImpressBannerCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetBannerUiModel,
        channelPositionInList: Int
    ) {
        analytic.onImpressBannerCard(
            view,
            item,
            channelPositionInList,
            verticalWidgetPosition,
            businessWidgetPosition
        )
    }

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        analytic.onImpressChannelCard(
            view,
            item,
            config,
            channelPositionInList,
            verticalWidgetPosition,
            businessWidgetPosition,
        )
    }

    override fun onClickToggleReminderChannel(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean
    ) {
        analytic.onClickToggleReminderChannel(
            view,
            item,
            channelPositionInList,
            isRemindMe,
            verticalWidgetPosition,
            businessWidgetPosition,
        )
    }

    override fun onClickChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        analytic.onClickChannelCard(
            view,
            item,
            config,
            channelPositionInList,
            verticalWidgetPosition,
            businessWidgetPosition,
        )
    }

    override fun onClickToggleReminderChannel(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean
    ) {
        analytic.onClickToggleReminderChannel(
            view,
            item,
            channelPositionInList,
            isRemindMe,
            verticalWidgetPosition,
            businessWidgetPosition,
        )
    }

    override fun onImpressChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        analytic.onImpressChannelCard(
            view,
            item,
            config,
            channelPositionInList,
            verticalWidgetPosition,
            businessWidgetPosition,
        )
    }

    override fun onImpressViewAll(view: PlayWidgetSmallView) {
        analytic.onImpressViewAll(view, verticalWidgetPosition, businessWidgetPosition)
    }

    override fun onImpressBannerCard(view: PlayWidgetSmallView) {
        analytic.onImpressBannerCard(view, verticalWidgetPosition, businessWidgetPosition)
    }
}