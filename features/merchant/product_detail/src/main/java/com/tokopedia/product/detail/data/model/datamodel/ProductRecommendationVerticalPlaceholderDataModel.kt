package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class ProductRecommendationVerticalPlaceholderDataModel(
    private val type: String = "",
    private val name: String = "",
    override val impressHolder: ImpressHolder = ImpressHolder(),
    var recomWidgetData: RecommendationWidget? = null
) : DynamicPdpDataModel {

    val recommendationVerticalDataModels = mutableListOf<ProductRecommendationVerticalDataModel>()

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is ProductRecommendationVerticalPlaceholderDataModel &&
                this.recomWidgetData?.title == newData.recomWidgetData?.title
    }

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    fun updateItemList(items: List<RecommendationItem>) {
        val dataModels = items.map { item ->
            ProductRecommendationVerticalDataModel(
                name = item.productId.toString(),
                recommendationItem = item
            )
        }
        recommendationVerticalDataModels.addAll(dataModels)
    }
}