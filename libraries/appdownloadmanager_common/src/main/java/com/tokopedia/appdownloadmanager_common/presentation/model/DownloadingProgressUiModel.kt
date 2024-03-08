package com.tokopedia.appdownloadmanager_common.presentation.model

import androidx.compose.runtime.Stable
import com.tokopedia.kotlin.extensions.view.ZERO

@Stable
data class DownloadingProgressUiModel(
    val currentProgressInPercent: Int = Int.ZERO,
    val currentDownloadedSize: String = "0",
    val totalResourceSize: String = "0"
)
