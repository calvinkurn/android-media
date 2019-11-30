package com.tokopedia.home.beranda.helper.glide


import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.RoundedImageTransformation

fun ImageView.loadImage(url: String){
    Glide.with(context)
            .load(url)
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.loading_page)
            .thumbnail(0.1f)
            .into(this)
}

fun ImageView.loadImageFitCenter(url: String){
    Glide.with(context)
            .load(url)
            .fitCenter()
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.loading_page)
            .thumbnail(0.1f)
            .into(this)
}

fun ImageView.loadImageRounded(url: String){
    Glide.with(context)
            .load(url)
            .transform(RoundedImageTransformation(context, 10))
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.loading_page)
            .thumbnail(0.1f)
            .into(this)
}


fun ImageView.loadImageRounded(url: String, width: Int, height: Int){
    Glide.with(context)
            .load(url)
            .fitCenter()
            .override(width, height)
            .transform(RoundedImageTransformation(context, 10))
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.loading_page)
            .thumbnail(0.1f)
            .into(this)
}

fun ImageView.loadMiniImage(url: String){
    Glide.with(context)
            .load(url)
            .fitCenter()
            .override(100)
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.loading_page)
            .thumbnail(0.1f)
            .into(this)
}

fun ImageView.loadMiniImage(url: String, width: Int, height: Int){
    Glide.with(context)
            .load(url)
            .fitCenter()
            .override(width, height)
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.loading_page)
            .thumbnail(0.1f)
            .into(this)
}

fun ImageView.loadGif(url: String){
    Glide.with(context)
            .asGif()
            .load(url)
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .thumbnail(0.1f)
            .transform(RoundedImageTransformation(context, 10))
            .into(this)
}