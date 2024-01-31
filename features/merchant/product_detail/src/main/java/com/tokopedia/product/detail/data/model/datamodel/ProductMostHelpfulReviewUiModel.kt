package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.review.ReviewImage
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.detail.view.viewholder.review.ui.ReviewRatingUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

data class ProductMostHelpfulReviewUiModel(
    val type: String = "",
    val name: String = ""
) : DynamicPdpDataModel {

    override fun tabletSectionPosition(): TabletPosition = TabletPosition.BOTTOM

    var review: Review? = null
        private set

    var mediaThumbnails: ReviewMediaThumbnailUiModel? = null
        private set

    var rating: ReviewRatingUiModel = ReviewRatingUiModel()
        private set

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductMostHelpfulReviewUiModel) {
            review == newData.review &&
                mediaThumbnails == newData.mediaThumbnails &&
                rating == newData.rating
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

    fun setData(reviews: List<Review>, reviewImage: ReviewImage, rating: ReviewRatingUiModel) {
        this.review = reviews.firstOrNull()
        this.mediaThumbnails = reviewImage.reviewMediaThumbnails
        this.rating = rating
    }
}
