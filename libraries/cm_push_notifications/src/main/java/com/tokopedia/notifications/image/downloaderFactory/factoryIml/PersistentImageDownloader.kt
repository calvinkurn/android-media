package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel

class PersistentImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun verifyAndUpdate() {
        baseNotificationModel.persistentButtonList?.forEach { persistentButton ->
            if (null == persistentButton.icon || persistentButton.icon!!.startsWith("http") || persistentButton.icon!!.startsWith("www")) {
                baseNotificationModel.type = CMConstant.NotificationType.DROP_NOTIFICATION
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
