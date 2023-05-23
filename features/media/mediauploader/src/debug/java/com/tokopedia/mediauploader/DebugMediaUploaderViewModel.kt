package com.tokopedia.mediauploader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.VideoMetaDataExtractor
import com.tokopedia.mediauploader.tracker.TrackerCacheDataStore
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface DebugMediaUploaderViewModelContract {
    val state: StateFlow<DebugMediaLoaderState>

    fun setAction(event: DebugMediaLoaderEvent)
    fun setSourceId(value: String)
    fun shouldCompress(status: Boolean)
    fun waitingTranscode(status: Boolean)
}

class DebugMediaUploaderViewModel @Inject constructor(
    private val videoMetaDataExtractor: VideoMetaDataExtractor,
    private val compressionCacheManager: TrackerCacheDataStore,
    private val uploaderUseCase: UploaderUseCase
) : ViewModel(), DebugMediaUploaderViewModelContract {

    private val _state = MutableStateFlow(DebugMediaLoaderState())

    override val state: StateFlow<DebugMediaLoaderState>
        get() = _state

    private val _event = MutableSharedFlow<DebugMediaLoaderEvent>(replay = 50)

    private val sourceId = MutableStateFlow("exwbZW")
    private val shouldCompress = MutableStateFlow(true)
    private val waitingTranscode = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            _event
                .distinctUntilChanged()
                .collect { event ->
                    when (event) {
                        is DebugMediaLoaderEvent.FileChosen -> {
                            val filePaths = event.filePaths

                            _state.value = state.value.copy(filePaths = filePaths).also {
                                it.clear()
                                it.addLog(LogType.FileInfo, fileInfo(filePaths.first()))
                            }
                        }

                        is DebugMediaLoaderEvent.Upload -> {
                            val path = state.value.filePaths.first()
                            onFileUpload(path)
                        }
                    }
                }
        }
    }

    override fun waitingTranscode(status: Boolean) {
        waitingTranscode.value = status
    }

    override fun setSourceId(value: String) {
        sourceId.value = value
    }

    override fun shouldCompress(status: Boolean) {
        shouldCompress.value = status
    }

    override fun setAction(event: DebugMediaLoaderEvent) {
        _event.tryEmit(event)
    }

    private fun onFileUpload(path: String) {
        val param = uploaderUseCase.createParams(
            shouldCompress = shouldCompress.value,
            withTranscode = waitingTranscode.value,
            sourceId = sourceId.value,
            filePath = File(path),
            isSecure = false
        )

        uploaderUseCase.trackProgress { i, type ->
            _state.value = state.value.copy().also {
                if (
                    type == ProgressType.Upload &&
                    param.getBoolean("should_compress", false) &&
                    it.logs.firstOrNull { it.first == LogType.CompressInfo } == null
                ) {
                    viewModelScope.launch {
                        it.addLog(LogType.CompressInfo, buildString {
                            val key = compressionCacheManager.key(sourceId.value, path)
                            val cache = compressionCacheManager.getData(key)

                            val compressionTime =
                                cache?.endCompressedTime.orZero() - cache?.startCompressedTime.orZero()

                            append("\n")
                            append("${cache?.compressedVideoPath}")
                            append("\n")
                            append("bitrate: ${cache?.compressedVideoMetadata?.bitrate} bps")
                            append("\n")
                            append("resolution: ${cache?.compressedVideoMetadata?.width} x ${cache?.compressedVideoMetadata?.height} px")
                            append("\n")
                            append(
                                "compression time: ${
                                    TimeUnit.MILLISECONDS.toSeconds(
                                        compressionTime
                                    )
                                } sec."
                            )
                            append("\n")
                            append(
                                "size: ${
                                    try {
                                        File(cache?.compressedVideoPath.toString()).length()
                                            .formattedToMB()
                                    } catch (ignored: Throwable) {
                                        "0"
                                    }
                                } MB"
                            )
                        })
                    }
                }

                it.updateProgress(type, i)
            }
        }

        viewModelScope.launch {
            val uploader = uploaderUseCase(param)

            withContext(Dispatchers.Main) {
                when (uploader) {
                    is UploadResult.Success -> {
                        _state.value = state.value.copy().also {
                            val result = buildString {
                                if (uploader.videoUrl.isNotEmpty()) {
                                    append("\n")
                                    append(uploader.videoUrl)
                                }

                                if (uploader.fileUrl.isNotEmpty()) {
                                    append("\n")
                                    append(uploader.fileUrl)
                                }

                                append("\n")
                                append("uploadId: ${uploader.uploadId}")
                            }

                            it.addLog(LogType.UploadResult, result)
                        }
                    }

                    is UploadResult.Error -> {
                        _state.value = state.value.copy().also {
                            it.addLog(LogType.UploadResult, "Upload gagal: ${uploader.message}")
                        }
                    }
                }
            }
        }
    }

    private fun fileInfo(path: String): String {
        val videoInfo = if (path.asPickerFile().isVideo()) {
            val info = videoMetaDataExtractor.extract(path)

            buildString {
                append("bitrate: ${info?.bitrate} bps")
                append("\n")
                append("resolution:  ${info?.width} x ${info?.height} px")
            }
        } else {
            ""
        }

        return buildString {
            append("\n")
            append(path)
            append("\n")
            append("size: ${File(path).length().formattedToMB()} MB")
            append("\n")
            append(videoInfo)
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
