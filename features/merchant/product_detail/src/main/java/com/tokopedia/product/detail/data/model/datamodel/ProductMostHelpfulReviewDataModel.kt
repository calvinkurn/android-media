package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.product.detail.common.data.model.product.Rating
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductMostHelpfulReviewDataModel(
        val type: String = "",
        val name: String = "",
        var listOfReviews: List<Review> = listOf(),
        var imageReviews: List<ImageReviewItem>? = null,
        var rating: Rating? = null
) : DynamicPDPDataModel {
    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}