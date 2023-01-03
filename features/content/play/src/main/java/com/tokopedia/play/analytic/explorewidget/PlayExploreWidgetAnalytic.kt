package com.tokopedia.play.analytic.explorewidget

import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * @author by astidhiyaa on 03/01/23
 */
interface PlayExploreWidgetAnalytic {
    interface Factory {
        fun create(
            channelInfo: PlayChannelInfoUiModel,
            trackingQueue: TrackingQueue,
        ): PlayExploreWidgetAnalytic
    }

    fun impressExploreIcon()
    fun clickExploreIcon()
    fun impressExploreTab(categoryName: String)
    fun clickExploreTab(categoryName: String)
    fun clickContentCard(selectedChannel: PlayWidgetChannelUiModel)
    fun clickCloseExplore()
    fun clickRemind(selectedChannelId: String)
    fun scrollExplore()
    fun swipeRefresh()
    fun impressToasterGlobalError()
    fun clickRetryToaster()
}
