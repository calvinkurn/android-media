package com.tokopedia.review.feature.reviewdetail.view.adapter

import android.view.View
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.list.ListItemUnify

/**
 * Created by Yehezkiel on 24/04/20
 */
interface SellerReviewDetailListener {
    fun onChildTopicFilterClicked(item: SortFilterItem, adapterPosition: Int)
    fun onParentTopicFilterClicked()
}

interface ProductFeedbackDetailListener {
    fun onOptionFeedbackClicked(view: View, title: String, data: FeedbackUiModel, optionDetailListItemUnify: ArrayList<ListItemUnify>, isEmptyReply: Boolean)
    fun onImageItemClicked(imageUrls: List<String>, thumbnailsUrl: List<String>, feedbackId: String, position: Int)
    fun onFeedbackMoreReplyClicked(feedbackId: String)
}

interface OverallRatingDetailListener {
    fun onFilterPeriodClicked(view: View, title: String)
    fun shouldShowTickerForRatingDisclaimer(): Boolean
    fun updateSharedPreference()
}

interface SellerRatingAndTopicListener {
    fun onRatingCheckBoxClicked(ratingAndState: Pair<Int, Boolean>, ratingSelected: Int, adapterPosition: Int)
}

interface TopicSortFilterListener {
    interface Topic {
        fun onTopicClicked(chipType: String, adapterPosition: Int)
    }
    interface Sort {
        fun onSortClicked(chipType: String, adapterPosition: Int)
    }
}