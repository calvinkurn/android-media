package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import com.tokopedia.sortfilter.SortFilterItem

/**
 * Created by Yehezkiel on 24/04/20
 */
interface SellerReviewDetailListener {
    fun onChildTopicFilterClicked(item: SortFilterItem)
    fun onParentTopicFilterClicked()
}