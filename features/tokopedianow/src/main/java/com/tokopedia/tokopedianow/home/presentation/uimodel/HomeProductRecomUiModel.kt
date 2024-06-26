package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeProductRecomUiModel(
    val id: String,
    val title: String,
    val productList: List<ProductCardCompactCarouselItemUiModel>,
    val seeMoreModel: ProductCardCompactCarouselSeeMoreUiModel? = null,
    val headerModel: TokoNowDynamicHeaderUiModel? = null,
    val realTimeRecom: HomeRealTimeRecomUiModel = HomeRealTimeRecomUiModel()
) : HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getChangePayload(newModel: HomeLayoutUiModel): Any? {
        val newItem = newModel as HomeProductRecomUiModel
        val oldProductList = productList
        val newProductList = newItem.productList
        val oldRtrWidget = realTimeRecom
        val newRtrWidget = newItem.realTimeRecom

        return when {
            oldProductList != newProductList ||
            oldRtrWidget != newRtrWidget -> true
            else -> null
        }
    }
}
