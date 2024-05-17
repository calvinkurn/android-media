package com.tokopedia.media.loaderfresco

import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.facebook.drawee.view.SimpleDraweeView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loaderfresco.data.Properties
import com.tokopedia.media.loaderfresco.tracker.FrescoLogger
import com.tokopedia.media.loaderfresco.utils.Hierarchy.generateHierarchy
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

            val draweeView = SimpleDraweeView(context, generateHierarchy(context, properties))
            imageView.setImageDrawable(draweeView.drawable)
            draweeView.setImageURI(properties.generateFrescoUrl())

            FrescoLogger.loggerSlardarFresco()
        } else {
            imageView.loadImage(properties.generateFrescoUrl()){
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
