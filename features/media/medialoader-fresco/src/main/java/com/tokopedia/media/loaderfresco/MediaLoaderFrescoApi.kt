package com.tokopedia.media.loaderfresco

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.bytedance.apm6.util.Tools.runOnUiThread
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.BaseDataSubscriber
import com.facebook.datasource.DataSource
import com.facebook.imagepipeline.image.CloseableBitmap
import com.facebook.imagepipeline.image.CloseableImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loaderfresco.data.Properties
import com.tokopedia.media.loaderfresco.tracker.FrescoLogger
import com.tokopedia.media.loaderfresco.utils.FrescoDataSourceRequest
import com.tokopedia.media.loaderfresco.utils.RoundingImage.roundingImage
import com.tokopedia.media.loaderfresco.utils.generateFrescoUrl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

internal object MediaLoaderFrescoApi {
    fun loadImage(imageView: ImageView, properties: Properties) {
        if (enableFrescoLoader()) {
            val source = properties.data
            val context = imageView.context

            if (properties.data is String && source.toString().isEmpty()) {
                imageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        properties.error
                    )
                )
                return
            }

            if (properties.data == null) {
                imageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        properties.error
                    )
                )
                return
            }

            val dataSource = FrescoDataSourceRequest.frescoDataSourceBuilder(properties, context)
            dataSource.subscribe(object : BaseDataSubscriber<CloseableReference<CloseableImage>>() {
                override fun onNewResultImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
                    val imageReference = dataSource?.result
                    imageReference?.let {
                        var bitmap: Bitmap? = null
                        try {
                            val closeableImage = it.get()
                            if (closeableImage is CloseableBitmap) {
                                bitmap = closeableImage.underlyingBitmap.copy(Bitmap.Config.ARGB_8888, true)
                                if (!bitmap.isRecycled) {
                                    runOnUiThread{
                                        val processedImage = bitmap.roundingImage(imageView.context, properties)
                                        imageView.setImageDrawable(processedImage)
                                    }
                                }
                            }
                        } finally {
                            CloseableReference.closeSafely(imageReference)
                            dataSource.close()
                        }
                    } ?: dataSource?.close()
                }

                override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
                    dataSource?.close()
                    imageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            properties.error
                        ))
                }
            }) { command ->
                command.run()
            }
        } else {
            imageView.loadImage(properties.generateFrescoUrl()) {
                setRoundedRadius(properties.roundedRadius)
                setErrorDrawable(properties.error)
                setPlaceHolder(properties.placeHolder)
            }
        }
    }

    private fun enableFrescoLoader(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.FRESCO_IMAGE,
            ""
        ) == RollenceKey.FRESCO_IMAGE
    }
}
