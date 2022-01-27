package com.tokopedia.product.detail.view.listener

import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel

class PdpPlayWidgetAnalyticListener(
    private val listener: DynamicProductDetailListener
) : PlayWidgetAnalyticListener {

    var componentTrackDataModel: ComponentTrackDataModel? = null

    override fun onImpressChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        componentTrackDataModel?.let { listener.onImpressChannelCard(it, item) }
    }

    override fun onClickChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        componentTrackDataModel?.let { listener.onClickChannelCard(it, item) }
    }

    override fun onClickBannerCard(view: PlayWidgetSmallView) {
        componentTrackDataModel?.let { listener.onClickBannerCard(it) }
    }

    override fun onClickViewAll(view: PlayWidgetMediumView) {
        componentTrackDataModel?.let { listener.onClickViewAll(it) }

    }

    override fun onClickToggleReminderChannel(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean
    ) {
        componentTrackDataModel?.let { listener.onClickToggleReminderChannel(it, item, isRemindMe) }
    }

}