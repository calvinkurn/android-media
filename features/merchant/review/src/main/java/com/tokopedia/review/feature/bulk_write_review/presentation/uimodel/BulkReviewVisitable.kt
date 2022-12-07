package com.tokopedia.review.feature.bulk_write_review.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory

interface BulkReviewVisitable<T : BaseAdapterTypeFactory> : Visitable<T> {
    fun areItemsTheSame(other: BulkReviewVisitable<T>): Boolean
    fun areContentsTheSame(other: BulkReviewVisitable<T>): Boolean
    fun getChangePayload(other: BulkReviewVisitable<T>): Any?
}
