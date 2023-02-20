package com.tokopedia.review.feature.bulk_write_review.presentation.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewBadRatingCategoryAdapterTypeFactory

data class BulkReviewBadRatingCategoryUiModel(
    val id: String,
    val text: String,
    val selected: Boolean,
    val impressHolder: ImpressHolder
) : BulkReviewVisitable<BulkReviewBadRatingCategoryAdapterTypeFactory> {
    override fun type(typeFactory: BulkReviewBadRatingCategoryAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: BulkReviewVisitable<BulkReviewBadRatingCategoryAdapterTypeFactory>): Boolean {
        return other is BulkReviewBadRatingCategoryUiModel && id == other.id
    }

    override fun getChangePayload(other: BulkReviewVisitable<BulkReviewBadRatingCategoryAdapterTypeFactory>): Any? {
        return null
    }

    override fun areContentsTheSame(other: BulkReviewVisitable<BulkReviewBadRatingCategoryAdapterTypeFactory>): Boolean {
        // just return true since `text` will never change and `selected` can change but it's already
        // reflected because we use onCheckedChangeListener on the checkbox
        return true
    }
}
