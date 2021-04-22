package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel
import java.util.*

class BigBannerImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun verifyAndUpdate() {
        baseNotificationModel.media?.run {
            (mediumQuality.startsWith(CMConstant.HTTP) || mediumQuality.startsWith(CMConstant.WWW)).let {
                if(it) {
                    baseNotificationModel.media = null
                    val messageMap: MutableMap<String, String> = HashMap()
                    messageMap["type"] = "validation"
                    messageMap["reason"] = "image_download"
                    messageMap["data"] = baseNotificationModel.toString().take(CMConstant.TimberTags.MAX_LIMIT)
                    ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap)
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
