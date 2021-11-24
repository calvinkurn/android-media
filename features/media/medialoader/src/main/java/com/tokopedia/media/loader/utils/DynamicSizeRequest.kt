package com.tokopedia.media.loader.utils

import android.graphics.Bitmap
import android.view.ViewGroup
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target

fun Bitmap?.adaptiveSizeImageRequest(target: Target<Bitmap>?) {
    if (this == null && target == null) return

    if (target is BitmapImageViewTarget) {
        val targetImageView = target.view
        targetImageView.layout(0,0,0,0)

        val width = this?.width?: 0
        val height = this?.height?: 0

        if (width == 0 || height == 0) {
            return
        }

        if (width >= height) {
            if (targetImageView.layoutParams.width != ViewGroup.LayoutParams.MATCH_PARENT) {
                targetImageView.layoutParams.width = targetImageView.maxWidth
                if (targetImageView.maxWidth == 0 || targetImageView.maxWidth == Int.MAX_VALUE) {
                    targetImageView.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                }
            }
            targetImageView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            targetImageView.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            if (targetImageView.layoutParams.height != ViewGroup.LayoutParams.MATCH_PARENT) {
                targetImageView.layoutParams.height = targetImageView.maxHeight
                if (targetImageView.maxHeight == 0 || targetImageView.maxHeight == Int.MAX_VALUE) {
                    targetImageView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
            }
        }

        targetImageView.requestLayout()
    }
}