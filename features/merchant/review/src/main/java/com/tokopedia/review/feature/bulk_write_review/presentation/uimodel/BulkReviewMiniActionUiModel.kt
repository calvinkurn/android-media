package com.tokopedia.review.feature.bulk_write_review.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewMiniActionAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewMiniActionUiState

data class BulkReviewMiniActionUiModel(
    val uiState: BulkReviewMiniActionUiState
) : Visitable<BulkReviewMiniActionAdapterTypeFactory>, BulkReviewVisitable<BulkReviewMiniActionAdapterTypeFactory> {
    override fun type(typeFactory: BulkReviewMiniActionAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: BulkReviewVisitable<BulkReviewMiniActionAdapterTypeFactory>): Boolean {
        return other is BulkReviewMiniActionUiModel && uiState.iconUnifyId == other.uiState.iconUnifyId
    }

    override fun areContentsTheSame(other: BulkReviewVisitable<BulkReviewMiniActionAdapterTypeFactory>): Boolean {
        return this == other
    }

    override fun getChangePayload(other: BulkReviewVisitable<BulkReviewMiniActionAdapterTypeFactory>): Any? {
        return null
    }
}
