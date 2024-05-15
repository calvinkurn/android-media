package com.tokopedia.media.loaderfresco

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.tokopedia.media.loaderfresco.data.Properties
import com.tokopedia.media.loaderfresco.tracker.FrescoLogger
import com.tokopedia.media.loaderfresco.utils.FrescoDataSourceRequest
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

internal object MediaLoaderFrescoApi {
    fun loadImage(imageView: ImageView, properties: Properties) {
        val source = properties.data
        val context = imageView.context

        if (properties.data is String && source.toString().isEmpty()) {
            imageView.setImageDrawable(AppCompatResources.getDrawable(context, properties.error))
            return
        }

        if (properties.data == null) {
            imageView.setImageDrawable(AppCompatResources.getDrawable(context, properties.error))
            return
        }

        val dataSource = FrescoDataSourceRequest.frescoDataSourceBuilder(properties, context)
        dataSource.subscribe(object : BaseBitmapDataSubscriber() {
            override fun onNewResultImpl(bitmap: Bitmap?) {
                if (bitmap != null && !bitmap.isRecycled) {
                    imageView.setImageBitmap(bitmap)
                }
            }

            override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
            }
        }) { command ->
            command.run()
        }

        FrescoLogger.loggerSlardarFresco()
    }

    //TODO ADD Enabler
    private fun enableFrescoLoader(context: Context): Boolean {
        val key = "android_enable_image_fresco"
        return FirebaseRemoteConfigImpl(context.applicationContext)
            .getBoolean(key, false)
    }
}
