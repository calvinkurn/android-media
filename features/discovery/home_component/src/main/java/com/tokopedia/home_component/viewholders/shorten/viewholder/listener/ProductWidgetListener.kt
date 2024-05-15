package com.tokopedia.home_component.viewholders.shorten.viewholder.listener

import com.tokopedia.home_component.visitable.shorten.ItemProductWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ProductWidgetUiModel

interface ProductWidgetListener {

    fun productChannelHeaderClicked(data: ProductWidgetUiModel)
    fun retryWidget()

    fun productImpressed(data: ProductWidgetUiModel, position: Int)
    fun itemProductClicked(data: ItemProductWidgetUiModel, position: Int)
}
