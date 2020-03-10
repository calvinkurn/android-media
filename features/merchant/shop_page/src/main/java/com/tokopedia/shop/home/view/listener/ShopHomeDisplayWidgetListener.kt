package com.tokopedia.shop.home.view.listener;

import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

interface ShopHomeDisplayWidgetListener {
    fun onItemImpression(
            displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?,
            displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
            parentPosition: Int,
            adapterPosition: Int
    )

    fun onItemClicked(
            displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?,
            displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
            parentPosition: Int,
            adapterPosition: Int
    )}
