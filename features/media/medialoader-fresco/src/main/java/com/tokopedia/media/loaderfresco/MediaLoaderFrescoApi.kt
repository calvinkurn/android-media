package com.tokopedia.media.loaderfresco

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.tokopedia.media.loaderfresco.tracker.FrescoLogger
import com.tokopedia.media.loaderfresco.utils.FrescoDataSourceRequest
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

internal object MediaLoaderFrescoApi {
    fun loadImage(imageView: ImageView, url: String?) {
        //TODO usage Properties
        val context = imageView.context
        val dataSource = FrescoDataSourceRequest.frescoDataSourceBuilder(url, context)

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

    private fun enableFrescoLoader(context: Context): Boolean {
        val key = "android_enable_image_fresco"
        return FirebaseRemoteConfigImpl(context.applicationContext)
            .getBoolean(key, false)
    }
}
