package com.tokopedia.creation.common.upload.uploader.manager

import com.tokopedia.creation.common.upload.model.CreationUploadQueue
import com.tokopedia.creation.common.upload.notification.CreationUploadNotificationManager
import com.tokopedia.creation.common.upload.notification.ShortsUploadNotificationManager
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class ShortsUploadManager @Inject constructor(
    private val notificationManager: ShortsUploadNotificationManager,
) : CreationUploadManager {

    override fun execute(data: CreationUploadQueue) {
        TODO("Not yet implemented")
    }

}
