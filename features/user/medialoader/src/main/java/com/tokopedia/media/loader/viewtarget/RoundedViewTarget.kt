package com.tokopedia.media.loader.viewtarget

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create
import com.bumptech.glide.request.target.BitmapImageViewTarget

class RoundedViewTarget(
        private val imageView: ImageView,
        private val radius: Float
): BitmapImageViewTarget(imageView) {

    override fun setResource(resource: Bitmap?) {
        val bitmap = create(imageView.context.resources, resource)
        bitmap.cornerRadius = radius
        imageView.setImageDrawable(bitmap)
    }

}