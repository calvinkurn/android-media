package com.tokopedia.home.beranda.helper.glide

import android.graphics.Bitmap
import android.widget.ImageView
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home.R
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.listener.MediaListener
import com.tokopedia.media.loader.loadAsGif
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.media.loader.wrapper.MediaDataSource

const val FPM_ATTRIBUTE_IMAGE_URL = "image_url"
const val FPM_PRODUCT_ORGANIC_CHANNEL = "home_product_organic"
const val FPM_THEMATIC_CARD_VIEW = "home_thematic_card"
const val FPM_DYNAMIC_LEGO_BANNER = "home_lego_banner"
const val FPM_CATEGORY_WIDGET_ITEM = "home_category_widget_item"
const val FPM_USE_CASE_ICON = "home_use_case_icon"
const val FPM_DEALS_WIDGET_PRODUCT_IMAGE = "home_deals_widget_product_image"
const val FPM_SEE_ALL_CARD_BACKGROUND = "home_see_all_card_background_image"
const val FPM_RECOMMENDATION_LIST_CAROUSEL = "home_recommendation_list_carousel"
const val TRUNCATED_URL_PREFIX = "https://ecs7.tokopedia.net/img/cache/"

fun ImageView.loadGif(url: String) = loadAsGif(url)

fun ImageView.loadImage(url: String, fpmItemLabel: String = "", listener: MediaListener? = null){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        listener({ resource, dataSource ->
            handleOnResourceReady(dataSource, resource, performanceMonitoring)
            listener?.onLoaded(resource, dataSource)
        }, {
            GlideErrorLogHelper().logError(context, it, url)
            listener?.onFailed(it)
        })
    }
}

fun ImageView.loadImageFitCenter(url: String, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        setPlaceHolder(R.drawable.placeholder_grey)
        fitCenter()
        listener({ resource, dataSource ->
            handleOnResourceReady(dataSource, resource, performanceMonitoring)
        }, {
            GlideErrorLogHelper().logError(context, it, url)
        })
    }
}

fun ImageView.loadIconFitCenter(url: String, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadIcon(url) {
        setPlaceHolder(R.drawable.placeholder_grey)
        fitCenter()
        listener({ resource, dataSource ->
            handleOnResourceReady(dataSource, resource, performanceMonitoring)
        }, {
            GlideErrorLogHelper().logError(context, it, url)
        })
    }
}

fun ImageView.loadImageRounded(url: String, roundedRadius: Int, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImageRounded(url, roundedRadius) { bitmap, dataSource ->
        handleOnResourceReady(dataSource, bitmap, performanceMonitoring)
        this.setImageBitmap(bitmap)
    }
}

fun ImageView.loadMiniImage(
        url: String,
        width: Int,
        height: Int,
        fpmItemLabel: String = "",
        onLoaded: () -> Unit,
        onFailed: () -> Unit
){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        setPlaceHolder(R.drawable.placeholder_grey)
        overrideSize(Resize(width, height))
        listener({ resource, dataSource ->
            onLoaded()
            handleOnResourceReady(dataSource, resource, performanceMonitoring)
        }, {
            onFailed()
        })
    }
}

fun ImageView.loadImageCenterCrop(url: String){
    this.loadImage(url) {
        setPlaceHolder(R.drawable.placeholder_grey)
        setRoundedRadius(15.toFloat())
        centerCrop()
    }
}

fun ImageView.loadImageNoRounded(url: String, placeholder: Int = -1){
    this.loadImage(url) {
        centerCrop()
        setPlaceHolder(placeholder)
    }
}

fun getPerformanceMonitoring(url: String, fpmItemLabel: String = "") : PerformanceMonitoring? {
    var performanceMonitoring : PerformanceMonitoring? = null

    //FPM only allow max 100 chars, so the url needs to be truncated
    val truncatedUrl = url.removePrefix(TRUNCATED_URL_PREFIX)

    if (fpmItemLabel.isNotEmpty()) {
        performanceMonitoring = PerformanceMonitoring.start(fpmItemLabel)
        performanceMonitoring.putCustomAttribute(FPM_ATTRIBUTE_IMAGE_URL, truncatedUrl)
    }
    return performanceMonitoring
}

fun handleOnResourceReady(dataSource: MediaDataSource?, resource: Bitmap?, performanceMonitoring: PerformanceMonitoring?) {
    if (dataSource == MediaDataSource.REMOTE) {
        performanceMonitoring?.stopTrace()
    }
}