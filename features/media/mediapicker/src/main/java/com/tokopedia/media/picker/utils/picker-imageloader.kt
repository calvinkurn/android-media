package com.tokopedia.media.picker.utils

import android.widget.ImageView
import com.tokopedia.media.R
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File

fun ImageView.loadPickerImage(path: String, onLoaded: () -> Unit = {}) {
    val thumbnailSize = context.resources.getDimensionPixelOffset(R.dimen.picker_thumbnail_size)
    val roundedSize = context.resources.getDimension(R.dimen.picker_thumbnail_rounded)

    val isFitCenter = File(path).let {
        if (it.exists()) {
            ImageProcessingUtil.shouldLoadFitCenter(it)
        } else {
            false
        }
    }

    loadImage(path) {
        overrideSize(Resize(thumbnailSize, thumbnailSize))
        setRoundedRadius(roundedSize)
        isAnimate(true)
        setPlaceHolder(-1)

        if (isFitCenter) {
            fitCenter()
        } else {
            centerCrop()
        }

        listener(onSuccess = { _, _ ->
            onLoaded()
        })
    }
}
