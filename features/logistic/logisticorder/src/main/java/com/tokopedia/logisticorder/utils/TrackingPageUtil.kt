package com.tokopedia.logisticorder.utils

import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

object TrackingPageUtil {

    const val PATH_IMAGE_LOGISTIC = "logistic/tracking/get-delivery-image"
    const val HEADER_KEY_AUTH = "Accounts-Authorization"
    const val HEADER_VALUE_BEARER = "Bearer"
    const val IMAGE_SMALL_SIZE = "small"
    const val DEFAULT_OS_TYPE = 1
    const val IMAGE_LARGE_SIZE = "large"


    fun getDeliveryImage(imageId: String, orderId: Long, size: String, userId: String, osType: Int, deviceId: String): String {
        val baseUrl = TokopediaUrl.getInstance().API + PATH_IMAGE_LOGISTIC
        return "$baseUrl?order_id=$orderId&image_id=$imageId&size=$size&user_id=$userId&os_type=$osType&device_id=$deviceId"
    }
}