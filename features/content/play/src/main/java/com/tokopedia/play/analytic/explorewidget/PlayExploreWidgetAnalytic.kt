package com.tokopedia.play.analytic.explorewidget

import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.ExploreWidgetType
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelRecommendationConfig
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * @author by astidhiyaa on 03/01/23
 */
interface PlayExploreWidgetAnalytic {
    interface Factory {
        fun create(
            channelInfo: PlayChannelInfoUiModel,
            trackingQueue: TrackingQueue
        ): PlayExploreWidgetAnalytic
    }

    fun impressExploreIcon(widgetInfo: PlayChannelRecommendationConfig, type: ExploreWidgetType)
    fun clickExploreIcon(widgetInfo: PlayChannelRecommendationConfig, type: ExploreWidgetType)
    fun impressExploreTab(
        categoryName: String,
        chips: Map<ChipWidgetUiModel, Int>
    )

    fun clickExploreTab(categoryName: String)
    fun clickContentCard(
        selectedChannel: PlayWidgetChannelUiModel,
        position: Int,
        widgetInfo: PlayChannelRecommendationConfig,
        config: PlayWidgetConfigUiModel,
        type: ExploreWidgetType,
    )

    fun clickCloseExplore()
    fun clickRemind(selectedChannelId: String)
    fun scrollExplore(widgetInfo: PlayChannelRecommendationConfig, type: ExploreWidgetType)
    fun swipeRefresh()
    fun impressToasterGlobalError()
    fun clickRetryToaster()
    fun impressChannelCard(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        widgetInfo: PlayChannelRecommendationConfig,
        position: Int,
        type: ExploreWidgetType
    )
}
