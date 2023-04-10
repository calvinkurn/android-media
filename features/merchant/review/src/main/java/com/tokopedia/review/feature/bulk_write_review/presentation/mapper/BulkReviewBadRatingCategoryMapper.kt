package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetBadRatingCategoryRequestState
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewBadRatingCategoryUiModel
import javax.inject.Inject

class BulkReviewBadRatingCategoryMapper @Inject constructor() {
    fun map(
        badRatingCategoryRequestState: BulkReviewGetBadRatingCategoryRequestState
    ): List<BulkReviewBadRatingCategoryUiModel> {
        return if (badRatingCategoryRequestState is BulkReviewGetBadRatingCategoryRequestState.Complete.Success) {
            badRatingCategoryRequestState.result.productrevGetBadRatingCategory.list.map {
                BulkReviewBadRatingCategoryUiModel(
                    id = it.id,
                    text = it.description,
                    selected = false,
                    impressHolder = ImpressHolder()
                )
            }
        } else {
            emptyList()
        }
    }
}
