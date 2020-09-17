package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationStatus
import timber.log.Timber

class VisualImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun verifyAndUpdate() {
        baseNotificationModel.visualCollapsedImageUrl?.run {
            if (startsWith(CMConstant.HTTP) || startsWith(CMConstant.WWW)) {
                baseNotificationModel.status = NotificationStatus.COMPLETED
                baseNotificationModel.type = CMConstant.NotificationType.DROP_NOTIFICATION
                Timber.w("${CMConstant.TimberTags.TAG}validation;reason=image_download_collapsed;data=$baseNotificationModel")
                return
            }
        }
        baseNotificationModel.visualExpandedImageUrl?.run {
            if (startsWith(CMConstant.HTTP) || startsWith(CMConstant.WWW)) {
                baseNotificationModel.status = NotificationStatus.COMPLETED
                baseNotificationModel.type = CMConstant.NotificationType.DROP_NOTIFICATION
                Timber.w("${CMConstant.TimberTags.TAG}validation;reason=image_download_expanded;data=$baseNotificationModel")
            }
        }
    }

    override suspend fun downloadAndVerify(context: Context): BaseNotificationModel? {
        baseNotificationModel.visualCollapsedImageUrl?.let {
            val filePath = downloadAndStore(context, it, ImageSizeAndTimeout.VISUAL_COLLAPSED)
            filePath?.let { fp ->
                baseNotificationModel.visualCollapsedImageUrl = fp
            }
        }

        baseNotificationModel.visualExpandedImageUrl?.let {
            val filePath = downloadAndStore(context, it, ImageSizeAndTimeout.VISUAL_EXPANDED)
            filePath?.let { fp ->
                baseNotificationModel.visualExpandedImageUrl = fp
            }
        }
        verifyAndUpdate()
        return baseNotificationModel
    }

}
