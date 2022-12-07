package com.tokopedia.review.feature.bulk_write_review.presentation.uimodel

data class BulkReviewItemMediaUrisUiModel(
    val inboxID: String,
    val uris: List<String>,
    val shouldResetFailedUploadStatus: Boolean
)
