package com.tokopedia.media.loader

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.media.common.Loader
import com.tokopedia.media.common.data.PARAM_BLURHASH
import com.tokopedia.media.common.data.toUri
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.common.PropertiesBuilder
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.tracker.PerformanceTracker.track as performanceTracker

object GlideBuilder {

    private val propertiesBuilder by lazy { PropertiesBuilder() }

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
    fun loadImage(data: Any?, imageView: ImageView, properties: Properties) {
        val context = imageView.context

        if (data == null) {
            imageView.setImageDrawable(getDrawable(context, properties.error))
        }

        GlideApp.with(context).asBitmap().load(data).apply {
            automateScaleType(imageView, this)

            when (data) {
                is String -> {
                    val source = Loader.urlBuilder(data)
                    val hash = data.toUri()?.getQueryParameter(PARAM_BLURHASH)
                    val performanceMonitoring = performanceTracker(source, context)

                    propertiesBuilder.build(
                            context = context,
                            blurHash = hash,
                            properties = properties,
                            performanceMonitoring = performanceMonitoring,
                            request = this
                    )
                }
                else -> propertiesBuilder.build(
                        context = context,
                        properties = properties,
                        request = this
                )
            }.into(imageView)
        }
    }

    fun loadGifImage(imageView: ImageView, data: String) {
        with(imageView) {
            GlideApp.with(context)
                    .asGif()
                    .load(data)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transform(RoundedCorners(10))
                    .into(this)
        }
    }

}