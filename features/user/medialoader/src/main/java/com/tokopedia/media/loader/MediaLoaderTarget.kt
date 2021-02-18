package com.tokopedia.media.loader

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.Loader
import com.tokopedia.media.common.data.CDN_IMAGE_URL
import com.tokopedia.media.common.data.PARAM_BLURHASH
import com.tokopedia.media.common.data.toUri
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.common.builder.GlideBitmapBuilder
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.tracker.PerformanceTracker
import com.tokopedia.media.loader.utils.MediaTarget

object MediaLoaderTarget {

    private val bitmapBuilder by lazy { GlideBitmapBuilder() }
    private val handler by lazy { Handler() }

    fun <T: View> loadImage(context: Context, properties: Properties, target: MediaTarget<T>) {
        var tracker: PerformanceMonitoring? = null

        // handling empty url
        if (properties.data is String && properties.data.toString().isEmpty()) return

        if (target is ImageView && properties.data == null) {
            // if the data source is null, the image will be render the error drawable
            target.setImageDrawable(AppCompatResources.getDrawable(context, properties.error))
            return
        }

        GlideApp.with(context).asBitmap().apply {
            val request = when(properties.data) {
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
            }

            handler.postDelayed({
                request.into(target)
            }, properties.renderDelay)
        }
    }

}