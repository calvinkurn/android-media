package com.tokopedia.review.feature.bulk_write_review.presentation.uimodel

import com.tokopedia.review.feature.createreputation.presentation.viewmodel.MediaUploadJobMap

data class BulkReviewItemMediaUploadJobsUiModel(
    val inboxID: String,
    val jobs: MediaUploadJobMap
)
