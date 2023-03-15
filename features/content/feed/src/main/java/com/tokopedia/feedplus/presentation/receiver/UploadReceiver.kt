package com.tokopedia.feedplus.presentation.receiver

import kotlinx.coroutines.flow.Flow

/**
 * Created by kenny.hadisaputra on 14/03/23
 */
interface UploadReceiver {

    fun observe(): Flow<UploadInfo>
}

data class UploadInfo(
    val progress: Int,
    val thumbnailUrl: String,
)
