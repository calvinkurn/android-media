package com.tokopedia.media.loader.v1

import android.os.Handler
import android.widget.ImageView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.data.*
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.v1.factory.BitmapFactory
import com.tokopedia.media.loader.v1.util.UrlBuilder

internal object LegacyMediaLoaderApi {

    private val handler by lazy(LazyThreadSafetyMode.NONE) { Handler() }
    private val bitmap by lazy { BitmapFactory() }

    private fun LazyHeaders.Builder.headers(accessToken: String, userId: String) {
        addHeader(HEADER_KEY_AUTH /* Accounts-Authorization */, "$PREFIX_BEARER %s".format(accessToken))
        addHeader(HEADER_X_DEVICE /* X-Device */, "android-${GlobalConfig.VERSION_NAME}")
        addHeader(HEADER_USER_ID /* Tkpd-UserId */, userId)
    }

    fun loadImage(imageView: ImageView, properties: Properties, isSecure: Boolean) {
        val context = imageView.context

        // handling empty url
        if (properties.data is String && properties.data.toString().isEmpty()) {
            return
        }

        if (properties.data == null) {
            // if the data source is null, the image will be render the error drawable
            imageView.setImageDrawable(getDrawable(context, properties.error))
            return
        }

        GlideApp.with(context).asBitmap().apply {

            val request = when(properties.data) {
                /*
                * currently, this builder only support for URL,
                * will supporting URL, drawable, etc. later
                * */
                is String -> {
                    // url builder
                    val source = UrlBuilder
                        .build(context)
                        .buildUrl(properties.data.toString())

                    // get the imageView size
                    properties.setImageSize(
                        width = imageView.measuredWidth,
                        height = imageView.measuredHeight
                    )

                    bitmap.build(
                        context = context,
                        properties = properties,
                        request = this
                    ).load(
                        if (!isSecure) source
                        else {
                            GlideUrl(source, LazyHeaders.Builder()
                                .also {
                                    it.headers(
                                        accessToken = properties.accessToken,
                                        userId = properties.userId
                                    )
                                }
                                .build()
                            )
                        }
                    )
                }
                else -> {
                    bitmap.build(
                        context = context,
                        properties = properties,
                        request = this
                    ).load(properties.data)
                }
            }

            // handling image delayed display
            if (properties.renderDelay <= 0L) {
                request.into(imageView)
            } else {
                handler.postDelayed({
                    request.into(imageView)
                }, properties.renderDelay)
            }
        }
    }
}
