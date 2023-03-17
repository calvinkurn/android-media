package com.tokopedia.feedplus.presentation.receiver

import kotlinx.coroutines.flow.Flow

/**
 * Created by kenny.hadisaputra on 14/03/23
 */
interface UploadReceiver {

    fun observe(): Flow<UploadInfo>
}

data class UploadInfo(
    val type: UploadType,
    val status: UploadStatus,
)

sealed interface UploadStatus {
    data class Progress(val progress: Int, val thumbnailUrl: String) : UploadStatus
    data class Finished(val contentId: String) : UploadStatus
    data class Failed(val thumbnailUrl: String, val onRetry:() -> Unit) : UploadStatus
}

enum class UploadType {
    Shorts,
    Post,
}
