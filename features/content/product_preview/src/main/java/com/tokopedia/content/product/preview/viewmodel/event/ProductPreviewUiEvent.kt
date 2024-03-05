package com.tokopedia.content.product.preview.viewmodel.event

import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMenuStatus

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed interface ProductPreviewUiEvent {
    data class LoginUiEvent<T>(val data: T) : ProductPreviewUiEvent
    data class ShowSuccessToaster(
        val message: Int? = null,
        val type: Type
    ) : ProductPreviewUiEvent {
        enum class Type(val textRes: Int) {
            ATC(R.string.bottom_atc_success_toaster), Remind(R.string.bottom_wishlist_toaster), Report(
                R.string.review_report_success_toaster
            ),
            Unknown(0);
        }
    }

    data class ShowErrorToaster(
        val message: Throwable,
        val type: Type = Type.Unknown,
        val onClick: () -> Unit
    ) : ProductPreviewUiEvent {
        enum class Type(val textRes: Int) {
            ATC(R.string.bottom_atc_failed_toaster), Report(R.string.review_report_failed_toaster), Unknown(
                0
            );
        }
    }

    data class NavigateUiEvent(
        val appLink: String
    ) : ProductPreviewUiEvent

    data class ShowMenuSheet(
        val status: ReviewMenuStatus
    ) : ProductPreviewUiEvent

    data class LikeUpdate(
        val state: ReviewLikeUiState
    ) : ProductPreviewUiEvent

    data class FailFetchMiniInfo(
        val message: Throwable
    ) : ProductPreviewUiEvent

    object UnknownSourceData : ProductPreviewUiEvent
}
