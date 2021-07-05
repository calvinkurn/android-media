package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.CategoryNavigationTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.CategoryNavigationListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by Lukas on 23/11/20.
 */
class CategoryNavigationCallback (
        val context: Context?,
        val homeCategoryListener: HomeCategoryListener
): CategoryNavigationListener{
    override fun onCategoryNavigationImpress(channelModel: ChannelModel, grid: ChannelGrid, position: Int) {
        homeCategoryListener.getTrackingQueueObj()?.let { CategoryNavigationTracking.sendCategoryNavigationImpress(it, channelModel, grid, homeCategoryListener.userId, position) }
    }

    override fun onCategoryNavigationClick(channelModel: ChannelModel, grid: ChannelGrid, position: Int) {
        CategoryNavigationTracking.sendCategoryNavigationClick(channelModel, grid, homeCategoryListener.userId, position)
        RouteManager.route(context, grid.applink)
    }
}