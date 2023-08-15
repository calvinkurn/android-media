package com.tokopedia.mediauploader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.mediauploader.common.data.store.datastore.AnalyticsCacheDataStore
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.data.entity.LogType
import com.tokopedia.mediauploader.data.repository.LogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

interface DebugMediaUploaderHandlerContract {
    val state: StateFlow<DebugMediaLoaderState>
    val config: StateFlow<DebugUploaderParam>

    fun setAction(event: DebugMediaLoaderEvent)
    fun setSourceId(value: String)
    fun shouldCompress(status: Boolean)
    fun waitingTranscode(status: Boolean)
}

class DebugMediaUploaderHandler @Inject constructor(
    @UploaderQualifier private val trackerCacheStore: AnalyticsCacheDataStore,
    private val logRepository: LogRepository,
    private val uploaderUseCase: UploaderUseCase,
) : ViewModel(), DebugMediaUploaderHandlerContract {

    private val _event = MutableSharedFlow<DebugMediaLoaderEvent>(replay = 50)

    private val _state = MutableStateFlow(DebugMediaLoaderState())
    override val state: StateFlow<DebugMediaLoaderState> get() = _state

    override val config = MutableStateFlow(DebugUploaderParam.default())

    private var uploadJob = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    init {
        viewModelScope.launch {
            _event
                .distinctUntilChanged()
                .collect { event ->
                    when (event) {
                        is DebugMediaLoaderEvent.FileChosen -> {
                            _state.value = state.value.copy(
                                filePath = event.filePath,
                                progress = Pair(ProgressType.Upload, 0)
                            ).also {
                                it.reset()
                                it.log(
                                    LogType.FileInfo, logRepository.fileInfo(
                                    event.filePath
                                ))
                            }
                        }
                        is DebugMediaLoaderEvent.Upload -> {
                            onFileUpload(state.value.filePath)
                            onTrackUploaderProgress()
                        }
                        is DebugMediaLoaderEvent.AbortUpload -> {
                            _state.value = state.value.copy(uploading = false)
                            uploadJob.cancel()
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

        viewModelScope.launch(uploadJob) {
            val uploader = uploaderUseCase(param)

            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    uploading = false
                ).also {
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

    private fun onTrackUploaderProgress() {
        uploaderUseCase.trackProgress { i, type ->
            viewModelScope.launch(uploadJob) {
                val key = trackerCacheStore.key(config.value.sourceId, state.value.filePath)
                val cached = trackerCacheStore.getData(key)

                if (cached != null &&
                    cached.compressedVideoPath.isNotEmpty() &&
                    type == ProgressType.Upload &&
                    config.value.shouldCompress &&
                    state.value.isCompressed().not()
                ) {
                    _state.value = state.value.copy(uploading = true)
                        .also {
                            it.log(
                                LogType.CompressInfo, logRepository.compressionInfo(
                                    sourceId = config.value.sourceId,
                                    path = state.value.filePath
                                ))
                        }
                }

            }

            // update loading
            _state.value = state.value.copy().also {
                it.updateProgress(type, i)
            }
        }
    }
}
