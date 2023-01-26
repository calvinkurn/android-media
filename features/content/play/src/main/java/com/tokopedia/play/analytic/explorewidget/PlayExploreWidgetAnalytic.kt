package com.tokopedia.play.analytic.explorewidget

import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * @author by astidhiyaa on 03/01/23
 */
interface PlayExploreWidgetAnalytic {
    interface Factory {
        fun create(
            channelInfo: PlayChannelInfoUiModel,
        ): PlayExploreWidgetAnalytic
    }

    fun impressExploreIcon()
    fun clickExploreIcon()
    fun impressExploreTab(
        categoryName: String, chips: Map<ChipWidgetUiModel, Int>
    )

    fun clickExploreTab(categoryName: String)
    fun clickContentCard(
        selectedChannel: PlayWidgetChannelUiModel,
        position: Int,
        categoryName: String,
        isAutoplay: Boolean
    )

    fun clickCloseExplore()
    fun clickRemind(selectedChannelId: String)
    fun scrollExplore()
    fun swipeRefresh()
    fun impressToasterGlobalError()
    fun clickRetryToaster()
}
