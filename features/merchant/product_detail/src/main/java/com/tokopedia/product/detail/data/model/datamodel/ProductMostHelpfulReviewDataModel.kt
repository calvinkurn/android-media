package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

data class ProductMostHelpfulReviewDataModel(
        val type: String = "",
        val name: String = "",
        var listOfReviews: List<Review>? = null,
        var mediaThumbnails: ReviewMediaThumbnailUiModel? = null,
        var detailedMediaResult: ProductrevGetReviewMedia? = null,
        var ratingScore: Float = 0F,
        var totalRating: Int = 0,
        var formattedRating: String = "",
        var totalRatingCount: String = "",
        var totalReviewCount: String = ""
) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductMostHelpfulReviewDataModel) {
            listOfReviews?.size == newData.listOfReviews?.size &&
            mediaThumbnails == newData.mediaThumbnails &&
            detailedMediaResult == newData.detailedMediaResult
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return null
    }
}