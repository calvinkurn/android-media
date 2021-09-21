package com.tokopedia.review.feature.createreputation.presentation.uimodel

sealed class CreateReviewDialogType {
    object CreateReviewSendRatingOnlyDialog: CreateReviewDialogType()
    object CreateReviewUnsavedDialog: CreateReviewDialogType()
}