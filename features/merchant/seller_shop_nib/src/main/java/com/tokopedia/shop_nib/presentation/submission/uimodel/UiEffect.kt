package com.tokopedia.shop_nib.presentation.submission.uimodel

import java.util.*

sealed class UiEffect {
    data class ShowDatePicker(val previouslySelectedDate: Date?): UiEffect()
    object ShowFilePicker : UiEffect()
    object RedirectToSubmissionSuccess : UiEffect()
    data class ShowError(val error : Throwable): UiEffect()
    data class ShowUploadError(val errorMessage: String): UiEffect()
}
