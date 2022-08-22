package com.tokopedia.media.loader

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.tokopedia.abstraction.R


@SuppressLint("ResourcePackage")
fun loadImageCircle (context: Context, imageView: ImageView, url: String, resIdEmpty: Int){
    if(url.isNotEmpty()){
        Glide.with(context)
            .asBitmap()
            .load(url)
            .dontAnimate()
            .placeholder(R.drawable.ic_loading_toped)
            .error(R.drawable.ic_loading_toped)
            .into(getCircleImageViewTarget(imageView))
    }
}

private fun getCircleImageViewTarget(imageView: ImageView): BitmapImageViewTarget {
    return  object : BitmapImageViewTarget(imageView){
        @SuppressLint("DeprecatedMethod")
        override fun setResource(resource: Bitmap?) {
            super.setResource(resource)
            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.context.resources,resource)
            circularBitmapDrawable.isCircular = true
            imageView.setImageDrawable(circularBitmapDrawable)
        }
    }
}
