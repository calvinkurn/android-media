package com.tokopedia.shop.home.view.listener;

import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

interface ShopHomeEndlessProductListener {
    fun onAllProductItemClicked(
            itemPosition: Int,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    )

    fun onAllProductItemImpression(
            itemPosition: Int,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    )

    fun onThreeDotsAllProductClicked(shopHomeProductViewModel: ShopHomeProductViewModel)
}
