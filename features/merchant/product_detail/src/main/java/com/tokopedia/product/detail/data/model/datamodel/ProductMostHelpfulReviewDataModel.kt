package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductMostHelpfulReviewDataModel(
        val type: String = "",
        val name: String = "",
        val dataLayout: List<ComponentData> = listOf(),
        var listOfReviews: List<Review> = listOf()
) : DynamicPDPDataModel {
    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}