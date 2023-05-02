package com.tokopedia.review.feature.bulk_write_review.presentation.uimodel

import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory

data class BulkReviewAnnouncementUiModel(
    val text: String
) : BulkReviewVisitable<BulkReviewAdapterTypeFactory> {
    override fun areItemsTheSame(other: BulkReviewVisitable<BulkReviewAdapterTypeFactory>): Boolean {
        // since there will be only 1 BulkReviewAnnouncementUiModel we can just check whether the new item is
        // BulkReviewAnnouncementUiModel or not
        return other is BulkReviewAnnouncementUiModel
    }

    override fun areContentsTheSame(other: BulkReviewVisitable<BulkReviewAdapterTypeFactory>): Boolean {
        return this == other
    }

    override fun getChangePayload(other: BulkReviewVisitable<BulkReviewAdapterTypeFactory>): Any? {
        // do full rebinding when contents changed
        return null
    }

    override fun type(typeFactory: BulkReviewAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
