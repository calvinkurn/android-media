package com.tokopedia.home_component.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.analytics.performance.PerformanceMonitoring

const val TRUNCATED_URL_PREFIX = "https://ecs7.tokopedia.net/img/cache/"
const val FPM_ATTRIBUTE_IMAGE_URL = "image_url"
const val FPM_DYNAMIC_LEGO_BANNER = "home_lego_banner"

//fun ImageView.loadImage(url: String, fpmItemLabel: String = ""){
//    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
//    Glide.with(context)
//            .load(url)
//            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//            .listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//                    GlideErrorLogHelper().logError(context, e, url)
//                    return false
//                }
//
//                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                    handleOnResourceReady(dataSource, resource, performanceMonitoring)
//                    return false
//                }
//            })
//            .into(this)
//}
//
//fun getPerformanceMonitoring(url: String, fpmItemLabel: String = "") : PerformanceMonitoring? {
//    var performanceMonitoring : PerformanceMonitoring? = null
//
//    //FPM only allow max 100 chars, so the url needs to be truncated
//    val truncatedUrl = url.removePrefix(TRUNCATED_URL_PREFIX)
//
//    if (fpmItemLabel.isNotEmpty()) {
//        performanceMonitoring = PerformanceMonitoring.start(fpmItemLabel)
//        performanceMonitoring.putCustomAttribute(FPM_ATTRIBUTE_IMAGE_URL, truncatedUrl)
//    }
//    return performanceMonitoring
//}
//
//fun handleOnResourceReady(dataSource: DataSource?, resource: Drawable?, performanceMonitoring: PerformanceMonitoring?) {
//    if (dataSource == DataSource.REMOTE) {
//        performanceMonitoring?.stopTrace()
//    }
//}