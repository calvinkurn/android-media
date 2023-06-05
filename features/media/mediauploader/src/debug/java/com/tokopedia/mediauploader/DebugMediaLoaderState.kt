package com.tokopedia.mediauploader

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.data.entity.LogType
import com.tokopedia.mediauploader.data.entity.Logs

data class DebugMediaLoaderState(
    var filePath: String = "",
    var progress: Pair<ProgressType, Int> = Pair(ProgressType.Upload, 0),
    val logs: SnapshotStateList<Pair<LogType, List<Logs>>> = mutableStateListOf(),
    val uploading: Boolean = false
) {

    fun reset() {
        logs.clear()
    }

    fun updateProgress(type: ProgressType, value: Int) {
        progress = Pair(type, value)
    }

    fun log(type: LogType, mLogs: List<Logs>) {
        logs.add(Pair(type, mLogs))
    }

    fun isCompressed(): Boolean {
        return logs.any { it.first == LogType.CompressInfo }
    }
}
