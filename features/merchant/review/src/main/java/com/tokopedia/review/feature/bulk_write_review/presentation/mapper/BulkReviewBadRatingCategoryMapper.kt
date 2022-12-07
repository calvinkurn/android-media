package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.review.feature.bulk_write_review.domain.model.RequestState
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewGetBadRatingCategoryRequestState
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewBadRatingCategoryUiModel
import javax.inject.Inject

class BulkReviewBadRatingCategoryMapper @Inject constructor() {
    fun map(
        badRatingCategoryRequestState: BulkReviewGetBadRatingCategoryRequestState
    ): List<BulkReviewBadRatingCategoryUiModel> {
        return if (badRatingCategoryRequestState is RequestState.Complete.Success) {
            badRatingCategoryRequestState.result.productrevGetBadRatingCategory.list.map {
                BulkReviewBadRatingCategoryUiModel(
                    id = it.id,
                    text = it.description,
                    selected = false
                )
            }
        } else {
            emptyList()
        }
    }
}
