package com.tokopedia.media.loader

import android.widget.ImageView
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.data.PLACEHOLDER_RES_UNIFY
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.transform.TopRightCrop
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy

fun ImageView.loadImageTopRightCrop(source: String) {
    if (context.isValid()) {
        try {
            call(
                source,
                Properties()
                    .transform(TopRightCrop())
                    .setCacheStrategy(MediaCacheStrategy.RESOURCE)
                    .setPlaceHolder(PLACEHOLDER_RES_UNIFY)
                    .setErrorDrawable(ERROR_RES_UNIFY)
            )
        } catch (e: Exception) {

            /*
            * don't let the imageView haven't image
            * render with error drawable
            * */
            this.loadImage(ERROR_RES_UNIFY)
        }
    }
}
