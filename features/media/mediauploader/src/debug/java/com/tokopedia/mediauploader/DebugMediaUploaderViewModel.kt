package com.tokopedia.mediauploader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.mediauploader.common.cache.VideoCompressionCacheManager
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.video.data.repository.VideoMetaDataExtractorRepository
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/*
* We need to rename it from `**ViewModel` to `**StateManager`
* because the jacoco detects it and must be not part of
* unit test coverage for this module.
* */
class DebugMediaUploaderViewModel @Inject constructor(
    private val videoMetaDataExtractor: VideoMetaDataExtractorRepository,
    private val compressionCacheManager: VideoCompressionCacheManager,
    private val uploaderUseCase: UploaderUseCase
) : ViewModel(), DebugMediaUploaderViewModelContract {

    private val _state = MutableStateFlow(DebugMediaLoaderState())
    override val state: StateFlow<DebugMediaLoaderState> get() = _state

    private val _event = MutableSharedFlow<DebugMediaLoaderEvent>(replay = 50)

    init {
        viewModelScope.launch {
            _event.collect { event ->
                when(event) {
                    is DebugMediaLoaderEvent.FileChosen -> {
                        val filePaths = event.filePaths

                        _state.value = state.value.copy(filePaths = filePaths).also {
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

    override fun setAction(event: DebugMediaLoaderEvent) {
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
                        append("${cache?.compressedVideoPath}")
                        append("\n")
                        append("bitrate: ${cache?.compressedVideoMetadata?.bitrate} bps")
                        append("\n")
                        append("resolution: ${cache?.compressedVideoMetadata?.width} x ${cache?.compressedVideoMetadata?.height} px")
                        append("\n")
                        append("compression time: ${TimeUnit.MILLISECONDS.toSeconds(cache?.compressedTime ?: 0)} sec.")
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

interface DebugMediaUploaderViewModelContract {
    val state: StateFlow<DebugMediaLoaderState>

    fun setAction(event: DebugMediaLoaderEvent)
}
