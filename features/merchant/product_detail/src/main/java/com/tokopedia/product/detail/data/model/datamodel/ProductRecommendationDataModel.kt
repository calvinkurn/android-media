package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class ProductRecommendationDataModel(
        val type: String = "",
        val name: String = "",
        var recomWidgetData: RecommendationWidget? = null,
        var cardModel: List<ProductCardModel>? = null,
        var position: Int = -1
) : DynamicPDPDataModel {

    val isRecomenDataEmpty: Boolean
        get() = recomWidgetData == null || recomWidgetData?.recommendationItemList?.isEmpty() == true

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name
}