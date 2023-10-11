package com.tokopedia.play.widget.analytic.list

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.*
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselView
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 02/11/20
 */
class DefaultPlayWidgetInListAnalyticListener(
    private val analytic: PlayWidgetInListAnalyticListener
) : PlayWidgetAnalyticListener {

    private var verticalWidgetPosition = RecyclerView.NO_POSITION

    override fun onImpressPlayWidget(
        view: PlayWidgetView,
        item: PlayWidgetUiModel,
        widgetPositionInList: Int
    ) {
        verticalWidgetPosition = widgetPositionInList
        analytic.onImpressPlayWidget(view, item, widgetPositionInList, item.config.businessWidgetPosition)
    }

    override fun onClickViewAll(view: PlayWidgetSmallView) {
        analytic.onClickViewAll(view, verticalWidgetPosition)
    }

    override fun onImpressViewAll(view: PlayWidgetMediumView) {
        analytic.onImpressViewAll(view, verticalWidgetPosition)
    }

    override fun onClickViewAll(view: PlayWidgetMediumView) {
        analytic.onClickViewAll(view, verticalWidgetPosition)
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
        )
    }

    override fun onClickBannerCard(view: PlayWidgetSmallView) {
        analytic.onClickBannerCard(view, verticalWidgetPosition)
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
        )
    }

    override fun onImpressOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetBackgroundUiModel, channelPositionInList: Int) {
        analytic.onImpressOverlayCard(view, item, channelPositionInList, verticalWidgetPosition)
    }

    override fun onClickOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetBackgroundUiModel, channelPositionInList: Int) {
        analytic.onClickOverlayCard(view, item, channelPositionInList, verticalWidgetPosition)
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
        )
    }

    override fun onImpressReminderIcon(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isReminded: Boolean
    ) {
        analytic.onImpressReminderIcon(view, item, channelPositionInList, isReminded, verticalWidgetPosition)
    }

    override fun onClickToggleReminderChannel(view: PlayWidgetMediumView, item: PlayWidgetChannelUiModel, channelPositionInList: Int, isRemindMe: Boolean) {
        analytic.onClickToggleReminderChannel(view, item, channelPositionInList, isRemindMe, verticalWidgetPosition)
    }

    override fun onClickMenuActionChannel(view: PlayWidgetMediumView, item: PlayWidgetChannelUiModel, channelPositionInList: Int) {
        analytic.onClickMoreActionChannel(view, item, channelPositionInList, verticalWidgetPosition)
    }

    override fun onClickMenuActionChannel(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int
    ) {
        analytic.onClickMoreActionChannel(view, item, channelPositionInList, verticalWidgetPosition)
    }

    override fun onClickDeleteChannel(view: PlayWidgetMediumView, item: PlayWidgetChannelUiModel, channelPositionInList: Int) {
        analytic.onClickDeleteChannel(view, item, channelPositionInList, verticalWidgetPosition)
    }

    override fun onClickDeleteChannel(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int
    ) {
        analytic.onClickDeleteChannel(view, item, channelPositionInList, verticalWidgetPosition)
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
        )
    }

    override fun onImpressBannerCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBannerUiModel,
        channelPositionInList: Int
    ) {
        analytic.onImpressBannerCard(view, item, channelPositionInList, verticalWidgetPosition)
    }

    override fun onClickBannerCard(view: PlayWidgetMediumView, item: PlayWidgetBannerUiModel, channelPositionInList: Int) {
        analytic.onClickBannerCard(view, item, channelPositionInList, verticalWidgetPosition)
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
        )
    }

    override fun onImpressViewAll(view: PlayWidgetSmallView) {
        analytic.onImpressViewAll(view, verticalWidgetPosition)
    }

    override fun onImpressBannerCard(view: PlayWidgetSmallView) {
        analytic.onImpressBannerCard(view, verticalWidgetPosition)
    }

    /**
     * Region Carousel
     */
    override fun onImpressChannelCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int
    ) {
        analytic.onImpressChannelCard(view, item, config, channelPositionInList, verticalWidgetPosition)
    }

    override fun onClickChannelCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int
    ) {
        analytic.onClickChannelCard(view, item, config, channelPositionInList, verticalWidgetPosition)
    }

    override fun onClickPartnerName(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int
    ) {
        analytic.onClickPartnerName(view, item, config, channelPositionInList, verticalWidgetPosition)
    }

    override fun onClickToggleMuteButton(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int
    ) {
        analytic.onClickToggleMuteButton(view, item, config, channelPositionInList, verticalWidgetPosition)
    }

    override fun onClickToggleReminderChannel(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean
    ) {
        analytic.onClickToggleReminderChannel(view, item, config, channelPositionInList, isRemindMe, verticalWidgetPosition)
    }

    override fun onImpressProductCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        product: PlayWidgetProduct,
        productPosition: Int,
        channelPositionInList: Int,
    ) {
        analytic.onImpressProductCard(view, item, config, product, productPosition, channelPositionInList, verticalWidgetPosition)
    }

    override fun onClickProductCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        product: PlayWidgetProduct,
        productPosition: Int,
        channelPositionInList: Int,
    ) {
        analytic.onClickProductCard(view, item, config, product, productPosition, channelPositionInList, verticalWidgetPosition)
    }
}
