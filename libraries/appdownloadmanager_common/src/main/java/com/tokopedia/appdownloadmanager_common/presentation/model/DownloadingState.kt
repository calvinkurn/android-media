package com.tokopedia.appdownloadmanager_common.presentation.model

sealed interface DownloadingState {

    data class DownloadSuccess(
        val downloadingProgressUiModel: DownloadingProgressUiModel,
        val fileName: String
    ): DownloadingState
    data class Downloading(
        val downloadingProgressUiModel: DownloadingProgressUiModel,
    ) : DownloadingState

    object DownloadFailed : DownloadingState
}
