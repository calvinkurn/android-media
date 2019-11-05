package com.tokopedia.notifications.image

import android.content.Context
import com.tokopedia.notifications.image.downloaderFactory.ImageFactory
import com.tokopedia.notifications.model.BaseNotificationModel

class ImageDownloadManager(val context: Context, val baseNotificationModel: BaseNotificationModel) {

    suspend fun startImageDownload(): BaseNotificationModel? {
        return ImageFactory.provideNotificationImageDownloader(baseNotificationModel)
                ?.downloadImages(context)
                ?: baseNotificationModel
    }

    companion object {
        suspend fun downloadImages(context: Context, baseNotificationModel: BaseNotificationModel): BaseNotificationModel? {
            return ImageDownloadManager(context, baseNotificationModel).startImageDownload()
        }
    }
}