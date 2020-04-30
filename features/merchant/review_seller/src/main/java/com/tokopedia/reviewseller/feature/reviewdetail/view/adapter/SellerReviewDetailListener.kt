package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import android.view.View
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortItemUiModel
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
    fun onOptionFeedbackClicked(view: View, title: String, optionDetailListItemUnify: ArrayList<ListItemUnify>, isEmptyReply: Boolean)
    fun onImageItemClicked(imageUrls: List<String>, thumbnailsUrl: List<String>, position: Int)
}

interface OverallRatingDetailListener {
    fun onFilterPeriodClicked(view: View, title: String)
}

interface SellerRatingAndTopicListener {
    fun onRatingCheckBoxClicked(ratingAndState: Pair<Int, Boolean>, adapterPosition: Int)
}

interface TopicSortFilterListener {
    fun onTopicClicked(item: SortFilterItemWrapper, adapterPosition: Int)
    fun onSortClicked(itemUiModel: SortItemUiModel, chipType: String, adapterPosition: Int)
}