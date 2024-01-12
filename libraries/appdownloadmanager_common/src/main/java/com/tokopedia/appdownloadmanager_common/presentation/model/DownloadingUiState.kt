package com.tokopedia.appdownloadmanager_common.presentation.model

interface DownloadingUiState {
    object Onboarding : DownloadingUiState

    object Downloading : DownloadingUiState

    object InSufficientSpace: DownloadingUiState
}
