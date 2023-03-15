package com.tokopedia.feedplus.presentation.receiver

import kotlinx.coroutines.flow.Flow

/**
 * Created by kenny.hadisaputra on 14/03/23
 */
interface UploadReceiver {

    fun observe(): Flow<UploadInfo>
}

sealed interface UploadInfo {
    data class Progress(val progress: Int, val thumbnailUrl: String) : UploadInfo
    object Finished : UploadInfo
    data class Failed(val thumbnailUrl: String, val onRetry:() -> Unit) : UploadInfo
}
