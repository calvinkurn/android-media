package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel

data class ProductRecommendationWidgetUiModel(
    private var name: String,
    private var type: String,
    var recommendationWidget: RecommendationWidgetModel = RecommendationWidgetModel()
) : DynamicPdpDataModel {
    override fun type() = type

    override fun type(
        typeFactory: DynamicProductDetailAdapterFactory
    ) = typeFactory.type(this)

    override fun name() = name

    override fun equalsWith(newData: DynamicPdpDataModel) = this == newData

    override fun newInstance() = copy()

    override fun getChangePayload(newData: DynamicPdpDataModel) = null

    override val impressHolder: ImpressHolder = ImpressHolder()
}
