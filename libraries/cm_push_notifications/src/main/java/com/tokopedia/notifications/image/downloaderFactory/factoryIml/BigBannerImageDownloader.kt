package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel

class BigBannerImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun verifyAndUpdate() {
        baseNotificationModel.media?.run {
            (mediumQuality.startsWith("http") || mediumQuality.startsWith("www")).let {
                if(it) {
                    baseNotificationModel.media = null
                }
            }
        }
    }

    override suspend fun downloadAndVerify(context: Context): BaseNotificationModel? {
        baseNotificationModel.media?.let { media ->
            val filePath = downloadAndStore(context, media.mediumQuality, ImageSizeAndTimeout.BIG_IMAGE)
            filePath?.let {
                media.displayUrl = filePath
                media.mediumQuality = filePath
                media.highQuality = filePath
                media.lowQuality = filePath
                media.fallbackUrl = filePath
            }
        }
        verifyAndUpdate()
        return baseNotificationModel
    }

}
