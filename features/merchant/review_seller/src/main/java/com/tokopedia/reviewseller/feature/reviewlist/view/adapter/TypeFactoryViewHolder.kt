package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import com.tokopedia.reviewseller.feature.reviewlist.view.model.FilterAndSortModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.SearchRatingProductUiModel

/**
 * Created by rizqiaryansa on 2020-02-20.
 */
interface TypeFactoryViewHolder {
    fun type(productRatingOverallUiModel: ProductRatingOverallUiModel): Int
    fun type(productReviewUiModel: ProductReviewUiModel): Int
    fun type(filterAndSortModel: FilterAndSortModel): Int
    fun type(searchRatingProductUiModel: SearchRatingProductUiModel): Int
}