package com.tokopedia.creation.common.upload.uploader.manager

import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadResult
import com.tokopedia.creation.common.upload.uploader.notification.StoriesUploadNotificationManager
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class StoriesUploadManager @Inject constructor(
    private val notificationManager: StoriesUploadNotificationManager,
) : CreationUploadManager {

    override suspend fun execute(
        uploadData: CreationUploadData,
        listener: CreationUploadManagerListener
    ): Boolean {
        /** TODO JOE: for mocking purpose */
        notificationManager.init(uploadData)
        listener.setupForegroundNotification(notificationManager.onStart())

        var progress = 0
        repeat(5) {
            progress += 20
            delay(2000)
            if (it == 2) {
                listener.setProgress(uploadData, -1)
                notificationManager.onError()

                return false
            }

            listener.setProgress(uploadData, progress)
            notificationManager.onProgress(progress)
        }

        return true
    }
}
