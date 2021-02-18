package com.tokopedia.media.loader.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

class MediaTarget<T : View>(
        view: T,
        private val onFailed: (errorDrawable: Drawable?) -> Unit = {},
        private val onCleared: (placeholder: Drawable?) -> Unit = {},
        private val onReady: (resource: Bitmap) -> Unit = {}
) : CustomViewTarget<T, Bitmap>(view) {

    override fun onLoadFailed(errorDrawable: Drawable?) {
        onFailed(errorDrawable)
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        onCleared(placeholder)
    }

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        onReady(resource)
    }

}