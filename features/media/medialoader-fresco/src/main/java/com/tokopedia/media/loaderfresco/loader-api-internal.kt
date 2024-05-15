package com.tokopedia.media.loaderfresco

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import com.tokopedia.media.loaderfresco.data.ERROR_RES_UNIFY
import com.tokopedia.media.loaderfresco.data.Properties

@PublishedApi
internal fun ImageView.call(source: Any?, properties: Properties) {
    if (context.isValid()) {
        try {
            MediaLoaderFrescoApi.loadImage(this,
                properties.setSource(source)
            )
        } catch (e: Exception) {
            e.printStackTrace()

            //still render image if there is error happen
            setImageResource(ERROR_RES_UNIFY)
        }
    }
}

internal fun Context?.isValid(): Boolean {
    return when {
        this == null -> false
        this is Activity -> !(this.isDestroyed || this.isFinishing)
        else -> true
    }
}
