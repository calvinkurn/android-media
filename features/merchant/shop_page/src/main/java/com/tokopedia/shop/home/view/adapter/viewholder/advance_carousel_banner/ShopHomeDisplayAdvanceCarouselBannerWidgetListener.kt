package com.tokopedia.shop.home.view.adapter.viewholder.advance_carousel_banner

import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

interface ShopHomeDisplayAdvanceCarouselBannerWidgetListener {

    fun onClickAdvanceCarouselBannerItem(
        uiModel: ShopHomeDisplayWidgetUiModel,
        bannerItemUiModel: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
        position: Int
    )

    fun onImpressionAdvanceCarouselBannerItem(
        uiModel: ShopHomeDisplayWidgetUiModel,
        bannerItemUiModel: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
        position: Int
    )

}
