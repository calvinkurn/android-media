package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class ProductRecommendationDataModel(
        val type: String = "",
        val name: String = "",
        var recomWidgetData: RecommendationWidget? = null,
        var cardModel: List<ProductCardModel>? = null,
        var position: Int = -1
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    val isRecomenDataEmpty: Boolean
        get() = recomWidgetData?.recommendationItemList?.isEmpty() == true

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name
}