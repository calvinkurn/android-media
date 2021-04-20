package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationStatus
import timber.log.Timber

class PersistentImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun verifyAndUpdate() {
        baseNotificationModel.persistentButtonList?.forEach { persistentButton ->
            if (null == persistentButton.icon || persistentButton.icon!!.startsWith(CMConstant.HTTP) || persistentButton.icon!!.startsWith(CMConstant.WWW)) {
                baseNotificationModel.status = NotificationStatus.COMPLETED
                baseNotificationModel.type = CMConstant.NotificationType.DROP_NOTIFICATION
                ServerLogger.log(Priority.P2, "CM_VALIDATION",
                        mapOf("type" to "validation", "reason" to "image_download",
                                "data" to baseNotificationModel.toString().take(CMConstant.TimberTags.MAX_LIMIT)
                        ))
                return
            }
        }
    }

    override suspend fun downloadAndVerify(context: Context): BaseNotificationModel? {
        baseNotificationModel.persistentButtonList?.forEach { persistentButton ->
            if (!persistentButton.isAppLogo)
                persistentButton.icon?.let { iconUrl ->
                    val filePath = downloadAndStore(context, iconUrl, ImageSizeAndTimeout.ACTION_BUTTON_ICON)
                    persistentButton.icon = filePath
                }
        }
        verifyAndUpdate()
        return baseNotificationModel
    }

}
