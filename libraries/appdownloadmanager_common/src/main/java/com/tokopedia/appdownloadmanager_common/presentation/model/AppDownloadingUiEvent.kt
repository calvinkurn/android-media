package com.tokopedia.appdownloadmanager_common.presentation.model

sealed interface AppDownloadingUiEvent {
    data class OnDownloadSuccess(val fileNamePath: String) : AppDownloadingUiEvent
    data class OnDownloadFailed(val reason: String, val statusColumn: Int) : AppDownloadingUiEvent
    object OnCancelClick : AppDownloadingUiEvent
}
