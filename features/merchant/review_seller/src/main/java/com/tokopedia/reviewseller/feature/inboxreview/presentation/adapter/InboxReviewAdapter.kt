package com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.reviewseller.common.util.DataEndlessScrollListener
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FilterInboxReviewUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewEmptyUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewErrorUiModel

class InboxReviewAdapter(
        inboxReviewAdapterTypeFactory: InboxReviewAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, InboxReviewAdapterTypeFactory>(inboxReviewAdapterTypeFactory), DataEndlessScrollListener.OnDataEndlessScrollListener {

    private var feedbackInboxListUiModel: MutableList<FeedbackInboxUiModel> = mutableListOf()

    fun setFilterInboxReview(filterInboxReviewList: FilterInboxReviewUiModel) {
        if (visitables.getOrNull(lastIndex) !is FilterInboxReviewUiModel) {
            visitables.add(filterInboxReviewList)
            notifyItemInserted(lastIndex)
        }
    }

    fun setFeedbackListData(feedbackInboxList: List<FeedbackInboxUiModel>) {
        val lastIndex = visitables.size
        feedbackInboxListUiModel.addAll(feedbackInboxList)
        visitables.addAll(feedbackInboxList)
        notifyItemRangeInserted(lastIndex, feedbackInboxList.size)
    }

    fun removeInboxFeedbackError() {
        if (visitables.getOrNull(lastIndex) is InboxReviewErrorUiModel) {
            visitables.removeAt(lastIndex)
        }
    }

    fun addInboxFeedbackError() {
        if (visitables.getOrNull(lastIndex) !is InboxReviewErrorUiModel) {
            visitables.add(InboxReviewErrorUiModel())
            notifyItemInserted(lastIndex)
        }
    }

    fun removeInboxFeedbackEmpty() {
        if (visitables.getOrNull(lastIndex) is InboxReviewEmptyUiModel) {
            visitables.removeAt(lastIndex)
        }
    }

    fun addInboxFeedbackEmpty(isFilter: Boolean) {
        if (isFilter) {
            val feedbackFirstIndex = visitables.count { it is FeedbackInboxUiModel }
            if (visitables.getOrNull(lastIndex) !is InboxReviewEmptyUiModel) {
                visitables.removeAll { it is FeedbackInboxUiModel }
                notifyItemRangeRemoved(visitables.size, feedbackFirstIndex)
                visitables.add(InboxReviewEmptyUiModel(isFilter))
                notifyItemInserted(lastIndex)
            }
        } else {
            if (visitables.getOrNull(lastIndex) !is InboxReviewEmptyUiModel) {
                visitables.add(InboxReviewEmptyUiModel(isFilter))
                notifyItemInserted(lastIndex)
            }
        }
    }

    override val endlessDataSize: Int
        get() = feedbackInboxListUiModel.size

    override fun hideLoading() {
        if (visitables.contains(loadingModel)) {
            val itemPosition = visitables.indexOf(loadingModel)
            visitables.remove(loadingModel)
            if (itemPosition != -1) {
                notifyItemRemoved(itemPosition)
            }
        } else if (visitables.contains(loadingMoreModel)) {
            val itemPosition = visitables.indexOf(loadingMoreModel)
            visitables.remove(loadingMoreModel)
            if (itemPosition != -1) {
                notifyItemRemoved(itemPosition)
            }
        }
    }

}