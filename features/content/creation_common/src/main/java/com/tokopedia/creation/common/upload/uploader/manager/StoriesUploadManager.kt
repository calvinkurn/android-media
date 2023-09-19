package com.tokopedia.creation.common.upload.uploader.manager

import com.tokopedia.creation.common.upload.model.CreationUploadQueue
import com.tokopedia.creation.common.upload.uploader.notification.StoriesUploadNotificationManager
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class StoriesUploadManager @Inject constructor(
    private val notificationManager: StoriesUploadNotificationManager,
) : CreationUploadManager {

    override suspend fun execute(
        uploadData: CreationUploadQueue,
        listener: CreationUploadManagerListener
    ) {
        /** TODO JOE: for mocking purpose */
        notificationManager.init(uploadData)
        listener.setupForegroundNotification(notificationManager.onStart())

        var progress = 0
        repeat(5) {
            progress += 20
            if (it == 2) {
                listener.setProgress(uploadData, -1)
                notificationManager.onError()

                return@repeat
            }

            listener.setProgress(uploadData, progress)
            notificationManager.onProgress(progress)
        }
    }
}
