package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.home.analytics.v2.CategoryWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.CategoryWidgetV2Listener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import java.util.HashMap

class CategoryWidgetV2Callback(
    val context: Context?,
    val homeCategoryListener: HomeCategoryListener
): CategoryWidgetV2Listener {

    override fun onClickCategoryWidget(channelModel: ChannelModel, grid: ChannelGrid, position: Int) {
        homeCategoryListener.sendEETracking(
            CategoryWidgetTracking.getCategoryWidgetBannerClick(
                homeCategoryListener.userId,
                (position+1).toString(),
                grid,
                channelModel
            ) as HashMap<String, Any>
        )
        homeCategoryListener.onSectionItemClicked(grid.applink)
    }

    override fun onImpressCategoryWidget(channelModel: ChannelModel) {
        homeCategoryListener.putEEToIris(
            CategoryWidgetTracking.getCategoryWidgetBannerImpression(
                channelModel.channelGrids,
                homeCategoryListener.userId,
                true,
                channelModel
            ) as HashMap<String, Any>
        )
    }

    override fun onSeeAllCategoryWidget(channelModel: ChannelModel) {
        CategoryWidgetTracking.sendCategoryWidgetSeeAllClick(channelModel, homeCategoryListener.userId)
        homeCategoryListener.onDynamicChannelClicked(channelModel.channelHeader.applink)
    }
}