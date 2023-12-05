package com.tokopedia.appdownloadmanager_common.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingProgressUiModel
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingState
import com.tokopedia.appdownloadmanager_common.domain.service.DownloadManagerService
import com.tokopedia.kotlin.extensions.view.ONE
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class DownloadManagerViewModel @Inject constructor(
    private val downloadManagerService: DownloadManagerService,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _downloadingState = MutableSharedFlow<DownloadingState>(replay = Int.ONE)
    val downloadingState: SharedFlow<DownloadingState>
        get() = _downloadingState.asSharedFlow().shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(5000),
            replay = Int.ONE
        )

    fun startDownload(apkUrl: String) {
        viewModelScope.launch {

            downloadManagerService.startDownload(
                apkUrl,
                object : DownloadManagerService.DownloadManagerListener {
                    override suspend fun onSuccessDownload(
                        downloadingProgressUiModel: DownloadingProgressUiModel,
                        fileNamePath: String
                    ) {
                        _downloadingState.emit(DownloadingState.DownloadSuccess(
                            downloadingProgressUiModel, fileNamePath
                        ))
                    }

                    override suspend fun onFailedDownload() {
                        _downloadingState.emit(DownloadingState.DownloadFailed)
                    }

                    override suspend fun onDownloading(downloadingProgressUiModel: DownloadingProgressUiModel) {
                        _downloadingState.emit(DownloadingState.Downloading(
                            downloadingProgressUiModel
                        ))
                    }
                })
        }
    }

    fun cancelDownload() {
        downloadManagerService.cancelDownload()
    }
}
