package com.tokopedia.media.loader

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.Loader
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.common.factory.BitmapFactory
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.tracker.PerformanceTracker
import com.tokopedia.media.loader.utils.MediaTarget

object MediaLoaderTarget {

    private val bitmap by lazy { BitmapFactory() }
    private val handler by lazy { Handler() }

    fun <T: View> loadImage(context: Context, properties: Properties, target: MediaTarget<T>) {
        var tracker: PerformanceMonitoring? = null

        // handling empty url
        if (properties.data.toString().isEmpty()) return

        if (target is ImageView && properties.data == null) {
            // if the data source is null, the image will be render the error drawable
            target.setImageDrawable(AppCompatResources.getDrawable(context, properties.error))
            return
        }

        if (properties.data is String) {
            GlideApp.with(context).asBitmap().also {
                // url builder
                val source = Loader.urlBuilder(properties.data.toString())

                /*
                * only track the performance monitoring for a new domain,
                * which is already using CDN services, 'images.tokopedia.net'.
                * */
                if (properties.isTrackable) {
                    tracker = PerformanceTracker.preRender(source, context)
                }

                val request = bitmap.build(
                        context = context,
                        properties = properties,
                        performanceMonitoring = tracker,
                        request = it
                ).load(source)

                // delay handler
                handler.postDelayed({
                    request.into(target)
                }, properties.renderDelay)
            }
        }
    }

}