package com.tokopedia.logisticCommon.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.url.TokopediaUrl
import java.util.*

/**
 * Created by irpan on 23/05/22.
 */
object LogisticImageDeliveryHelper {

    private const val PATH_IMAGE_LOGISTIC = "logistic/tracking/get-delivery-image"
    private const val HEADER_KEY_AUTH = "Accounts-Authorization"
    private const val HEADER_VALUE_BEARER = "Bearer"

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
        context: Context,
        accessToken: String,
        url: String,
        drawableImagePlaceholder: Drawable? = null,
        drawableImageError: Drawable? = null,
        onReadyListener: ((Unit) -> Unit)? = null,
        onFailedListener: ((Unit) -> Unit)? = null
    ) {
        val authKey = String.format(Locale.getDefault(), "%s %s", HEADER_VALUE_BEARER, accessToken)
        val newUrl = GlideUrl(url, LazyHeaders.Builder().addHeader(HEADER_KEY_AUTH, authKey).build())

        this?.let { imgProof ->
            Glide.with(context)
                .load(newUrl)
                .listener(
                    (
                        object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                onFailedListener?.invoke(Unit)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                // no-ops
                                onReadyListener?.invoke(Unit)
                                return false
                            }
                        }
                        )
                )
                .placeholder(drawableImagePlaceholder)
                .error(drawableImageError)
                .dontAnimate()
                .into(imgProof)
        }
    }
}
