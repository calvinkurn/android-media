package com.tokopedia.media.loader

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.load.MultiTransformation
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.Loader
import com.tokopedia.media.common.data.CDN_IMAGE_URL
import com.tokopedia.media.common.data.PARAM_BLURHASH
import com.tokopedia.media.common.data.toUri
import com.tokopedia.media.loader.common.GlideBuilder
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.tracker.PerformanceTracker
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy.Companion.mapToDiskCacheStrategy

internal object LoaderApi {

    private val builder by lazy { GlideBuilder() }

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
    fun loadImage(imageView: ImageView, properties: Properties) {
        var tracker: PerformanceMonitoring? = null
        val context = imageView.context

        if (properties.data == null) {
            // if the data source is null, the image will be render the error drawable
            imageView.setImageDrawable(getDrawable(context, properties.error))
            return
        }

        GlideApp.with(context).asBitmap().apply {
            /*
            * automateScaleType mean, the image will be scaled automatically
            * based on scaleType attribute which is already defined on ImageView
            * */
            automateScaleType(imageView, this)

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

                    builder.build(
                            context = context,
                            blurHash = blurHash,
                            properties = properties,
                            performanceMonitoring = tracker,
                            request = this
                    ).load(source)
                }
                else -> {
                    builder.build(
                            context = context,
                            properties = properties,
                            request = this
                    ).load(properties.data)
                }

            }.into(imageView)
        }
    }

    // temporarily the GIF loader
    fun loadGifImage(imageView: ImageView, data: String, properties: Properties) {
        with(imageView) {
            GlideApp.with(context).asGif().load(data).apply {
                properties.cacheStrategy?.let {
                    diskCacheStrategy(mapToDiskCacheStrategy(it))
                    transform(MultiTransformation(properties.transforms as MutableList))
                }
            }.into(this)
        }
    }

}