package com.tokopedia.creation.common.upload.util.logger

import com.tokopedia.creation.common.upload.model.CreationUploadData

/**
 * Created By : Jonathan Darwin on October 26, 2023
 */
interface CreationUploadLogger {

    fun sendLog(uploadData: CreationUploadData, throwable: Throwable)

    fun sendLog(uploadData: String, throwable: Throwable)

    fun sendLog(throwable: Throwable)
}
