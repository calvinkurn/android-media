package com.tokopedia.review.feature.bulk_write_review.presentation.uimodel

import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory

object BulkReviewAnnouncementUiModel : BulkReviewVisitable<BulkReviewAdapterTypeFactory> {
    override fun areItemsTheSame(other: BulkReviewVisitable<BulkReviewAdapterTypeFactory>): Boolean {
        return other is BulkReviewAnnouncementUiModel
    }

    override fun areContentsTheSame(other: BulkReviewVisitable<BulkReviewAdapterTypeFactory>): Boolean {
        return true
    }

    override fun getChangePayload(other: BulkReviewVisitable<BulkReviewAdapterTypeFactory>): Any? {
        return null
    }

    override fun type(typeFactory: BulkReviewAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
