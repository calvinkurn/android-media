package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.CueWidgetCategoryListener
import com.tokopedia.home_component.model.ChannelGrid

/**
 * Created by dhaba
 */
class CueWidgetComponentCallback (val homeCategoryListener: HomeCategoryListener) : CueWidgetCategoryListener {
    override fun onClickCategory(channelGrid: ChannelGrid) {
        homeCategoryListener.onDynamicChannelClicked(channelGrid.applink)
    }
}