package com.tokopedia.creation.common.upload.uploader.manager

import com.tokopedia.creation.common.upload.model.CreationUploadQueue
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class StoriesUploadManager @Inject constructor(

) : CreationUploadManager {

    override suspend fun execute(
        uploadData: CreationUploadQueue,
        listener: CreationUploadManagerListener
    ) {

    }
}
