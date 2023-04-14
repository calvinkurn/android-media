package com.tokopedia.shop_nib.presentation.submission.uimodel

import java.util.*

data class UiState(
    val isLoading: Boolean = false,
    val selectedFileUri: String = "",
    val selectedFileSizeKb: Long = 0,
    val selectedFileExtension: String = "",
    val selectedDate: Date? = null,
    val fileState: FileState = FileState.NotSelected,
    val isInputValid: Boolean = false,
    val error: Throwable? = null
) {
    sealed class FileState {
        object NotSelected: FileState()
        object InvalidFileExtension: FileState()
        object ExceedMaxFileSize: FileState()
        data class Valid(val fileUri: String, val fileSizeKb: Long): FileState()
    }
}
