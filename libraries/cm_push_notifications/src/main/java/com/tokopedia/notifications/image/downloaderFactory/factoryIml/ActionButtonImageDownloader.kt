package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel
import timber.log.Timber

class ActionButtonImageDownloader(baseNotificationModel: BaseNotificationModel)
    : NotificationImageDownloader(baseNotificationModel) {

    override suspend fun verifyAndUpdate() {
        baseNotificationModel.media?.run {
            if (mediumQuality.startsWith(CMConstant.HTTP) || mediumQuality.startsWith(CMConstant.WWW)) {
                baseNotificationModel.media = null
                Timber.w("${CMConstant.TimberTags.TAG}validation;reason=image_download;data=$baseNotificationModel")
            }
        }
    }

    override suspend fun downloadAndVerify(context: Context): BaseNotificationModel? {
        baseNotificationModel.media?.run {
            val filePath = downloadAndStore(context, mediumQuality, ImageSizeAndTimeout.BIG_IMAGE)
            filePath?.let {
                displayUrl = filePath
                mediumQuality = filePath
                highQuality = filePath
                lowQuality = filePath
                fallbackUrl = filePath
            }
        }
        baseNotificationModel.actionButton.forEach { actionButton ->
            actionButton.actionButtonIcon?.let { iconUrl ->
                val filePath = downloadAndStore(context, iconUrl, ImageSizeAndTimeout.ACTION_BUTTON_ICON)
                actionButton.actionButtonIcon = filePath
            }
        }

        verifyAndUpdate()
        return baseNotificationModel
    }
}