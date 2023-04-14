package com.tokopedia.shop_nib.presentation.submission.uimodel

import java.util.*

sealed class UiEvent {
    object TapSelectFile : UiEvent()
    data class ConfirmFile(val fileUri: String, val fileExtension: String, val fileSizeKb: Long) : UiEvent()
    object UnselectFile : UiEvent()
    object TapChangeDate : UiEvent()
    data class ConfirmDate(val newDate: Date) : UiEvent()
    object SubmitFile : UiEvent()
}
