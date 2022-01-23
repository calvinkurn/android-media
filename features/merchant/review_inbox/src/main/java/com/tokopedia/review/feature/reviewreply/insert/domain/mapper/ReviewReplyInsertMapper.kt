package com.tokopedia.review.feature.reviewreply.insert.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.review.feature.reviewreply.insert.domain.model.ReviewReplyInsertResponse
import com.tokopedia.review.feature.reviewreply.insert.presentation.model.ReviewReplyInsertUiModel

object ReviewReplyInsertMapper {
    fun mapToInsertReplyUiModel(
        productrevInsertSellerResponse: ReviewReplyInsertResponse.ProductrevInsertSellerResponse
    ): ReviewReplyInsertUiModel {
        return ReviewReplyInsertUiModel(productrevInsertSellerResponse.success.orFalse())
    }
}