package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel

class VisualImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun downloadImages(context: Context): BaseNotificationModel? {
        baseNotificationModel.visualCollapsedImageUrl?.let {
            val filePath = downloadAndStore(context, it , ImageSizeAndTimeout.BIG_IMAGE)
            filePath?.let {
                baseNotificationModel.visualCollapsedImageUrl = filePath
            }
        }

        baseNotificationModel.visualExpandedImageUrl?.let {
            val filePath = downloadAndStore(context, it , ImageSizeAndTimeout.BIG_IMAGE)
            filePath?.let {
                baseNotificationModel.visualExpandedImageUrl = filePath
            }
        }
        return null
    }

}
