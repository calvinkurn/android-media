package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel

class CarouselImageDownloader(baseNotificationModel: BaseNotificationModel)
    : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun downloadImages(context: Context): BaseNotificationModel? {
        baseNotificationModel.carouselList.forEach { carousel ->
            carousel.icon?.let { icon ->
                val filePath = downloadAndStore(context, icon, ImageSizeAndTimeout.CAROUSEL)
                filePath?.let {
                    carousel.filePath = filePath
                }
            }
        }
        return baseNotificationModel
    }

}
