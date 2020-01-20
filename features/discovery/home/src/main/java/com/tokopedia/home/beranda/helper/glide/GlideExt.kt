package com.tokopedia.home.beranda.helper.glide


import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home.R

val FPM_HOME_IMAGE_PREFIX = "fpm_home_image"
val FPM_ATTRIBUTE_IMAGE_URL = "image_url"
val FPM_PRODUCT_ORGANIC_CHANNEL = "product_organic_channel"


fun ImageView.loadImage(url: String, fpmItemLabel: String = ""){
    var performanceMonitoring : PerformanceMonitoring? = null

    if (!fpmItemLabel.isEmpty()) {
        performanceMonitoring = PerformanceMonitoring.start(FPM_HOME_IMAGE_PREFIX + fpmItemLabel)
        performanceMonitoring.putCustomAttribute(FPM_ATTRIBUTE_IMAGE_URL, url)
    }

    Glide.with(context)
            .load(url)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .placeholder(R.drawable.loading_page)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    GlideErrorLogHelper().logError(context, e, url)
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    if (dataSource == DataSource.REMOTE) {
                        performanceMonitoring?.stopTrace()
                    }
                    return false
                }
            })
            .into(this)
}

fun ImageView.loadImageFitCenter(url: String){
    Glide.with(context)
            .load(url)
            .fitCenter()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .skipMemoryCache(true)
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .placeholder(R.drawable.loading_page)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    GlideErrorLogHelper().logError(context, e, url)
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    return false
                }
            })
            .into(this)
}


fun ImageView.loadImageCrop(url: String){
    Glide.with(context)
            .load(url)
            .centerCrop()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .placeholder(R.drawable.loading_page)
            .into(this)
}

fun ImageView.loadImageRounded(url: String){
    Glide.with(context)
            .load(url)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .transform(RoundedCorners(10))
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .placeholder(R.drawable.loading_page)
            .into(this)
}

fun ImageView.loadImageRounded(url: String, width: Int, height: Int){
    Glide.with(context)
            .load(url)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .override(width, height)
            .transform(RoundedCorners( 10))
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .placeholder(R.drawable.loading_page)
            .into(this)
}

fun ImageView.loadMiniImage(url: String){
    Glide.with(context)
            .load(url)
            .fitCenter()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .override(100)
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .placeholder(R.drawable.loading_page)
            .into(this)
}

fun ImageView.loadMiniImage(url: String, width: Int, height: Int){
    Glide.with(context)
            .load(url)
            .fitCenter()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .override(width, height)
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .placeholder(R.drawable.loading_page)
            .into(this)
}

fun ImageView.loadImageCenterCrop(url: String){
    Glide.with(context)
            .load(url)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .transform(CenterCrop(), RoundedCorners(15))
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .placeholder(R.drawable.loading_page)
            .into(this)
}

fun ImageView.loadGif(url: String){
    Glide.with(context)
            .asGif()
            .load(url)
            .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
            .transform(RoundedCorners(10))
            .into(this)
}