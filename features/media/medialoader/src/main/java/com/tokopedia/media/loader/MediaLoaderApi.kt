package com.tokopedia.media.loader

import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.factory.BitmapFactory
import com.tokopedia.media.loader.factory.GifFactory
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.data.HEADER_KEY_AUTH
import com.tokopedia.media.loader.data.HEADER_USER_ID
import com.tokopedia.media.loader.data.HEADER_X_DEVICE
import com.tokopedia.media.loader.data.PREFIX_BEARER
import com.tokopedia.media.loader.data.PLACEHOLDER_RES_UNIFY
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.transform.TopRightCrop

internal object MediaLoaderApi {

    private val handler by lazy(LazyThreadSafetyMode.NONE) { Handler() }

    private val bitmap by lazy { BitmapFactory() }
    private val gif by lazy { GifFactory() }

    internal fun LazyHeaders.Builder.headers(accessToken: String, userId: String) {
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
                    val source = Loader.get(properties.data.toString())

                    // get the imageView size
                    properties.setImageSize(
                        width = imageView.measuredWidth,
                        height = imageView.measuredHeight
                    )

                    properties.setUrlHasQuality(source)

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

    // for custom transform
    fun loadImage(imageView: ImageView, source: String?) {
        if (source != null && source.isNotEmpty()) {
            GlideApp.with(imageView.context)
                .load(source)
                .transform(TopRightCrop())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(PLACEHOLDER_RES_UNIFY)
                .error(ERROR_RES_UNIFY)
                .into(imageView)
        }
    }

    // temporarily the GIF loader
    fun loadGifImage(imageView: ImageView, source: String, properties: Properties) {
        val context = imageView.context.applicationContext

        if (context.isValid()) {
            GlideApp.with(context)
                .asGif()
                .load(source)
                .apply { gif.build(properties, this) }
                .into(imageView)
        }
    }

}
