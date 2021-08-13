package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.Carousel
import timber.log.Timber

class CarouselImageDownloader(baseNotificationModel: BaseNotificationModel)
    : NotificationImageDownloader(baseNotificationModel) {

    override suspend fun verifyAndUpdate() {
        val faultyCarouselList = ArrayList<Carousel>()
        baseNotificationModel.carouselList.forEach { carousel ->
            if (carousel.filePath == null)
                faultyCarouselList.add(carousel)
        }
        baseNotificationModel.carouselList.removeAll(faultyCarouselList)
        if (baseNotificationModel.carouselList.isEmpty()) {
            baseNotificationModel.type = CMConstant.NotificationType.GENERAL
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                    mapOf("type" to "validation", "reason" to "image_download",
                            "data" to baseNotificationModel.toString().take(CMConstant.TimberTags.MAX_LIMIT)
                    ))
        }
    }

    override suspend fun downloadAndVerify(context: Context): BaseNotificationModel? {
        baseNotificationModel.carouselList.forEach { carousel ->
            carousel.icon?.let { icon ->
                val filePath = downloadAndStore(context, icon, ImageSizeAndTimeout.CAROUSEL)
                filePath?.let {
                    carousel.filePath = it
                }
            }
        }
        verifyAndUpdate()
        return baseNotificationModel
    }

}
