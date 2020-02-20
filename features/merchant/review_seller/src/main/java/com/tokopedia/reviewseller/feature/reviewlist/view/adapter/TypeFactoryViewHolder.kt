package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import com.tokopedia.reviewseller.common.ProductReviewModel
import com.tokopedia.reviewseller.common.SummaryReviewModel

/**
 * Created by rizqiaryansa on 2020-02-20.
 */
interface TypeFactoryViewHolder {
    fun type(summaryReviewModel: SummaryReviewModel): Int
    fun type(productReviewModel: ProductReviewModel): Int
}