package com.tokopedia.review.feature.reviewdetail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.*
import com.tokopedia.review.feature.reviewdetail.view.model.*

class SellerReviewDetailAdapterTypeFactory(private val listener: SellerReviewDetailListener,
                                           private val overallRatingDetailListener: OverallRatingDetailListener,
                                           private val productFeedbackDetailListener: ProductFeedbackDetailListener,
                                           private var ratingAndTopicsDetailListener: SellerRatingAndTopicListener) : BaseAdapterTypeFactory(), TypeFactoryDetailViewHolder {

    override fun type(feedbackUiModel: FeedbackUiModel): Int {
        return ProductFeedbackDetailViewHolder.LAYOUT
    }

    override fun type(overallRatingDetailUiModel: OverallRatingDetailUiModel): Int {
        return OverallRatingDetailViewHolder.LAYOUT
    }

    override fun type(topicUiModel: TopicUiModel): Int {
        return TopicViewHolder.LAYOUT
    }

    override fun type(filterUiModel: ProductReviewFilterUiModel): Int {
        return ProductRatingFilterViewHolder.LAYOUT
    }

    override fun type(feedbackErrorUiModel: ProductFeedbackErrorUiModel): Int {
        return FeedbackErrorViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ShimmerReviewDetailViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            ProductFeedbackDetailViewHolder.LAYOUT -> ProductFeedbackDetailViewHolder(parent, productFeedbackDetailListener)
            OverallRatingDetailViewHolder.LAYOUT -> OverallRatingDetailViewHolder(parent, overallRatingDetailListener)
            ProductRatingFilterViewHolder.LAYOUT -> ProductRatingFilterViewHolder(parent,ratingAndTopicsDetailListener)
            TopicViewHolder.LAYOUT -> TopicViewHolder(parent, listener)
            FeedbackErrorViewHolder.LAYOUT -> FeedbackErrorViewHolder(parent)
            ShimmerReviewDetailViewHolder.LAYOUT -> ShimmerReviewDetailViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}