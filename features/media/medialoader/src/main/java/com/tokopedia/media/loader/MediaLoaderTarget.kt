package com.tokopedia.media.loader

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.request.target.Target
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.Loader
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.common.factory.BitmapFactory
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.tracker.PerformanceTracker
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.utils.MediaTarget

object MediaLoaderTarget {

    private val bitmap by lazy { BitmapFactory() }
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    fun <T: View> loadImage(context: Context, properties: Properties, target: MediaTarget<T>) {
        if (target is ImageView && properties.data == null) {
            // if the data source is null, the image will be render the error drawable
            target.setImageDrawable(AppCompatResources.getDrawable(context, properties.error))
            return
        }

        loadImageTarget(context, properties, target)
    }

    fun loadImage(context: Context, properties: Properties, target: MediaBitmapEmptyTarget) {
        loadImageTarget(context, properties, target)
    }

    private fun <T> loadImageTarget(context: Context, properties: Properties, target: Target<T>) {
        var tracker: PerformanceMonitoring?

        // handling empty url
        if (properties.data.toString().isEmpty()) return

        if (properties.data is String) {
            GlideApp.with(context).asBitmap().also {
                // url builder
                val source = Loader.urlBuilder(properties.data.toString())

                tracker = PerformanceTracker.preRender(source, context)

                val request = bitmap.build(
                    context = context,
                    properties = properties,
                    performanceMonitoring = tracker,
                    request = it
                ).load(source)

                if (target is MediaTarget<*>) {
                    if (properties.renderDelay <= 0L) {
                        request.into(target)
                    } else {
                        handler.postDelayed({
                            request.into(target)
                        }, properties.renderDelay)
                    }
                }
            }
        }
    }

}