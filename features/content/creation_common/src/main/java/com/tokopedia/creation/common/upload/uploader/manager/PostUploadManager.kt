package com.tokopedia.creation.common.upload.uploader.manager

import com.tokopedia.creation.common.upload.model.CreationUploadData
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 21, 2023
 */
class PostUploadManager @Inject constructor(
) : CreationUploadManager {

    override suspend fun execute(
        uploadData: CreationUploadData,
        listener: CreationUploadManagerListener
    ): Boolean {
        TODO("Not yet implemented")
    }
}
