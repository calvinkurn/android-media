package com.tokopedia.home_page_banner.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tokopedia.home_page_banner.R


fun ImageView.loadImage(url: String){
    Glide.with(context)
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .placeholder(R.drawable.bannerview_image_broken)
            .into(this)
}