package com.tokopedia.shop.home.view.listener

import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel

interface ShopHomePlayWidgetListener {
    fun onPlayWidgetImpression(model: CarouselPlayWidgetUiModel, position: Int)

    fun onPlayWidgetCtaClicked(model: PlayWidgetUiModel)
}
