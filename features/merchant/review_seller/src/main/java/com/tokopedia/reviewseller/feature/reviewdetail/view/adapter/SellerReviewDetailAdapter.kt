package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.reviewseller.common.util.DataEndlessScrollListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.*

class SellerReviewDetailAdapter(
        sellerReviewDetailAdapterTypeFactory: SellerReviewDetailAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, SellerReviewDetailAdapterTypeFactory>(sellerReviewDetailAdapterTypeFactory), DataEndlessScrollListener.OnDataEndlessScrollListener {

    companion object {
        const val PAYLOAD_RATING_FILTER = 101
    }

    private var productReviewDetailFeedback: MutableList<FeedbackUiModel> = mutableListOf()

    fun setOverallRatingDetailData(overallRatingDetailUiModel: OverallRatingDetailUiModel) {
        val lastIndex = visitables.size
        visitables.add(overallRatingDetailUiModel)
        notifyItemInserted(lastIndex)
    }

    fun setRatingBarDetailData(ratingBarUiModel: List<RatingBarUiModel>) {
        val lastIndex = visitables.size
//        visitables.addAll(ratingBarUiModel)
        notifyItemRangeInserted(lastIndex, ratingBarUiModel.size)
    }

    fun setTopicDetailData(topicUiModel: TopicUiModel) {
        val lastIndex = visitables.size
        visitables.add(topicUiModel)
        notifyItemInserted(lastIndex)
    }

    fun setFeedbackListData(feedbackListUiModel: List<FeedbackUiModel>) {
        val lastIndex = visitables.size
        productReviewDetailFeedback.addAll(feedbackListUiModel)
        visitables.addAll(feedbackListUiModel)
        notifyItemRangeInserted(lastIndex, feedbackListUiModel.size)
    }

    fun updateFilterRating(position: Int, updatedState: Boolean, filterRatingInstance: ProductReviewFilterUiModel?) {
        val filterRatingIndex = visitables.indexOf(filterRatingInstance)
        if (filterRatingInstance != null && filterRatingIndex != -1) {
            filterRatingInstance.ratingBarList.getOrNull(position)?.ratingIsChecked = updatedState
            notifyItemChanged(filterRatingIndex, PAYLOAD_RATING_FILTER)
        }
    }

    override val endlessDataSize: Int
        get() = productReviewDetailFeedback.size

    override fun hideLoading() {
        if (visitables.contains(loadingModel)) {
            val itemPosition = visitables.indexOf(loadingModel)
            visitables.remove(loadingModel)
            notifyItemRemoved(itemPosition)
        } else if (visitables.contains(loadingMoreModel)) {
            val itemPosition = visitables.indexOf(loadingMoreModel)
            visitables.remove(loadingMoreModel)
            notifyItemRemoved(itemPosition)
        }
    }

}