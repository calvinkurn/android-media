package com.tokopedia.review.feature.createreputation.presentation.uimodel

import com.tokopedia.review.feature.createreputation.model.ProductrevGetPostSubmitBottomSheetResponse

sealed class PostSubmitUiState {
    data class ShowThankYouBottomSheet(
        val data: ProductrevGetPostSubmitBottomSheetResponse,
        val hasPendingIncentive: Boolean
    ): PostSubmitUiState()
    data class ShowThankYouToaster(
        val data: ProductrevGetPostSubmitBottomSheetResponse?
    ): PostSubmitUiState()
}
