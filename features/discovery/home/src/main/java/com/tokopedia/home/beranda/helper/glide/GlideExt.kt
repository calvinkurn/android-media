package com.tokopedia.home.beranda.helper.glide


import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home.R

const val FPM_ATTRIBUTE_IMAGE_URL = "image_url"
const val FPM_PRODUCT_ORGANIC_CHANNEL = "home_product_organic"
const val FPM_THEMATIC_CARD_VIEW = "home_thematic_card"
const val FPM_DYNAMIC_LEGO_BANNER = "home_lego_banner"
const val FPM_USE_CASE_ICON = "home_use_case_icon"
const val TRUNCATED_URL_PREFIX = "https://ecs7.tokopedia.net/img/cache/"


fun ImageView.loadImage(url: String, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(context, url, fpmItemLabel)
    Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.loading_page)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    GlideErrorLogHelper().logError(context, e, url)
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    handleOnResourceReady(dataSource, resource, performanceMonitoring)
                    return false
                }
            })
            .into(this)
}

fun ImageView.loadImageFitCenter(url: String, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(context, url, fpmItemLabel)
    Glide.with(context)
            .load(url)
            .fitCenter()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.loading_page)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    GlideErrorLogHelper().logError(context, e, url)
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    handleOnResourceReady(dataSource, resource, performanceMonitoring)
                    return false
                }
            })
            .into(this)
}

fun ImageView.loadImageRounded(url: String, roundedRadius: Int, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(context, url, fpmItemLabel)
    Glide.with(context)
            .load(url)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(RoundedCorners(roundedRadius))
            .placeholder(R.drawable.loading_page)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    handleOnResourceReady(dataSource, resource, performanceMonitoring)
                    return false
                }
            })
            .into(this)
}

fun ImageView.loadMiniImage(url: String, width: Int, height: Int, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(context, url, fpmItemLabel)
    Glide.with(context)
            .load(url)
            .fitCenter()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .override(width, height)
            .placeholder(R.drawable.loading_page)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    handleOnResourceReady(dataSource, resource, performanceMonitoring)
                    return false
                }
            })
            .into(this)
}

fun ImageView.loadImageCenterCrop(url: String){
    Glide.with(context)
            .load(url)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(CenterCrop(), RoundedCorners(15))
            .placeholder(R.drawable.loading_page)
            .into(this)
}

fun ImageView.loadImage(url: String, width: Int, height: Int, skipMemoryCache: Boolean = false, placeholder: Int = -1){
    Glide.with(context)
            .load(url)
            .override(width, height)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(CenterCrop(), RoundedCorners(15))
            .placeholder(placeholder)
            .into(this)
}

fun ImageView.loadImageNoRounded(url: String, placeholder: Int = -1){
    Glide.with(context)
            .load(url)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(CenterCrop())
            .placeholder(placeholder)
            .into(this)
}

fun ImageView.loadGif(url: String){
    Glide.with(context)
            .asGif()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(RoundedCorners(10))
            .into(this)
}

fun getPerformanceMonitoring(context: Context, url: String, fpmItemLabel: String = "") : PerformanceMonitoring? {
    var performanceMonitoring : PerformanceMonitoring? = null

    //FPM only allow max 100 chars, so the url needs to be truncated
    val truncatedUrl = url.removePrefix(TRUNCATED_URL_PREFIX)

    if (fpmItemLabel.isNotEmpty()) {
        performanceMonitoring = PerformanceMonitoring.start(context, fpmItemLabel)
        performanceMonitoring.putCustomAttribute(FPM_ATTRIBUTE_IMAGE_URL, truncatedUrl)
    }
    return performanceMonitoring
}

fun handleOnResourceReady(dataSource: DataSource?, resource: Drawable?, performanceMonitoring: PerformanceMonitoring?) {
    if (dataSource == DataSource.REMOTE) {
        performanceMonitoring?.stopTrace()
    }
}