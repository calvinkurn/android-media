package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeDisplayBannerItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

interface ShopHomeDisplayBannerItemWidgetListener {
    fun onDisplayBannerItemClicked(
        position: Int,
        uiModel: ShopHomeDisplayBannerItemUiModel,
        shopHomeProductViewModel: ShopHomeProductUiModel
    )

    fun onClickTncDisplayBannerItemWidget(uiModel: ShopHomeDisplayBannerItemUiModel)

    fun onClickRemindMe(uiModel: ShopHomeDisplayBannerItemUiModel)

    fun onClickCtaDisplayBannerItemWidget(uiModel: ShopHomeDisplayBannerItemUiModel)

    fun onImpressionDisplayBannerItemWidget(
        position: Int,
        uiModel: ShopHomeDisplayBannerItemUiModel
    )

    fun onTimerFinished(uiModel: ShopHomeDisplayBannerItemUiModel)

}
