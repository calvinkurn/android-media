package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
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
                Timber.w("${CMConstant.TimberTags.TAG}validation;reason=image_download;data=$baseNotificationModel")
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
