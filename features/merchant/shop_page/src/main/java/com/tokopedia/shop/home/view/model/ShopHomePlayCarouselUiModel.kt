package com.tokopedia.shop.home.view.model

import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

data class ShopHomePlayCarouselUiModel(
        override val widgetId: String = "",
        override val layoutOrder: Int = -1,
        override val name: String = "",
        override val type: String = "",
        override val header: BaseShopHomeWidgetUiModel.Header = BaseShopHomeWidgetUiModel.Header(),
        val playBannerCarouselDataModel: PlayBannerCarouselDataModel = PlayBannerCarouselDataModel()
) : BaseShopHomeWidgetUiModel {

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}