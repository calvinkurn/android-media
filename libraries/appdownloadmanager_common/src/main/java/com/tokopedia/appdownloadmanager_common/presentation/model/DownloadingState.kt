package com.tokopedia.appdownloadmanager_common.presentation.model

sealed interface DownloadingState {

    data class Downloading(
        val downloadingProgressUiModel: DownloadingProgressUiModel = DownloadingProgressUiModel()
    ) : DownloadingState

    data class DownloadSuccess(
        val downloadingProgressUiModel: DownloadingProgressUiModel = DownloadingProgressUiModel(),
        val fileNamePath: String = ""
    ) : DownloadingState

    data class DownloadFailed(val reason: String, val statusColumn: Int) : DownloadingState
}
