@file:Suppress("KotlinConstantConditions", "USELESS_IS_CHECK")

package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.tokopedia.media.loader.MediaLoaderApi.setThumbnailUrl
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.listener.MediaListenerBuilder
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.utils.FeatureToggleManager
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.utils.MediaTarget
import com.tokopedia.media.loader.utils.transition
import com.tokopedia.media.loader.utils.mediaLoad
import com.tokopedia.media.loader.utils.timeout
import java.io.File
import java.util.concurrent.TimeUnit

object MediaLoaderTarget {

    fun <T : View> loadImage(context: Context, properties: Properties, target: MediaTarget<T>) {
        if (target is ImageView && properties.data == null) {
            // if the data source is null, the image will be render the error drawable
            target.setImageDrawable(properties.getErrorDrawable(context))
            return
        }

        loadImageTarget(context, properties)?.into(target)
    }

    fun loadImage(context: Context, properties: Properties, target: MediaBitmapEmptyTarget<Bitmap>) {
        loadImageTarget(context, properties)?.into(target)
    }

    fun loadImageFuture(context: Context, timeout: Long, properties: Properties): Bitmap? {
        return loadImageTarget(context, properties)?.submit()?.get(timeout, TimeUnit.MILLISECONDS)
    }

    fun downloadImageFuture(context: Context, timeout: Long, properties: Properties): File? {
        val size = properties.overrideSize ?: Resize(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
        return loadImageTarget(context, properties)?.downloadOnly(size.width, size.height)?.get()
    }

    fun loadGif(context: Context, properties: Properties, target: MediaBitmapEmptyTarget<GifDrawable>) {
        loadGifTarget(context, properties)?.into(target)
    }

    fun <T: Any>clear(context: Context, target: MediaBitmapEmptyTarget<T>) {
        GlideApp.with(context).clear(target)
    }

    private fun loadImageTarget(context: Context, properties: Properties): GlideRequest<Bitmap>? {
        if (properties.data.toString().isEmpty()) return null
        if (properties.data !is String) return null

        // instance the feature toggle instance
        properties.featureToggle = FeatureToggleManager.instance()

        // startTimeRequest will use for performance tracking
        val startTimeRequest = System.currentTimeMillis()

        return GlideApp
            .with(context)
            .asBitmap()
            .transform(properties)
            .transition(properties)
            .commonOptions(properties)
            .dynamicPlaceHolder(context, properties)
            .thumbnail(setThumbnailUrl(context, properties))
            .timeout(properties)
            .listener(
                MediaListenerBuilder(
                    context,
                    properties,
                    startTimeRequest
                )
            )
            .mediaLoad(properties)
    }

    private fun loadGifTarget(context: Context, properties: Properties): GlideRequest<GifDrawable>? {
        if (properties.data.toString().isEmpty()) return null
        if (properties.data !is String) return null

        // instance the feature toggle instance
        properties.featureToggle = FeatureToggleManager.instance()

        // startTimeRequest will use for performance tracking
        val startTimeRequest = System.currentTimeMillis()

        return GlideApp
            .with(context)
            .asGif()
            .commonOptions(properties)
            .dynamicPlaceHolder(context, properties)
            .listener(
                MediaListenerBuilder(
                    context,
                    properties,
                    startTimeRequest
                )
            )
            .mediaLoad(properties)
    }

}
