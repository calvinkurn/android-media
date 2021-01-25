package com.tokopedia.review.feature.reviewdetail.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.review.feature.reviewdetail.view.model.*

class SellerReviewDetailAdapter(
        sellerReviewDetailAdapterTypeFactory: SellerReviewDetailAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, SellerReviewDetailAdapterTypeFactory>(sellerReviewDetailAdapterTypeFactory) {

    companion object {
        const val PAYLOAD_RATING_FILTER = 101
        const val PAYLOAD_TOPIC_FILTER = 102
        const val PAYLOAD_TOPIC_FILTER_REVIEW_COUNT = 103
    }

    fun setFeedbackListData(feedbackListUiModel: List<FeedbackUiModel>, reviewCount: Long) {
        val lastIndex = visitables.size
        updateReviewCount(reviewCount)
        visitables.addAll(feedbackListUiModel)
        notifyItemRangeInserted(lastIndex, feedbackListUiModel.size)
    }

    private fun updateReviewCount(reviewCount: Long) {
        val topicIndex = visitables.indexOfFirst { it is TopicUiModel }
        visitables.find { it is TopicUiModel }?.also {
            (it as TopicUiModel).countFeedback = reviewCount
        }
        if (topicIndex != -1) {
            notifyItemChanged(topicIndex, PAYLOAD_TOPIC_FILTER_REVIEW_COUNT)
        }
    }

    fun updateFilterRating(position: Int, updatedState: Boolean, filterRatingInstance: ProductReviewFilterUiModel?) {
        val filterRatingIndex = visitables.indexOf(filterRatingInstance)
        val feedbackFirstIndex = visitables.count { it is FeedbackUiModel }

        if (filterRatingInstance != null && filterRatingIndex != -1) {
            filterRatingInstance.ratingBarList.getOrNull(position)?.ratingIsChecked = updatedState
            notifyItemChanged(filterRatingIndex, PAYLOAD_RATING_FILTER)
            visitables.removeAll { it is FeedbackUiModel }
            notifyItemRangeRemoved(visitables.size, feedbackFirstIndex)
        }
    }

    fun updateTopicFromBottomSheet(topic: List<SortFilterItemWrapper>) {
        if(topic.isNotEmpty()) {
            val topicFirstIndex = visitables.indexOfFirst { it is TopicUiModel }
            val feedbackFirstIndex = visitables.count { it is FeedbackUiModel }

            val topicArrayList = ArrayList<SortFilterItemWrapper>(topic)

            val topicUiModelData = visitables.find { it is TopicUiModel }
            (topicUiModelData as TopicUiModel).sortFilterItemList = topicArrayList

            if (topicFirstIndex != -1) {
                notifyItemChanged(topicFirstIndex)
            }
            visitables.removeAll { it is FeedbackUiModel }

            notifyItemRangeRemoved(visitables.size, feedbackFirstIndex)
        } else {
            val feedbackFirstIndex = visitables.count { it is FeedbackUiModel }

            visitables.removeAll { it is FeedbackUiModel }

            notifyItemRangeRemoved(visitables.size, feedbackFirstIndex)
        }
    }

    fun updateFilterTopic(position: Int, selectedTopic: String, updatedState: Boolean, data: TopicUiModel?) {
        val feedbackFirstIndex = visitables.count { it is FeedbackUiModel }
        if (position != -1) {
            val selectedTopicData = data?.sortFilterItemList?.find {
                it.sortFilterItem?.title == selectedTopic
            }
            selectedTopicData?.isSelected = updatedState
            notifyItemChanged(position, PAYLOAD_TOPIC_FILTER)
            visitables.removeAll { it is FeedbackUiModel }
            notifyItemRangeRemoved(visitables.size, feedbackFirstIndex)
        }
    }

    fun removeReviewNotFound() {
        if (visitables.getOrNull(lastIndex) is ProductFeedbackErrorUiModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    fun addReviewNotFound() {
        if (visitables.getOrNull(lastIndex) !is ProductFeedbackErrorUiModel) {
            visitables.add(ProductFeedbackErrorUiModel())
            notifyItemInserted(lastIndex)
        }
    }
}