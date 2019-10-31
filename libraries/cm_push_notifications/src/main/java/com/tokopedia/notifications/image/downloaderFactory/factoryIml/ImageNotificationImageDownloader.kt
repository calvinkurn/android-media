package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel

class ImageNotificationImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun downloadImages(context: Context): BaseNotificationModel? {
        baseNotificationModel.media?.let { media ->
            val filePath = downloadAndStore(context, media.displayUrl, ImageSizeAndTimeout.BIG_IMAGE)
            filePath?.let {
                media.displayUrl = filePath
                media.mediumQuality = filePath
                media.highQuality = filePath
                media.lowQuality = filePath
                media.fallbackUrl = filePath
            }
        }
        return baseNotificationModel
    }

}
