package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel

class ProductImageDownloader(baseNotificationModel: BaseNotificationModel)
    : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun downloadImages(context: Context): BaseNotificationModel? {
        baseNotificationModel.productInfoList.forEach { productInfo ->
            val filePath = downloadAndStore(context, productInfo.productImage, ImageSizeAndTimeout.PRODUCT_IMAGE)
            filePath?.let {
                productInfo.productImage = filePath
            }
        }
        return baseNotificationModel
    }
}
