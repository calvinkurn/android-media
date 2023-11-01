package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult

interface UploaderManager {

    suspend fun upload(param: BaseUploaderParam): UploadResult

    fun setProgressUploader(progress: ProgressUploader?)
}
