package com.tokopedia.review.feature.bulk_write_review.presentation.uimodel

import com.tokopedia.review.feature.createreputation.presentation.viewmodel.MediaUploadResultMap

data class BulkReviewItemMediaUploadResultsUiModel(
    val inboxID: String,
    val mediaUploadResults: MediaUploadResultMap
)
