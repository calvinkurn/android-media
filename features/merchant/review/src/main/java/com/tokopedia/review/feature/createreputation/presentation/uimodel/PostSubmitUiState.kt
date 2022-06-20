package com.tokopedia.review.feature.createreputation.presentation.uimodel

import com.tokopedia.review.feature.createreputation.model.ProductrevGetPostSubmitBottomSheetResponse
import java.io.Serializable

sealed class PostSubmitUiState : Serializable {
    object Hidden : PostSubmitUiState()
    data class ShowThankYouBottomSheet(
        val data: ProductrevGetPostSubmitBottomSheetResponse,
        val hasPendingIncentive: Boolean
    ) : PostSubmitUiState()
    data class ShowThankYouToaster(
        val data: ProductrevGetPostSubmitBottomSheetResponse?
    ) : PostSubmitUiState()
}
