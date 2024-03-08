package com.tokopedia.appdownloadmanager_common.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.appdownloadmanager_common.domain.service.DownloadManagerService
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingProgressUiModel
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingState
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DownloadManagerViewModel @Inject constructor(
    private val downloadManagerService: DownloadManagerService
) : ViewModel() {

    private val _downloadingState = MutableStateFlow<DownloadingState>(
        DownloadingState.Downloading()
    )
    val downloadingState: StateFlow<DownloadingState>
        get() = _downloadingState.asStateFlow()

    private val _downloadingUiState = MutableStateFlow<DownloadingUiState>(
        DownloadingUiState.Onboarding
    )

    val downloadingUiState: StateFlow<DownloadingUiState>
        get() = _downloadingUiState

    fun updateDownloadingState() {
        viewModelScope.launch {
            _downloadingUiState.emit(DownloadingUiState.Downloading)
        }
    }

    fun updateInsufficientSpaceState() {
        viewModelScope.launch {
            _downloadingUiState.emit(DownloadingUiState.InSufficientSpace)
        }
    }

    fun startDownload(apkUrl: String) {
        downloadManagerService.startDownload(
            apkUrl,
            object : DownloadManagerService.DownloadManagerListener {

                override suspend fun onFailedDownload(reason: String, statusColumn: Int) {
                    _downloadingState.emit(DownloadingState.DownloadFailed(reason, statusColumn))
                    FirebaseCrashlytics.getInstance().recordException(RuntimeException("status: $statusColumn | reason: $reason | apkUrl: $apkUrl"))
                }

                override suspend fun onDownloading(downloadingProgressUiModel: DownloadingProgressUiModel) {
                    _downloadingState.emit(
                        DownloadingState.Downloading(
                            downloadingProgressUiModel
                        )
                    )
                }

                override suspend fun onSuccessDownload(
                    downloadingProgressUiModel: DownloadingProgressUiModel,
                    fileNamePath: String
                ) {
                    _downloadingState.emit(
                        DownloadingState.DownloadSuccess(
                            downloadingProgressUiModel,
                            fileNamePath
                        )
                    )
                }
            }
        )
    }

    fun cancelDownload() {
        downloadManagerService.cancelDownload()
    }
}
