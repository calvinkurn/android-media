package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import com.tokopedia.reviewseller.feature.reviewlist.view.model.FilterAndSortModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.SummaryReviewModel

/**
 * Created by rizqiaryansa on 2020-02-20.
 */
interface TypeFactoryViewHolder {
    fun type(summaryReviewModel: SummaryReviewModel): Int
    fun type(productReviewModel: ProductReviewModel): Int
    fun type(filterAndSortModel: FilterAndSortModel): Int
}