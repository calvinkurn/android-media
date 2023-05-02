package com.tokopedia.logisticCommon.util

import android.widget.ImageView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.url.TokopediaUrl

/**
 * Created by irpan on 23/05/22.
 */
object LogisticImageDeliveryHelper {

    private const val PATH_IMAGE_LOGISTIC = "logistic/tracking/get-delivery-image"

    const val IMAGE_SMALL_SIZE = "small"
    const val IMAGE_LARGE_SIZE = "large"

    const val DEFAULT_OS_TYPE = 1

    // generate url image
    fun getDeliveryImage(imageId: String, orderId: Long, size: String, userId: String, osType: Int, deviceId: String): String {
        val baseUrl = TokopediaUrl.getInstance().API + PATH_IMAGE_LOGISTIC
        return "$baseUrl?order_id=$orderId&image_id=$imageId&size=$size&user_id=$userId&os_type=$osType&device_id=$deviceId"
    }

    // use for load image POD
    fun ImageView.loadImagePod(
        accessToken: String,
        url: String,
        drawableImagePlaceholder: Int? = null,
        drawableImageError: Int? = null,
        onReadyListener: ((Unit) -> Unit)? = null,
        onFailedListener: ((Unit) -> Unit)? = null
    ) {
        this.loadImage(url) {
            isSecure(true)
            userSessionAccessToken(accessToken)
            drawableImageError?.let { error -> setErrorDrawable(error) }
            drawableImagePlaceholder?.let { placeholder -> setPlaceHolder(placeholder) }
            isAnimate(false)
            listener(
                onSuccess = { _, _ ->
                    onReadyListener?.invoke(Unit)
                },
                onError = {
                    onFailedListener?.invoke(Unit)
                }
            )
        }
    }
}
