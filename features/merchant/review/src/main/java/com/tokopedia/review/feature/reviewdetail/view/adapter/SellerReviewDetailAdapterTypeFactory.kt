package com.tokopedia.review.feature.reviewdetail.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.FeedbackErrorViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.OverallRatingDetailViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.ProductFeedbackDetailViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.ProductRatingFilterViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.ShimmerReviewDetailViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.TopicViewHolder
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.OverallRatingDetailUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.ProductFeedbackErrorUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.ProductReviewFilterUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.TopicUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory

class SellerReviewDetailAdapterTypeFactory(
    private val listener: SellerReviewDetailListener,
    private val overallRatingDetailListener: OverallRatingDetailListener,
    private val productFeedbackDetailListener: ProductFeedbackDetailListener,
    private var ratingAndTopicsDetailListener: SellerRatingAndTopicListener,
    private val reviewMediaThumbnailListener: ReviewMediaThumbnailTypeFactory.Listener,
    private val reviewMediaThumbnailRecycledViewPool: RecyclerView.RecycledViewPool
) : BaseAdapterTypeFactory(), TypeFactoryDetailViewHolder {

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
        return when (type) {
            ProductFeedbackDetailViewHolder.LAYOUT -> ProductFeedbackDetailViewHolder(
                reviewMediaThumbnailRecycledViewPool,
                reviewMediaThumbnailListener,
                parent,
                productFeedbackDetailListener
            )
            OverallRatingDetailViewHolder.LAYOUT -> OverallRatingDetailViewHolder(
                parent,
                overallRatingDetailListener
            )
            ProductRatingFilterViewHolder.LAYOUT -> ProductRatingFilterViewHolder(
                parent,
                ratingAndTopicsDetailListener
            )
            TopicViewHolder.LAYOUT -> TopicViewHolder(parent, listener)
            FeedbackErrorViewHolder.LAYOUT -> FeedbackErrorViewHolder(parent)
            ShimmerReviewDetailViewHolder.LAYOUT -> ShimmerReviewDetailViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}