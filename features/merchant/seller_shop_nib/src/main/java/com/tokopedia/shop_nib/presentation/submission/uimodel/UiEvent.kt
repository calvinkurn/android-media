package com.tokopedia.shop_nib.presentation.submission.uimodel

import java.util.*

sealed class UiEvent {
    object RecordImpression : UiEvent()

    object TapSelectFile : UiEvent()
    data class ConfirmFile(val fileUri: String, val fileExtension: String, val fileSizeBytes: Long) : UiEvent()
    object UnselectFile : UiEvent()
    object TapChangeDate : UiEvent()
    object DatePickerDismissed : UiEvent()
    data class ConfirmDate(val newDate: Date) : UiEvent()
    object SubmitFile : UiEvent()
}
