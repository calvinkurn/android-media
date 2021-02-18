package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.Loader
import com.tokopedia.media.common.data.CDN_IMAGE_URL
import com.tokopedia.media.common.data.PARAM_BLURHASH
import com.tokopedia.media.common.data.toUri
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.common.builder.GlideBitmapBuilder
import com.tokopedia.media.loader.common.builder.GlideGifBuilder
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.tracker.PerformanceTracker
import com.tokopedia.media.loader.utils.MediaTarget

internal object LoaderApi {

    private val bitmapBuilder by lazy { GlideBitmapBuilder() }
    private val gifBuilder by lazy { GlideGifBuilder() }

    @JvmOverloads
    fun loadImage(imageView: ImageView, properties: Properties) {
        loadImage(imageView.context, properties, MediaTarget(imageView, onReady = {
            imageView.setImageBitmap(it)
        }))
    }

    // temporarily the GIF loader
    fun loadGifImage(imageView: ImageView, data: String, properties: Properties) {
        val context = imageView.context

        GlideApp.with(context)
                .asGif()
                .load(data)
                .apply { gifBuilder.build(properties, this) }
                .into(imageView)
    }

    private fun automateScaleType(
            imageView: ImageView,
            request: GlideRequest<Bitmap>
    ): GlideRequest<Bitmap> {
        return request.apply {
            when (imageView.scaleType) {
                ImageView.ScaleType.FIT_CENTER -> fitCenter()
                ImageView.ScaleType.CENTER_CROP -> centerCrop()
                ImageView.ScaleType.CENTER_INSIDE -> centerInside()
                else -> {}
            }
        }
    }

    @JvmOverloads
    fun <T: View> loadImage(context: Context, properties: Properties, target: MediaTarget<T>) {
        var tracker: PerformanceMonitoring? = null

        // handling empty url
        if (properties.data is String && properties.data.toString().isEmpty()) {
            return
        }

        if (target is ImageView && properties.data == null) {
            // if the data source is null, the image will be render the error drawable
            target.setImageDrawable(getDrawable(context, properties.error))
            return
        }

        GlideApp.with(context).asBitmap().apply {

            if (target is ImageView) {
                /*
                * automateScaleType mean, the image will be scaled automatically
                * based on scaleType attribute which is already defined on ImageView
                * */
                automateScaleType(target, this)
            }

            when(properties.data) {
                is String -> {
                    // url builder
                    val source = Loader.urlBuilder(properties.data.toString())

                    /*
                    * get the hash of image blur (placeholder) from the URL, example:
                    * https://images.tokopedia.net/samples.png?b=abc123
                    * the hash of blur is abc123
                    * */
                    val blurHash = source.toUri()?.getQueryParameter(PARAM_BLURHASH)

                    /*
                    * only track the performance monitoring for a new domain,
                    * which is already using CDN services, 'images.tokopedia.net'.
                    * */
                    if (source.contains(CDN_IMAGE_URL)) {
                        tracker = PerformanceTracker.preRender(source, context)
                    }

                    bitmapBuilder.build(
                            context = context,
                            blurHash = blurHash,
                            properties = properties,
                            performanceMonitoring = tracker,
                            request = this
                    ).load(source)
                }
                else -> {
                    bitmapBuilder.build(
                            context = context,
                            properties = properties,
                            request = this
                    ).load(properties.data)
                }

            }.into(target)
        }
    }

}