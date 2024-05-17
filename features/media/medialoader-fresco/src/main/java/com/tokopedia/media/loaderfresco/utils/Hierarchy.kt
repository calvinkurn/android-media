package com.tokopedia.media.loaderfresco.utils

import android.content.Context
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.tokopedia.media.loaderfresco.data.Properties

object Hierarchy {

    internal fun generateHierarchy(context: Context, properties: Properties): GenericDraweeHierarchy {
        val roundingParams = RoundingParams.fromCornersRadius(properties.roundedRadius)
        val builder = GenericDraweeHierarchyBuilder(context.resources)
        return builder.setRoundingParams(roundingParams)
            .build()
    }
}
