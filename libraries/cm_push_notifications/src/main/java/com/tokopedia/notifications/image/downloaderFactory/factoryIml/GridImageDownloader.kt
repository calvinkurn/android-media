package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel

class GridImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun verifyAndUpdate() {
        baseNotificationModel.gridList.forEach { grid ->
            grid.img?.run {
                if (startsWith("http") || startsWith("www")) {
                    baseNotificationModel.type = CMConstant.NotificationType.GENERAL
                    baseNotificationModel.gridList.clear()
                    return
                }
            }
        }
    }

    override suspend fun downloadAndVerify(context: Context): BaseNotificationModel? {
        baseNotificationModel.gridList.forEach { grid ->
            grid.img?.let { imgUrl ->
                val filePath = downloadAndStore(context, imgUrl, ImageSizeAndTimeout.BIG_IMAGE)
                filePath?.let {
                    grid.img = it
                }
            }
        }
        verifyAndUpdate()
        return baseNotificationModel
    }

}
