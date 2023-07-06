package com.tokopedia.media.picker.utils

import android.widget.ImageView
import com.tokopedia.media.R
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage

fun ImageView.loadPickerImage(path: String, onLoaded: () -> Unit = {}) {
    val thumbnailSize = context.resources.getDimensionPixelOffset(R.dimen.picker_thumbnail_size)
    val roundedSize = context.resources.getDimension(R.dimen.picker_thumbnail_rounded)

    loadImage(path) {
        overrideSize(Resize(thumbnailSize, thumbnailSize))
        setPlaceHolder(R.drawable.bg_picker_placeholder)
        setRoundedRadius(roundedSize)
        isAnimate(false)
        centerCrop()

        listener(onSuccess = { _, _ ->
            onLoaded()
        })
    }
}
