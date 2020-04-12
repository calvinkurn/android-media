package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import com.tokopedia.reviewseller.feature.reviewlist.view.model.FilterAndSortModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel

/**
 * Created by rizqiaryansa on 2020-02-20.
 */
interface TypeFactoryViewHolder {
    fun type(productRatingOverallModel: ProductRatingOverallModel): Int
    fun type(productReviewUiModel: ProductReviewUiModel): Int
    fun type(filterAndSortModel: FilterAndSortModel): Int
}