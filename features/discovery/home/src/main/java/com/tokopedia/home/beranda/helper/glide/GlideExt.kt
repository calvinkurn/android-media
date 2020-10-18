package com.tokopedia.home.beranda.helper.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home.R
import com.tokopedia.media.loader.common.LoaderStateListener
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadAsGif
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.transform.CenterCrop
import com.tokopedia.media.loader.transform.FitCenter
import com.tokopedia.media.loader.transform.RoundedCorners
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat

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

fun ImageView.loadImage(url: String, fpmItemLabel: String = "", listener: LoaderStateListener? = null){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        cacheStrategy = MediaCacheStrategy.RESOURCE
        loaderListener = object : LoaderStateListener {
            override fun successLoad(resource: Drawable?, dataSource: DataSource?) {
                handleOnResourceReady(dataSource, resource, performanceMonitoring)
                listener?.successLoad(resource, dataSource)
            }

            override fun failedLoad(error: GlideException?) {
                GlideErrorLogHelper().logError(context, error, url)
                listener?.failedLoad(error)
            }

        }
    }
}

fun ImageView.loadImageFitCenter(url: String, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        placeHolder = R.drawable.placeholder_grey
        decodeFormat = MediaDecodeFormat.PREFER_ARGB_8888
        cacheStrategy = MediaCacheStrategy.RESOURCE
        transform = FitCenter()
        loaderListener = object : LoaderStateListener {
            override fun successLoad(resource: Drawable?, dataSource: DataSource?) {
                handleOnResourceReady(dataSource, resource, performanceMonitoring)
            }

            override fun failedLoad(error: GlideException?) {
                GlideErrorLogHelper().logError(context, error, url)
            }

        }
    }
}

fun ImageView.loadImageRounded(url: String, roundedRadius: Int, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        decodeFormat = MediaDecodeFormat.PREFER_ARGB_8888
        cacheStrategy = MediaCacheStrategy.RESOURCE
        transforms = listOf(RoundedCorners(roundedRadius), CenterCrop())
        loaderListener = object : LoaderStateListener {
            override fun failedLoad(error: GlideException?) {}

            override fun successLoad(resource: Drawable?, dataSource: DataSource?) {
                handleOnResourceReady(dataSource, resource, performanceMonitoring)
            }
        }
    }
}

fun ImageView.loadMiniImage(url: String, width: Int, height: Int, fpmItemLabel: String = "", listener: LoaderStateListener? = null){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        placeHolder = R.drawable.placeholder_grey
        decodeFormat = MediaDecodeFormat.PREFER_ARGB_8888
        overrideSize = Resize(width, height)
        loaderListener = object : LoaderStateListener {
            override fun failedLoad(error: GlideException?) {
                listener?.failedLoad(error)
            }

            override fun successLoad(resource: Drawable?, dataSource: DataSource?) {
                listener?.successLoad(resource, dataSource)
                handleOnResourceReady(dataSource, resource, performanceMonitoring)
            }
        }
    }
}

fun ImageView.loadImageCenterCrop(url: String){
    this.loadImage(url) {
        decodeFormat = MediaDecodeFormat.PREFER_ARGB_8888
        placeHolder = R.drawable.placeholder_grey
        transforms = listOf(RoundedCorners(15), CenterCrop())
        cacheStrategy = MediaCacheStrategy.RESOURCE
    }
}

fun ImageView.loadImageWithoutPlaceholder(url: String){
    this.loadImage(url) {
        decodeFormat = MediaDecodeFormat.PREFER_ARGB_8888
        cacheStrategy = MediaCacheStrategy.RESOURCE
        placeHolder = -1
    }
}

fun ImageView.loadImageNoRounded(url: String, placeholder: Int = -1){
    this.loadImage(url) {
        decodeFormat = MediaDecodeFormat.PREFER_ARGB_8888
        cacheStrategy = MediaCacheStrategy.RESOURCE
        transform = CenterCrop()
        placeHolder = placeholder
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

fun handleOnResourceReady(dataSource: DataSource?, resource: Drawable?, performanceMonitoring: PerformanceMonitoring?) {
    if (dataSource == DataSource.REMOTE) {
        performanceMonitoring?.stopTrace()
    }
}