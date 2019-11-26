package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel

class ImageNotificationImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {

    override suspend fun verifyAndUpdate() {
        baseNotificationModel.media?.run {
            if (mediumQuality.startsWith("http") || mediumQuality.startsWith("www")) {
                    baseNotificationModel.media = null
                    baseNotificationModel.type = CMConstant.NotificationType.GENERAL
            }
        }
    }

    override suspend fun downloadAndVerify(context: Context): BaseNotificationModel? {
        baseNotificationModel.media?.run {
            val filePath = downloadAndStore(context, displayUrl, ImageSizeAndTimeout.BIG_IMAGE)
            filePath?.let {
                displayUrl = it
                mediumQuality = it
                highQuality = it
                lowQuality = it
                fallbackUrl = it
            }
        }
        verifyAndUpdate()
        return baseNotificationModel
    }

}
