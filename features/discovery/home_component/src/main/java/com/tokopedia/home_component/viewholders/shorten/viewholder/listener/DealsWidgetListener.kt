package com.tokopedia.home_component.viewholders.shorten.viewholder.listener

import com.tokopedia.home_component.visitable.shorten.DealsWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemDealsWidgetUiModel

interface DealsWidgetListener {
    // container
    // fun dealsContainerImpressed(data: DealsWidgetUiModel)

    // items
    fun dealsImpressed(data: ItemDealsWidgetUiModel, position: Int)
    fun dealsClicked(data: ItemDealsWidgetUiModel, position: Int)
}
