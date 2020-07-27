package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductMostHelpfulReviewDataModel(
        val type: String = "",
        val name: String = "",
        var listOfReviews: List<Review> = listOf(),
        var imageReviews: List<ImageReviewItem>? = null,
        var ratingScore:Float = 0F,
        var totalRating:Int = 0
) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}