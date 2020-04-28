package com.tokopedia.reviewseller.feature.reviewdetail.view.model

import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory
import com.tokopedia.sortfilter.SortFilterItem

data class TopicUiModel(
        var sortFilterItemList: ArrayList<Pair<SortFilterItem, Boolean>> = arrayListOf(),
        var countFeedback: Int? = -1
) : BaseSellerReviewDetail {
    override fun type(typeFactory: SellerReviewDetailAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}