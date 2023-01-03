package com.tokopedia.play.analytic.explorewidget

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * @author by astidhiyaa on 03/01/23
 */
interface PlayExploreWidgetAnalytic {
    fun impressExploreIcon()
    fun clickExploreIcon()
    fun impressExploreTab(categoryName: String)
    fun clickExploreTab(categoryName: String)
    fun clickContentCard(selectedChannel: PlayWidgetChannelUiModel)
    fun clickCloseExplore()
    fun clickRemind(selectedChannelId: String)
    fun swipeRefresh()
    fun impressToasterGlobalError()
    fun clickRetryToaster()
}
