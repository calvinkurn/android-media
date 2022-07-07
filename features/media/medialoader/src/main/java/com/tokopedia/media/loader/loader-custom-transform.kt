package com.tokopedia.media.loader

import android.widget.ImageView
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY

fun ImageView.loadImageTopRightCrop(source: String) {
    if (context.isValid()) {
        try {
            MediaLoaderApi.loadImage(this, source)
        } catch (e: Exception) {

            /*
            * don't let the imageView haven't image
            * render with error drawable
            * */
            this.loadImage(ERROR_RES_UNIFY)
        }
    }
}