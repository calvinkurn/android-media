package com.tokopedia.media.loader.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
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

class MediaBitmapEmptyTarget<T : Any> : CustomTarget<T> {
    private var onCleared: (placeholder: Drawable?) -> Unit = {}
    private var onReady: (resource: Bitmap) -> Unit = {}
    private var onFailed: (errorDrawable: Drawable?) -> Unit = {}
    private var onLoadStart: (placeHolder: Drawable?) -> Unit = {}

    constructor(
        onCleared: (placeholder: Drawable?) -> Unit = {},
        onReady: (resource: Bitmap) -> Unit = {},
        onFailed: (errorDrawable: Drawable?) -> Unit = {},
        onLoadStarted: (placeholder: Drawable?) -> Unit = {}
    ) : super() {
        this.onCleared = onCleared
        this.onReady = onReady
        this.onFailed = onFailed
        this.onLoadStart = onLoadStarted
    }

    constructor(
        onCleared: (placeholder: Drawable?) -> Unit = {},
        onReady: (resource: Bitmap) -> Unit = {},
        onFailed: (errorDrawable: Drawable?) -> Unit = {},
        width: Int,
        height: Int
    ) : super(width, height) {
        this.onCleared = onCleared
        this.onReady = onReady
        this.onFailed = onFailed
    }

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

    override fun onLoadStarted(placeholder: Drawable?) {
        onLoadStart(placeholder)
    }
}
