package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationStatus

class ProductImageDownloader(baseNotificationModel: BaseNotificationModel)
    : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun verifyAndUpdate() {
        baseNotificationModel.productInfoList.forEach { productInfo ->
            productInfo.productImage.run {
                if (startsWith(CMConstant.HTTP) || startsWith(CMConstant.WWW)) {
                    baseNotificationModel.status = NotificationStatus.COMPLETED
                    baseNotificationModel.type = CMConstant.NotificationType.DROP_NOTIFICATION
                    baseNotificationModel.productInfoList.clear()
                    return
                }
            }
        }
    }

    override suspend fun downloadAndVerify(context: Context): BaseNotificationModel? {
        baseNotificationModel.productInfoList.forEach { productInfo ->
            val productImage = downloadAndStore(
                    context,
                    productInfo.productImage,
                    ImageSizeAndTimeout.PRODUCT_IMAGE
            )
            val freeOngkirIcon = downloadAndStore(
                    context,
                    productInfo.freeDeliveryIcon,
                    ImageSizeAndTimeout.FREE_ONGKIR
            )

            productImage?.let { productInfo.productImage = it }
            freeOngkirIcon?.let { productInfo.freeDeliveryIcon = it }
        }
        verifyAndUpdate()
        return baseNotificationModel
    }
}
