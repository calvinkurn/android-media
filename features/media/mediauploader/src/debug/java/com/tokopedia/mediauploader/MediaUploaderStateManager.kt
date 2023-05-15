package com.tokopedia.mediauploader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.mediauploader.common.internal.VideoCompressionCacheManager
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.ui.isVideo
import com.tokopedia.mediauploader.video.data.repository.VideoMetaDataExtractorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

/*
* We need to rename it from `**ViewModel` to `**StateManager`
* because the jacoco detects it and must be not part of
* unit test coverage for this module.
* */
class MediaUploaderStateManager @Inject constructor(
    private val videoMetaDataExtractor: VideoMetaDataExtractorRepository,
    private val compressionCacheManager: VideoCompressionCacheManager,
    private val uploaderUseCase: UploaderUseCase
) : ViewModel() {

    private val _uploading = MutableLiveData<UploadState>()
    val uploading: LiveData<UploadState> get() = _uploading

    fun setUploadingStatus(uploading: UploadState) {
        _uploading.value = uploading
    }

    private val _state = MutableStateFlow(DebugMediaLoaderState())
    val state: StateFlow<DebugMediaLoaderState> get() = _state

    private val _event = MutableSharedFlow<DebugMediaLoaderEvent>(replay = 50)

    init {
        viewModelScope.launch {
            _event.collect { event ->
                when(event) {
                    is DebugMediaLoaderEvent.FileResult -> {
                        val filePaths = event.filePaths

                        _state.value = state.value.copy(filePaths = filePaths).also {
                            it.addLog(LogType.FileInfo, fileInfo(filePaths.first()))
                        }
                    }
                    is DebugMediaLoaderEvent.Succeed -> {
                        _state.value = state.value.copy().also {
                            val result = buildString {
                                if (event.videoUrl.isNotEmpty()) {
                                    append("\n")
                                    append("videoUrl: ${event.videoUrl}")
                                }

                                if (event.fileUrl.isNotEmpty()) {
                                    append("\n")
                                    append("fileUrl: ${event.fileUrl}")
                                }

                                append("\n")
                                append("uploadId: ${event.uploadId}")
                            }

                            it.addLog(LogType.UploadResult, result)
                        }
                    }
                    is DebugMediaLoaderEvent.Failed -> {
                        _state.value = state.value.copy().also {
                            it.addLog(LogType.UploadResult, "Upload gagal: ${event.message}")
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

    fun setAction(event: DebugMediaLoaderEvent) {
        _event.tryEmit(event)
    }

    private fun onFileUpload(path: String) {
        val param = uploaderUseCase.createParams(
            sourceId = "exwbZW",
            filePath = File(path),
            withTranscode = false,
            isSecure = false,
            shouldCompress = true
        )

        uploaderUseCase.trackProgress { i, type ->
            _state.value = state.value.copy().also {
                if (
                    type == ProgressType.Upload &&
                    param.getBoolean("should_compress", false) &&
                    it.logs.firstOrNull { it.first == LogType.CompressInfo } == null
                ) {
                    it.addLog(LogType.CompressInfo, buildString {
                        val cache = compressionCacheManager.get("exwbZW", path)

                        append("\n")
                        append("compressed: ${cache?.compressedVideoPath}")
                        append("\n")
                        append("bitrate: ${cache?.compressedVideoMetadata?.bitrate} bps")
                        append("\n")
                        append("resolution: ${cache?.compressedVideoMetadata?.width} x ${cache?.compressedVideoMetadata?.height} px")
                        append("\n")
                        append("size: ${
                            try {
                                File(cache?.compressedVideoPath.toString()).length().formattedToMB()
                            } catch (ignored: Throwable) {
                                "0"
                            }
                        } MB")
                    })
                }

                it.addOrUpdateProgress(
                    LogType.Progress(
                        if (type == ProgressType.Compression) {
                            "Compression"
                        } else {
                            "Upload"
                        }
                    ), buildString {
                        append("\n")
                        append("$i%")
                    }
                )
            }
        }

        viewModelScope.launch {
            val uploader = uploaderUseCase(param)

            withContext(Dispatchers.Main) {
                when (uploader) {
                    is UploadResult.Success -> {
                        setAction(DebugMediaLoaderEvent.Succeed(
                            uploadId = uploader.uploadId,
                            fileUrl = uploader.fileUrl,
                            videoUrl = uploader.videoUrl
                        ))
                    }
                    is UploadResult.Error -> {
                        setAction(DebugMediaLoaderEvent.Failed(
                            uploader.message
                        ))
                    }
                }
            }
        }
    }

    private fun fileInfo(path: String): String {
        val videoInfo = if (path.isVideo()) {
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
            append("path: $path")
            append("\n")
            append("size: ${File(path).length().formattedToMB()} MB")
            append("\n")
            append(videoInfo)
        }
    }

    companion object {
        sealed class UploadState {
            object Idle: UploadState()
            data class Uploading(
                val withWorker: Boolean
            ): UploadState()
            object Stopped: UploadState()
            object Aborted: UploadState()
            data class Finished(
                val result: UploadResult
            ): UploadState()
        }
    }
}
