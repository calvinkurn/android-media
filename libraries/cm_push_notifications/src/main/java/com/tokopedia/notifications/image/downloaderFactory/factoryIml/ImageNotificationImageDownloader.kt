package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel
import timber.log.Timber

class ImageNotificationImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {

    override suspend fun verifyAndUpdate() {
        baseNotificationModel.media?.run {
            if (mediumQuality.startsWith(CMConstant.HTTP) || mediumQuality.startsWith(CMConstant.WWW)) {
                baseNotificationModel.media = null
                baseNotificationModel.type = CMConstant.NotificationType.GENERAL
                ServerLogger.log(Priority.P2, "CM_VALIDATION",
                        mapOf("type" to "validation", "reason" to "image_download",
                                "data" to baseNotificationModel.toString().take(CMConstant.TimberTags.MAX_LIMIT)
                        ))
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
