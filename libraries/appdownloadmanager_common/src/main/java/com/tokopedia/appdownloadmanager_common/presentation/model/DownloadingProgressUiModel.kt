package com.tokopedia.appdownloadmanager_common.presentation.model

import androidx.compose.runtime.Stable

@Stable
data class DownloadingProgressUiModel(
    val currentProgressInPercent: Int,
    val currentDownloadedSize: String,
    val totalResourceSize: String,
    val isFinishedDownloading: Boolean
)
