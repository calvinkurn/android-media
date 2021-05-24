package com.tokopedia.logisticorder.utils

import com.tokopedia.url.TokopediaUrl

object TrackingPageUtil {

    val PATH_IMAGE_LOGISTIC = "logistic/tracking/get-delivery-image"

    fun getDeliveryImage(imageId: String, orderId: Long, size: String): String? {
        val baseUrl = TokopediaUrl.getInstance().API + PATH_IMAGE_LOGISTIC
        return "$baseUrl?order_id=$orderId&image_id=$imageId&size=$size"
    }
}