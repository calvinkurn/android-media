package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.*
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.OverallRatingDetailUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.TopicUiModel

class SellerReviewDetailAdapterTypeFactory(private val listener: SellerReviewDetailListener,
                                           private val overallRatingDetailListener: OverallRatingDetailViewHolder.OverallRatingDetailListener,
                                           private val productFeedbackDetailListener: ProductFeedbackDetailViewHolder.ProductFeedbackDetailListener): BaseAdapterTypeFactory(), TypeFactoryDetailViewHolder {

    override fun type(feedbackUiModel: FeedbackUiModel): Int {
        return ProductFeedbackDetailViewHolder.LAYOUT
    }

    override fun type(overallRatingDetailUiModel: OverallRatingDetailUiModel): Int {
        return OverallRatingDetailViewHolder.LAYOUT
    }

    override fun type(ratingBarUiModel: RatingBarUiModel): Int {
        return RatingAndTopicDetailViewHolder.LAYOUT
    }

    override fun type(topicUiModel: TopicUiModel): Int {
        return TopicViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return ShimmerReviewDetailViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            ProductFeedbackDetailViewHolder.LAYOUT -> ProductFeedbackDetailViewHolder(parent, productFeedbackDetailListener)
            OverallRatingDetailViewHolder.LAYOUT -> OverallRatingDetailViewHolder(parent, overallRatingDetailListener)
            RatingAndTopicDetailViewHolder.LAYOUT -> RatingAndTopicDetailViewHolder(parent)
            ShimmerReviewDetailViewHolder.LAYOUT -> ShimmerReviewDetailViewHolder(parent)
            TopicViewHolder.LAYOUT -> TopicViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}