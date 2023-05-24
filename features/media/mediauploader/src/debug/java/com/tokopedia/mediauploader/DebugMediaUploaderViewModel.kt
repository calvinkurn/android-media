package com.tokopedia.mediauploader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.data.entity.LogType
import com.tokopedia.mediauploader.data.repository.LogRepository
import com.tokopedia.mediauploader.tracker.TrackerCacheDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

interface DebugMediaUploaderViewModelContract {
    val state: StateFlow<DebugMediaLoaderState>

    fun setAction(event: DebugMediaLoaderEvent)
    fun setSourceId(value: String)
    fun shouldCompress(status: Boolean)
    fun waitingTranscode(status: Boolean)
}

class DebugMediaUploaderViewModel @Inject constructor(
    private val trackerCacheStore: TrackerCacheDataStore,
    private val uploaderUseCase: UploaderUseCase,
    private val logRepository: LogRepository
) : ViewModel(), DebugMediaUploaderViewModelContract {

    private val _event = MutableSharedFlow<DebugMediaLoaderEvent>(replay = 50)

    private val _state = MutableStateFlow(DebugMediaLoaderState())
    override val state: StateFlow<DebugMediaLoaderState> get() = _state

    private val config = MutableStateFlow(DebugUploaderParam.default())

    private var isCompressionLogExist = false

    init {
        viewModelScope.launch {
            _event
                .distinctUntilChanged()
                .collect { event ->
                    when (event) {
                        is DebugMediaLoaderEvent.FileChosen -> {
                            _state.value = state.value.copy(
                                filePath = event.filePath
                            ).also {
                                it.clear()
                                it.log(
                                    LogType.FileInfo, logRepository.fileInfo(
                                    event.filePath
                                ))
                            }
                        }
                        is DebugMediaLoaderEvent.Upload -> {
                            onFileUpload(state.value.filePath)
                        }
                    }
                }
        }
    }

    override fun waitingTranscode(status: Boolean) {
        config.value = config.value.copy(
            withTranscode = status
        )
    }

    override fun setSourceId(value: String) {
        config.value = config.value.copy(
            sourceId = value
        )
    }

    override fun shouldCompress(status: Boolean) {
        config.value = config.value.copy(
            shouldCompress = status
        )
    }

    override fun setAction(event: DebugMediaLoaderEvent) {
        _event.tryEmit(event)
    }

    private fun onFileUpload(path: String) {
        val param = uploaderUseCase.createParams(
            shouldCompress = config.value.shouldCompress,
            withTranscode = config.value.withTranscode,
            sourceId = config.value.sourceId,
            filePath = File(path),
            isSecure = false
        )

        uploaderUseCase.trackProgress { i, type ->
            viewModelScope.launch {
                val key = trackerCacheStore.key(config.value.sourceId, state.value.filePath)
                val cached = trackerCacheStore.getData(key)

                if (cached != null &&
                    cached.compressedVideoPath.isNotEmpty() &&
                    type == ProgressType.Upload &&
                    config.value.shouldCompress &&
                    isCompressionLogExist.not()
                ) {
                    isCompressionLogExist = true

                    _state.value = state.value.copy()
                        .also {

                            it.log(
                                LogType.CompressInfo, logRepository.compressionInfo(
                                sourceId = config.value.sourceId,
                                path = state.value.filePath
                            ))
                        }
                }

                _state.value = state.value.copy().also {
                    it.updateProgress(type, i)
                }
            }

        }

        viewModelScope.launch {
            val uploader = uploaderUseCase(param)

            withContext(Dispatchers.Main) {
                _state.value = state.value.copy().also {
                    it.log(
                        LogType.UploadResult,
                        logRepository.uploadResult(
                            uploader,
                            config.value.sourceId,
                            state.value.filePath
                        )
                    )
                }
            }
        }
    }

    private val _uploading = MutableLiveData<UploadState>()
    val uploading: LiveData<UploadState> get() = _uploading

    fun setUploadingStatus(uploading: UploadState) {
        _uploading.value = uploading
    }

    companion object {
        sealed class UploadState {
            object Idle : UploadState()
            data class Uploading(
                val withWorker: Boolean
            ) : UploadState()

            object Stopped : UploadState()
            object Aborted : UploadState()
            data class Finished(
                val result: UploadResult
            ) : UploadState()
        }
    }
}
