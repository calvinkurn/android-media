package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

interface ShopHomeDisplayBannerTimerWidgetListener {
    fun onDisplayBannerTimerClicked(
        position: Int,
        uiModel: ShopWidgetDisplayBannerTimerUiModel,
        shopHomeProductViewModel: ShopHomeProductUiModel
    )

    fun onClickTncDisplayBannerTimerWidget(uiModel: ShopWidgetDisplayBannerTimerUiModel)

    fun onClickRemindMe(uiModel: ShopWidgetDisplayBannerTimerUiModel)

    fun onClickCtaDisplayBannerTimerWidget(uiModel: ShopWidgetDisplayBannerTimerUiModel)

    fun onImpressionDisplayBannerTimerWidget(
        position: Int,
        uiModel: ShopWidgetDisplayBannerTimerUiModel
    )

    fun onTimerFinished(uiModel: ShopWidgetDisplayBannerTimerUiModel)

}
