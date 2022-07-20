package com.tokopedia.shop.home.view.listener;

import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

interface ShopHomeDisplayWidgetListener {
    fun onDisplayItemImpression(
            displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?,
            displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
            parentPosition: Int,
            adapterPosition: Int
    )

    fun onDisplayItemClicked(
            displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?,
            displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
            parentPosition: Int,
            adapterPosition: Int
    )

    fun loadYouTubeData(videoUrl: String, widgetId: String)

    fun onDisplayWidgetImpression(model: ShopHomeDisplayWidgetUiModel, position: Int)

}
