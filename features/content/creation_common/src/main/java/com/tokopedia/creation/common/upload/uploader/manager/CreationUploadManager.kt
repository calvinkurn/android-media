package com.tokopedia.creation.common.upload.uploader.manager

import com.tokopedia.creation.common.upload.model.CreationUploadQueue

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
interface CreationUploadManager {

    fun execute(data: CreationUploadQueue)
}
