package com.tokopedia.utils.image

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import android.widget.ImageView.ScaleType.*
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.kotlin.extensions.R

object ImageUtils {

    fun loadImage(imageView: ImageView, url: String, resPlaceholder: Int) {
        if (imageView.context != null) {
            Glide.with(imageView.context)
                    .load(url)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            imageView.setImageDrawable(resource)
                        }

                        override fun onLoadStarted(placeholder: Drawable?) {
                            imageView.setImageResource(resPlaceholder)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            imageView.setImageResource(resPlaceholder)
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            imageView.setImageResource(resPlaceholder)
                        }
                    })
        }
    }

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

    fun loadImageCircleWithPlaceHolder(context: Context?, imageView: ImageView,
                                       url: String?) {
        context?.let {
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
        val context = imageview.context
        if (context != null) {
            if (context is Activity && (context.isFinishing)) {
                return
            }
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

    @JvmStatic
    fun loadImageWithSignature(imageView: ImageView, url: String, signature: ObjectKey, imageLoaded: (Boolean) -> Unit) {
        Glide.with(imageView.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .dontAnimate()
                .signature(signature)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        imageView.setImageDrawable(resource)
                        imageLoaded.invoke(true)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        imageView.setImageDrawable(null)
                        imageLoaded.invoke(false)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        imageLoaded.invoke(false)
                    }
                })
    }

    @JvmOverloads
    fun loadImage(imageView: ImageView, url: String,
                  radius: Float = 0f,
                  signatureKey: Any = Object(),
                  @DrawableRes placeHolder: Int = 0,
                  @DrawableRes resOnError: Int = 0,
                  isAnimate: Boolean = false,
                  imageLoaded: (Boolean) -> Unit = {},
                  imageCleared: (Boolean) -> Unit = {}) {

        val drawableError: Drawable? = if (resOnError == 0) {
            AppCompatResources.getDrawable(imageView.context, R.drawable.ic_loading_error)
        } else {
            AppCompatResources.getDrawable(imageView.context, resOnError)
        }

        if (url.isEmpty()) {
            setImage(imageView, radius, drawableError)
            imageLoaded.invoke(false)
            imageCleared.invoke(false)
        } else {
            Glide.with(imageView).load(url).apply {
                signature(ObjectKey(signatureKey))
                diskCacheStrategy(DiskCacheStrategy.DATA)
                drawableError?.let { error(it) }

                if (placeHolder != 0) {
                    placeholder(placeHolder)
                }

                if (!isAnimate) {
                    dontAnimate()
                }

                when (imageView.scaleType) {
                    FIT_CENTER -> fitCenter()
                    CENTER_CROP -> centerCrop()
                    CENTER_INSIDE -> centerInside()
                    else -> {
                    }
                }

                into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        setImage(imageView, radius, resource)
                        imageLoaded.invoke(true)
                        imageCleared.invoke(false)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        setImage(imageView, radius, placeholderDrawable)
                        imageLoaded.invoke(false)
                        imageCleared.invoke(true)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        setImage(imageView, radius, errorDrawable)
                        imageLoaded.invoke(false)
                        imageCleared.invoke(false)
                    }
                })
            }
        }
    }

    private fun setImage(imageView: ImageView, radius: Float, drawable: Drawable?) {
        if (radius > 0f) {
            getRoundedImageViewTarget(imageView, radius)
        } else {
            imageView.setImageDrawable(drawable)
        }
    }

    interface ImageLoaderStateListener {
        fun successLoad()
        fun failedLoad()
    }
}