package com.tokopedia.media.loaderfresco

import android.content.Context
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import com.tokopedia.media.loaderfresco.data.Properties
import com.tokopedia.media.loaderfresco.tracker.FrescoLogger
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


        val roundingParams = RoundingParams.fromCornersRadius(properties.roundedRadius)

        val builder = GenericDraweeHierarchyBuilder(context.resources)
        val hierarchy: GenericDraweeHierarchy = builder.setRoundingParams(roundingParams).build()

        val draweeView = SimpleDraweeView(context, hierarchy)
        imageView.setImageDrawable(draweeView.drawable)

        draweeView.setImageURI(properties.data.toString())

        FrescoLogger.loggerSlardarFresco()
    }

    //TODO ADD Enabler Rollence
    private fun enableFrescoLoader(context: Context): Boolean {
        val key = "android_enable_image_fresco"
        return FirebaseRemoteConfigImpl(context.applicationContext)
            .getBoolean(key, false)
    }
}
