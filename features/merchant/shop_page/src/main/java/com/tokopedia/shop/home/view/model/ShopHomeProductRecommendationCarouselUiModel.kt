package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

/**
 * Created by Rafli Syam on 16/02/2021
 */
data class ShopHomeProductRecommendationCarouselUiModel(
        override val widgetId: String = "",
        override val layoutOrder: Int = -1,
        override val name: String = "",
        override val type: String = "",
        override val header: BaseShopHomeWidgetUiModel.Header = BaseShopHomeWidgetUiModel.Header(),
        val productList: List<ShopHomeProductUiModel> = listOf()
) : BaseShopHomeWidgetUiModel {

    companion object {
        const val IS_ATC = 1
    }

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}