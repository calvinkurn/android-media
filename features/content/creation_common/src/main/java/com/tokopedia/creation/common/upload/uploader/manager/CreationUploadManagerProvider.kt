package com.tokopedia.creation.common.upload.uploader.manager

import com.tokopedia.creation.common.upload.model.CreationUploadData
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class CreationUploadManagerProvider @Inject constructor(
    private val postUploadManagerFactory: PostUploadManager.Factory,
    private val shortsUploadManagerFactory: ShortsUploadManager.Factory,
    private val storiesUploadManagerFactory: StoriesUploadManager.Factory,
) {

    fun get(uploadData: CreationUploadData): CreationUploadManager {
        return when (uploadData) {
            is CreationUploadData.Post -> postUploadManagerFactory.create(uploadData)
            is CreationUploadData.Shorts -> shortsUploadManagerFactory.create(uploadData)
            is CreationUploadData.Stories -> storiesUploadManagerFactory.create(uploadData)
        }
    }
}
