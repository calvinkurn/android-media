package com.tokopedia.review.feature.inboxreview.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.review.feature.inboxreview.presentation.model.InboxReviewEmptyUiModel
import com.tokopedia.review.feature.inboxreview.presentation.model.InboxReviewErrorUiModel

class InboxReviewAdapter(
        inboxReviewAdapterTypeFactory: InboxReviewAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, InboxReviewAdapterTypeFactory>(inboxReviewAdapterTypeFactory){

    fun setFeedbackListData(feedbackInboxList: List<FeedbackInboxUiModel>) {
        val lastIndex = visitables.size
        visitables.addAll(feedbackInboxList)
        notifyItemRangeInserted(lastIndex, feedbackInboxList.size)
    }

    fun addInboxFeedbackError() {
        if (visitables.getOrNull(lastIndex) !is InboxReviewErrorUiModel) {
            visitables.add(InboxReviewErrorUiModel())
            notifyItemInserted(lastIndex)
        }
    }

    fun addInboxFeedbackEmpty(isFilter: Boolean) {
        if (visitables.getOrNull(lastIndex) !is InboxReviewEmptyUiModel) {
            visitables.add(InboxReviewEmptyUiModel(isFilter))
            notifyItemInserted(lastIndex)
        }
    }

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