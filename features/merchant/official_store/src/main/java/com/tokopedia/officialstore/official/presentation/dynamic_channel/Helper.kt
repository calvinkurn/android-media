@file:JvmName("Helper")
package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.AppCompatImageView
import com.bumptech.glide.request.target.BitmapImageViewTarget

internal fun getRoundedImageViewTarget(
        imageView: AppCompatImageView,
        cornerRadius: Float
): BitmapImageViewTarget {
    return object : BitmapImageViewTarget(imageView) {
        override fun setResource(resource: Bitmap?) {
            val roundedBitmap = RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
            roundedBitmap.cornerRadius = cornerRadius

            imageView.setImageDrawable(roundedBitmap)
        }
    }
}
