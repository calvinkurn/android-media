package com.tokopedia.home.beranda.helper

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.BitmapTransitionFactory
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.tokopedia.abstraction.R

object HomeImageHandler {
    private val factoryDrawable: DrawableCrossFadeFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    fun loadImage(context: Context, imageView: ImageView, url: String){

        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(factoryDrawable))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView)
    }

    fun loadImageFitCenter(context: Context, imageView: ImageView, url: String){
        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(factoryDrawable))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView)
    }

    fun loadImageWithOutPlaceHolder(context: Context, imageView: ImageView, url: String){
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(DrawableTransitionOptions.withCrossFade(factoryDrawable))
                .into(imageView)
    }

    fun loadImageWithOutAnimation(context: Context, imageView: ImageView, url: String){
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .error(R.drawable.error_drawable)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView)
    }

    fun loadImageRounded(context: Context, imageView: ImageView, url: String, radius: Float){
        Glide.with(context)
                .asBitmap()
                .load(url)
//                .transform(RoundedImageTransformation(context, radius.toInt()))
                .transition(BitmapTransitionOptions.withCrossFade(factoryDrawable))
                .into(getRoundedImageViewTarget(imageView, radius))
    }

    fun loadImageRoundedCenterCrop(context: Context, imageView: ImageView, url: String, radius: Float){
        Glide.with(context)
                .asBitmap()
                .load(url)
                .centerCrop()
                .transition(BitmapTransitionOptions.withCrossFade(factoryDrawable))
                .into(getRoundedImageViewTarget(imageView, radius))
    }

    fun loadGif(context: Context, imageView: ImageView, url: String, radius: Float){
        Glide.with(context)
                .asGif()
                .load(url)
                .transform(RoundedImageTransformation(context, radius.toInt()))
                .error(R.drawable.error_drawable)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView)
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