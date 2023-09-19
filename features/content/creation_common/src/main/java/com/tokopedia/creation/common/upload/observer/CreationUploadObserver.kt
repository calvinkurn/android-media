package com.tokopedia.creation.common.upload.observer

import com.tokopedia.creation.common.upload.model.CreationUploadResult
import kotlinx.coroutines.flow.Flow

/**
 * Created By : Jonathan Darwin on September 19, 2023
 */
interface CreationUploadObserver {

    fun observe(): Flow<CreationUploadResult>
}
