package com.tokopedia.tokopedianow.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

object ImageUtil {
    fun setBackgroundImageFromUrl(context: Context, url: String, view: View?) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val drawable = BitmapDrawable(context.resources, resource)
                    view?.background = drawable
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}