package com.tokopedia.media.loader.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

class MediaTarget<T : View>(
        private val viewComponent: T,
        private val onFailed: (viewComponent: T, errorDrawable: Drawable?) -> Unit = { _, _ -> },
        private val onCleared: (placeholder: Drawable?) -> Unit = {},
        private val onReady: (viewComponent: T, resource: Bitmap) -> Unit = { _, _ -> }
) : CustomViewTarget<T, Bitmap>(viewComponent) {

    override fun onLoadFailed(errorDrawable: Drawable?) {
        onFailed(viewComponent, errorDrawable)
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        onCleared(placeholder)
    }

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        onReady(viewComponent, resource)
    }

}

class MediaBitmapEmptyTarget<T : Any>(
    private val onCleared: (placeholder: Drawable?) -> Unit = {},
    private val onReady: (resource: Bitmap) -> Unit = {},
    private val onFailed: (errorDrawable: Drawable?) -> Unit = {},
    width: Int = SIZE_ORIGINAL,
    height: Int = SIZE_ORIGINAL
) : CustomTarget<T>(width, height) {
    override fun onLoadCleared(placeholder: Drawable?) {
        onCleared(placeholder)
    }

    override fun onResourceReady(resource: T, transition: Transition<in T>?) {
        if (resource is Bitmap) {
            onReady(resource)
        }
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        onFailed(errorDrawable)
    }
}
