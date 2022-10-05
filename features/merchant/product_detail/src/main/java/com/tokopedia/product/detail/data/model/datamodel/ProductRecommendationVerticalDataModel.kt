package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class ProductRecommendationVerticalDataModel(
    private val type: String = String.EMPTY,
    private val name: String = String.EMPTY,
    override val impressHolder: ImpressHolder = ImpressHolder(),
    var recommendationItem: RecommendationItem? = null,
    var position: Int = -1
) : DynamicPdpDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is ProductRecommendationVerticalDataModel &&
            this.recommendationItem?.productId == newData.recommendationItem?.productId
    }

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
}
