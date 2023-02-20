@file:Suppress("KotlinConstantConditions", "USELESS_IS_CHECK")
package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.media.loader.MediaLoaderApi.setThumbnailUrl
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.options.CommonOptions
import com.tokopedia.media.loader.options.PlaceholderOptions
import com.tokopedia.media.loader.options.TransformationOptions
import com.tokopedia.media.loader.listener.MediaListenerBuilder
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.utils.MediaTarget
import com.tokopedia.media.loader.utils.generateUrl

object MediaLoaderTarget {

    fun <T : View> loadImage(context: Context, properties: Properties, target: MediaTarget<T>) {
        if (target is ImageView && properties.data == null) {
            // if the data source is null, the image will be render the error drawable
            target.setImageDrawable(AppCompatResources.getDrawable(context, properties.error))
            return
        }

        loadImageTarget(context, properties)?.into(target)
    }

    fun loadImage(context: Context, properties: Properties, target: MediaBitmapEmptyTarget<Bitmap>) {
        loadImageTarget(context, properties)?.into(target)
    }

    private fun loadImageTarget(context: Context, properties: Properties): GlideRequest<Bitmap>? {
        if (properties.data.toString().isEmpty()) return null
        if (properties.data !is String) return null

        // startTimeRequest will use for performance tracking
        val startTimeRequest = System.currentTimeMillis()

        GlideApp
            .with(context)
            .asBitmap()
            .apply(CommonOptions.build(properties))
            .apply(TransformationOptions.build(properties))
            .apply(PlaceholderOptions.build(context, properties))
            .apply {
                // set custom thumbnail
                setThumbnailUrl(context, properties)

                // callback listener
                listener(
                    MediaListenerBuilder.callback(
                        context,
                        properties,
                        startTimeRequest
                    )
                )

                return if (properties.data is String) {
                    load(properties.generateUrl())
                } else {
                    load(properties.data)
                }
            }
    }

}
