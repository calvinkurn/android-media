package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.OverallRatingDetailViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.ProductFeedbackDetailViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.RatingAndTopicDetailViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.TopicViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.OverallRatingDetailUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.TopicUiModel

class SellerReviewDetailAdapterTypeFactory(
        private val overallRatingDetailListener: OverallRatingDetailViewHolder.OverallRatingDetailListener
): BaseAdapterTypeFactory(), TypeFactoryDetailViewHolder {

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

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            ProductFeedbackDetailViewHolder.LAYOUT -> ProductFeedbackDetailViewHolder(parent)
            OverallRatingDetailViewHolder.LAYOUT -> OverallRatingDetailViewHolder(parent, overallRatingDetailListener)
            RatingAndTopicDetailViewHolder.LAYOUT -> RatingAndTopicDetailViewHolder(parent)
            TopicViewHolder.LAYOUT -> TopicViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}