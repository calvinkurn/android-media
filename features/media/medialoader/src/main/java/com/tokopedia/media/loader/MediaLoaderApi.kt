package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.media.loader.data.*
import com.tokopedia.media.loader.options.*
import com.tokopedia.media.loader.listener.MediaListenerBuilder
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.utils.generateUrl

internal object MediaLoaderApi {

    private val handler by lazy(LazyThreadSafetyMode.NONE) {
        Handler()
    }

    fun loadImage(imageView: ImageView, properties: Properties) {
        val context = imageView.context

        // startTimeRequest will use for performance tracking
        val startTimeRequest = System.currentTimeMillis()

        // handling empty url
        if (properties.data is String && properties.data.toString().isEmpty()) {
            return
        }

        // if the data source is null, the image will be render the error drawable
        if (properties.data == null) {
            imageView.setImageDrawable(getDrawable(context, properties.error))
            return
        }

        GlideApp
            .with(context)
            .asBitmap()
            .apply(CommonOptions.build(properties))
            .apply(TransformationOptions.build(properties))
            .apply(PlaceholderOptions.build(context, properties))
            .apply {
                // set custom thumbnail
                setThumbnailUrl(context, properties)

                // get image-view size
                properties.setImageSize(
                    width = imageView.measuredWidth,
                    height = imageView.measuredHeight
                )

                // callback listener
                listener(
                    MediaListenerBuilder.callback(
                        context,
                        properties,
                        startTimeRequest
                    )
                )

                // load image
                val request = if (properties.data is String) {
                    load(properties.generateUrl())
                } else {
                    load(properties.data)
                }

                // render image
                if (properties.renderDelay <= 0L) {
                    request.into(imageView)
                } else {
                    handler.postDelayed({
                        request.into(imageView)
                    }, properties.renderDelay)
                }
            }
    }

    // temporarily the GIF loader
    fun loadGifImage(imageView: ImageView, source: String, properties: Properties) {
        val context = imageView.context.applicationContext

        if (context.isValid()) {
            GlideApp
                .with(context)
                .asGif()
                .apply(CommonOptions.build(properties))
                .apply(TransformationOptions.build(properties))
                .load(source)
                .into(imageView)
        }
    }

    fun setThumbnailUrl(context: Context, properties: Properties): RequestBuilder<Bitmap>? {
        if (properties.thumbnailUrl.isEmpty()) return null

        return GlideApp
            .with(context)
            .asBitmap()
            .fitCenter()
            .load(properties.thumbnailUrl)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    }

}
