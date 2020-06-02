package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel

/**
 * Created by rizqiaryansa on 2020-02-20.
 */
interface TypeFactoryViewHolder {
    fun type(productRatingOverallUiModel: ProductRatingOverallUiModel): Int
    fun type(productReviewUiModel: ProductReviewUiModel): Int
}