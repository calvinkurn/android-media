package com.tokopedia.media.loader

import android.graphics.Bitmap
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.Loader
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.common.factory.BitmapFactory
import com.tokopedia.media.loader.common.factory.GifFactory
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.data.PLACEHOLDER_RES_UNIFY
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.tracker.PerformanceTracker
import com.tokopedia.media.loader.transform.TopRightCrop
import com.tokopedia.media.loader.wrapper.MediaDataSource

internal object MediaLoaderApi {

    private val handler by lazy(LazyThreadSafetyMode.NONE) { Handler() }

    private val bitmap by lazy { BitmapFactory() }
    private val gif by lazy { GifFactory() }

    @JvmOverloads
    fun loadImage(imageView: ImageView, properties: Properties) {
        var tracker: PerformanceMonitoring? = null
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
                    val source = Loader.urlBuilder(properties.data.toString())

                    /*
                    * only track the performance monitoring for a new domain,
                    * which is already using CDN services, 'images.tokopedia.net'.
                    * */
                    if (properties.isTrackable) {
                        tracker = PerformanceTracker.preRender(source, context)
                    }

                    // get the imageView size
                    properties.setImageSize(
                            width = imageView.measuredWidth,
                            height = imageView.measuredHeight
                    )

                    bitmap.build(
                            context = context,
                            performanceMonitoring = tracker,
                            properties = properties.setUrlHasQuality(source),
                            request = this
                    ).load(source)
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

    // for rounded (temp solution for category widget)
    fun loadImageRounded(
            imageView: ImageView,
            source: String?,
            radius: Int,
            onSuccess: (Bitmap?, MediaDataSource?) -> Unit
    ) {
        if (source != null && source.isNotEmpty()) {
            GlideApp.with(imageView.context)
                    .asBitmap()
                    .centerCrop()
                    .load(source)
                    .transform(RoundedCorners(radius))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(PLACEHOLDER_RES_UNIFY)
                    .error(ERROR_RES_UNIFY)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                isFirstResource: Boolean
                        ): Boolean {
                            return true
                        }

                        override fun onResourceReady(
                                resource: Bitmap?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                        ): Boolean {
                            onSuccess(resource, MediaDataSource.mapTo(dataSource))
                            return true
                        }

                    })
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