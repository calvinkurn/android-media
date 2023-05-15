package com.tokopedia.mediauploader

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

sealed class DebugMediaLoaderEvent {
    data class FileResult(val filePaths: List<String>) : DebugMediaLoaderEvent()

    object Upload : DebugMediaLoaderEvent()

    data class Succeed(
        val uploadId: String,
        val fileUrl: String,
        val videoUrl: String
    ) : DebugMediaLoaderEvent()

    data class Failed(
        val message: String
    ) : DebugMediaLoaderEvent()
}

data class DebugMediaLoaderState(
    var filePaths: List<String> = emptyList(),
    val logs: SnapshotStateList<Pair<LogType, String>> = mutableStateListOf(),
) {

    fun addLog(type: LogType, content: String) {
        logs.add(Pair(type, content))
    }

    fun addOrUpdateProgress(type: LogType, value: String) {
        val log = logs.lastOrNull { it.first == type }

        if (log == null) {
            addLog(type, value)
            return
        }

        val index = logs.indexOf(log)
        logs[index] = Pair(type, value)
    }
}

sealed class LogType {
    object FileInfo : LogType()
    object CompressInfo : LogType()
    object UploadResult : LogType()
    data class Progress(
        val type: String
    ) : LogType()

    companion object {
        fun map(type: LogType): String {
            return when (type) {
                is FileInfo -> "File Info"
                is CompressInfo -> "Compression Info"
                is UploadResult -> "Upload Result"
                is Progress -> "Progress: ${type.type}"
            }
        }
    }
}
