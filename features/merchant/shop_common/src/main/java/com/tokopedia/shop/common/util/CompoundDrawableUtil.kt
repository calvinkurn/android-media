package com.tokopedia.shop.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

fun convertUrlToBitmapAndLoadImage(
        context: Context,
        url: String?,
        convertIntoSize: Int,
        loadImage: (resource: Bitmap) -> Unit
) {
    Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(convertIntoSize, convertIntoSize) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    loadImage(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
}

fun UnifyButton.loadLeftDrawable(context: Context, url: String?, convertIntoSize: Int) {
    convertUrlToBitmapAndLoadImage(
            context,
            url,
            convertIntoSize
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
                BitmapDrawable(resources, it),
                null,
                null,
                null
        )
    }
}

fun Typography.loadLeftDrawable(context: Context, url: String?, convertIntoSize: Int) {
    convertUrlToBitmapAndLoadImage(
            context,
            url,
            convertIntoSize
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
                BitmapDrawable(resources, it),
                null,
                null,
                null
        )
    }
}

fun UnifyButton.removeDrawable() {
    setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            null,
            null
    )
}

fun Typography.removeDrawable() {
    setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            null,
            null
    )
}