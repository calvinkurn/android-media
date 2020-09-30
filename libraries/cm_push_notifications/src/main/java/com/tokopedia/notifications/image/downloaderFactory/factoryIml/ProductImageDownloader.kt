package com.tokopedia.notifications.image.downloaderFactory.factoryIml

import android.content.Context
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout.*
import com.tokopedia.notifications.image.downloaderFactory.NotificationImageDownloader
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationStatus
import timber.log.Timber

class ProductImageDownloader(baseNotificationModel: BaseNotificationModel)
    : NotificationImageDownloader(baseNotificationModel) {
    override suspend fun verifyAndUpdate() {
        baseNotificationModel.productInfoList.forEach { productInfo ->
            productInfo.productImage.run {
                if (startsWith(CMConstant.HTTP) || startsWith(CMConstant.WWW)) {
                    baseNotificationModel.status = NotificationStatus.COMPLETED
                    baseNotificationModel.type = CMConstant.NotificationType.DROP_NOTIFICATION
                    baseNotificationModel.productInfoList.clear()
                    Timber.w("${CMConstant.TimberTags.TAG}validation;reason='image_download';data='${
                    baseNotificationModel.toString().take(CMConstant.TimberTags.MAX_LIMIT)}'")
                    return
                }
            }
        }
    }

    override suspend fun downloadAndVerify(context: Context): BaseNotificationModel? {
        baseNotificationModel.productInfoList.forEach { product ->
            val productImage = downloadAndStore(context, product.productImage, PRODUCT_IMAGE)
            val freeOngkirIcon = downloadAndStore(context, product.freeOngkirIcon, FREE_ONGKIR)
            val starReviewIcon = downloadAndStore(context, R.drawable.cm_ic_star_review, STAR_REVIEW)

            productImage?.let { product.productImage = it }
            freeOngkirIcon?.let { product.freeOngkirIcon = it }
            starReviewIcon?.let { product.reviewIcon = it }
        }

        verifyAndUpdate()
        return baseNotificationModel
    }

}
