package com.tokopedia.home.beranda.presentation.view.listener.shorten.item

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.DealsWidgetListener
import com.tokopedia.home_component.visitable.shorten.ItemDealsWidgetUiModel

class DealsTwoSquareWidgetCallback(val listener: HomeCategoryListener) : DealsWidgetListener {

    override fun dealsClicked(data: ItemDealsWidgetUiModel, position: Int) {
        listener.onDynamicChannelClicked(data.appLink)
    }

    override fun dealsImpressed(data: ItemDealsWidgetUiModel, position: Int) {

    }
}
