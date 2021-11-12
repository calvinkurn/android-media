package com.tokopedia.logisticorder.utils

import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

object TrackingPageUtil {

    val PATH_IMAGE_LOGISTIC = "logistic/tracking/get-delivery-image"
    val HEADER_KEY_AUTH = "Accounts-Authorization"
    val HEADER_VALUE_BEARER = "Bearer"

    val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"
    val EXTRA_TRACKING_DATA_MODEL = "EXTRA_TRACKING_DATA_MODEL"

    fun getDeliveryImage(imageId: String, orderId: Long, size: String, userId: String, osType: Int, deviceId: String): String? {
        val baseUrl = TokopediaUrl.getInstance().API + PATH_IMAGE_LOGISTIC
        return "$baseUrl?order_id=$orderId&image_id=$imageId&size=$size&user_id=$userId&os_type=$osType&device_id=$deviceId"
    }
}