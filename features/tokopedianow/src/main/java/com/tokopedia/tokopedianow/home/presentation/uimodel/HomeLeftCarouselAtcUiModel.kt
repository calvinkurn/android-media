package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeLeftCarouselAtcUiModel(
    val id : String,
    val name :String,
    val header: TokoNowDynamicHeaderUiModel,
    val productList: List<Visitable<*>> = listOf(),
    val backgroundColorArray: ArrayList<String> = arrayListOf(),
    val campaignId: String = "",
    var imageBanner: String = "",
    var imageBannerAppLink: String = "",
    val realTimeRecom: HomeRealTimeRecomUiModel = HomeRealTimeRecomUiModel()
): HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun hashCode(): Int {
        return id.toIntSafely()
    }

    override fun equals(other: Any?): Boolean {
        return (other as? HomeLeftCarouselAtcUiModel)?.id == id &&
            (other as? HomeLeftCarouselAtcUiModel)?.productList == productList &&
            (other as? HomeLeftCarouselAtcUiModel)?.realTimeRecom == realTimeRecom
    }

    override fun getChangePayload(newModel: HomeLayoutUiModel): Any? {
        val newItem = newModel as HomeLeftCarouselAtcUiModel
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
