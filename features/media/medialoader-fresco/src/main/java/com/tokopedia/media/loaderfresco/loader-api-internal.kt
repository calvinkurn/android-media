package com.tokopedia.media.loaderfresco

import android.app.Activity
import android.content.Context
import android.widget.ImageView

internal fun ImageView.call(url: String?) {
    if (context.isValid()) {
        try {
            MediaLoaderFrescoApi.loadImage(this, url)
        } catch (e: Exception) {
            e.printStackTrace()

            //TODO Error Image
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
