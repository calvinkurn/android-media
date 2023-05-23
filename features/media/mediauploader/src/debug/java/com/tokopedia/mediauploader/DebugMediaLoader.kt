package com.tokopedia.mediauploader

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tokopedia.mediauploader.common.state.ProgressType

sealed class DebugMediaLoaderEvent {
    data class FileChosen(val filePaths: List<String>) : DebugMediaLoaderEvent()
    object Upload : DebugMediaLoaderEvent()
}

data class DebugMediaLoaderState(
    var filePaths: List<String> = emptyList(),
    var progress: Pair<ProgressType, Int> = Pair(ProgressType.Upload, 0),
    val logs: SnapshotStateList<Pair<LogType, String>> = mutableStateListOf(),
) {

    init {
        val isWelcomingShown = logs.any { it.first == LogType.Welcome }

        if (!isWelcomingShown) {
            addLog(
                LogType.Welcome,
                "Welcome to Media-uploader debug page! Please browse the file and set the config!"
            )
        }
    }

    fun clear() {
        logs.clear()
    }

    fun updateProgress(type: ProgressType, value: Int) {
        progress = Pair(type, value)
    }

    fun addLog(type: LogType, content: String) {
        logs.add(Pair(type, content))
    }
}

sealed class LogType {
    object Welcome : LogType()
    object FileInfo : LogType()
    object CompressInfo : LogType()
    object UploadResult : LogType()

    companion object {
        fun map(type: LogType): String {
            return when (type) {
                is Welcome -> "Media Uploader!"
                is FileInfo -> "File Info"
                is CompressInfo -> "Compression Info"
                is UploadResult -> "Upload Result"
            }
        }
    }
}
