package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.media.common.Loader
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.common.factory.BitmapFactory
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.utils.MediaTarget

object MediaLoaderTarget {

    private val bitmap by lazy { BitmapFactory() }

    fun <T: View> loadImage(context: Context, properties: Properties, target: MediaTarget<T>) {
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

        GlideApp.with(context).asBitmap().also {
            // url builder
            val source = Loader.urlBuilder(properties.data.toString())

            return bitmap.build(
                context = context,
                properties = properties,
                request = it
            ).load(source)
        }
    }

}