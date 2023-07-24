package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

data class ShopHomePersoProductComparisonUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = "",
    override val type: String = "",
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
    val recommendationWidget: RecommendationWidget? = null,
    val isError: Boolean = false
) : BaseShopHomeWidgetUiModel() {
    val impressHolder = ImpressHolder()

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
