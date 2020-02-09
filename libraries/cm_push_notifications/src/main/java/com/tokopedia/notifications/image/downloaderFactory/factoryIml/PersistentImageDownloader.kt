package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel

class PersistentImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun downloadImages(context: Context): BaseNotificationModel? {
        baseNotificationModel.persistentButtonList?.forEach { persistentButton ->
            if (!persistentButton.isAppLogo)
                persistentButton.icon?.let { iconUrl ->
                    val filePath = downloadAndStore(context, iconUrl, ImageSizeAndTimeout.ACTION_BUTTON_ICON)
                    filePath?.let {
                        persistentButton.icon = filePath
                    }
                }
        }
        return baseNotificationModel
    }

}
