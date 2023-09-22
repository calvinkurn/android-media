package com.tokopedia.creation.common.upload.uploader.manager

import com.tokopedia.creation.common.upload.model.CreationUploadType
import com.tokopedia.creation.common.upload.model.exception.NoUploadManagerException
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class CreationUploadManagerProvider @Inject constructor(
    private val postUploadManager: PostUploadManager,
    private val shortsUploadManager: ShortsUploadManager,
    private val storiesUploadManager: StoriesUploadManager,
) {

    fun get(uploadType: CreationUploadType): CreationUploadManager {
        return when (uploadType) {
            CreationUploadType.Post -> postUploadManager
            CreationUploadType.Shorts -> shortsUploadManager
            CreationUploadType.Stories -> storiesUploadManager
            CreationUploadType.Unknown -> throw NoUploadManagerException()
        }
    }
}
