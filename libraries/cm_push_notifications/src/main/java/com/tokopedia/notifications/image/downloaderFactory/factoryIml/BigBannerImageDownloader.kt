package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel
import timber.log.Timber

class BigBannerImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun verifyAndUpdate() {
        baseNotificationModel.media?.run {
            (mediumQuality.startsWith(CMConstant.HTTP) || mediumQuality.startsWith(CMConstant.WWW)).let {
                if(it) {
                    baseNotificationModel.media = null
                    Timber.w("${CMConstant.TimberTags.TAG}validation;reason=image_download;data=$baseNotificationModel")
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
