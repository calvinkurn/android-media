package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel

class GridImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {

    override suspend fun downloadImages(context: Context): BaseNotificationModel? {
        baseNotificationModel.gridList.forEach { grid ->
            grid.img?.let { imgUrl ->
                val filePath = downloadAndStore(context, imgUrl, ImageSizeAndTimeout.BIG_IMAGE)
                filePath?.let {
                    grid.img = filePath
                }
            }
        }
        return baseNotificationModel
    }

}
