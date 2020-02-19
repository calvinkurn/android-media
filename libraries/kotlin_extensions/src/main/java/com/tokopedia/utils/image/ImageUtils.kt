package com.tokopedia.utils.image

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.tokopedia.kotlin.extensions.R

object ImageUtils {

    fun loadImage2(imageview: ImageView, url: String?, resId: Int) {
        val error = AppCompatResources.getDrawable(imageview.context, resId)
        if (url != null && !TextUtils.isEmpty(url)) {
            Glide.with(imageview.context)
                    .load(url)
                    .placeholder(R.drawable.ic_loading_placeholder)
                    .dontAnimate()
                    .error(error)
                    .into(imageview)
        } else {
            Glide.with(imageview.context)
                    .load(url)
                    .placeholder(resId)
                    .error(error)
                    .into(imageview)
        }
    }

    fun loadImageCircle2(context: Context, imageView: ImageView,
                         url: String?) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .dontAnimate()
                    .placeholder(R.drawable.ic_loading_placeholder)
                    .error(R.drawable.ic_loading_error)
                    .into(getCircleImageViewTarget(imageView))
        }
    }

    fun loadImageRounded2(context: Context, imageview: ImageView, url: String?, radius: Float) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .dontAnimate()
                    .placeholder(R.drawable.ic_loading_placeholder)
                    .error(R.drawable.ic_loading_error)
                    .into(getRoundedImageViewTarget(imageview, radius))
        }
    }

    fun loadImageWithIdWithoutPlaceholder(imageview: ImageView, resId: Int) {
        if (imageview.context != null) {
            val drawable = AppCompatResources.getDrawable(imageview.context, resId)
            Glide.with(imageview.context)
                    .load("")
                    .placeholder(drawable)
                    .dontAnimate()
                    .error(drawable)
                    .into(imageview)
        }
    }

    fun loadImageWithoutPlaceholderAndError(imageview: ImageView, url: String) {
        if (imageview.context != null) {
            Glide.with(imageview.context)
                    .load(url)
                    .dontAnimate()
                    .into(imageview)
        }
    }

    fun clearImage(imageView: ImageView?) {
        if (imageView != null) {
            Glide.with(imageView.context).clear(imageView)
        }
    }

    private fun getCircleImageViewTarget(imageView: ImageView): BitmapImageViewTarget {
        return object : BitmapImageViewTarget(imageView) {
            override fun setResource(resource: Bitmap?) {
                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
                circularBitmapDrawable.isCircular = true
                imageView.setImageDrawable(circularBitmapDrawable)
            }
        }
    }

    private fun getRoundedImageViewTarget(imageView: ImageView, radius: Float): BitmapImageViewTarget {
        return object : BitmapImageViewTarget(imageView) {
            override fun setResource(resource: Bitmap?) {
                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
                circularBitmapDrawable.cornerRadius = radius
                imageView.setImageDrawable(circularBitmapDrawable)
            }
        }
    }
}