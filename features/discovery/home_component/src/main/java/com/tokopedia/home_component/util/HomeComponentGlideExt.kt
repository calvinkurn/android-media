package com.tokopedia.home_component.util

import android.widget.ImageView
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home_component.R
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadAsGif
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
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


fun ImageView.loadImage(url: String, fpmItemLabel: String = "", listener: ImageHandler.ImageLoaderStateListener? = null){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        listener({ _, mediaDataSource ->
            handleOnResourceReady(mediaDataSource, performanceMonitoring)
            listener?.successLoad()
        }, {
            GlideErrorLogHelper().logError(context, it, url)
            listener?.failedLoad()
        })
    }
}

fun ImageView.loadImageFitCenter(url: String, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        fitCenter()
        setPlaceHolder(R.drawable.placeholder_grey)
        listener({ _, mediaDataSource ->
            handleOnResourceReady(mediaDataSource, performanceMonitoring)
        }, {
            GlideErrorLogHelper().logError(context, it, url)
        })
    }
}

fun ImageView.loadImageRounded(url: String, roundedRadius: Int, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        centerCrop()
        setRoundedRadius(roundedRadius.toFloat())
        listener({ _, mediaDataSource ->
            handleOnResourceReady(mediaDataSource, performanceMonitoring)
        })
    }
}

fun ImageView.loadMiniImage(url: String, width: Int, height: Int, fpmItemLabel: String = "", listener: ImageHandler.ImageLoaderStateListener? = null){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        overrideSize(Resize(width, height))
        setPlaceHolder(R.drawable.placeholder_grey)
        fitCenter()
        listener({ _, mediaDataSource ->
            listener?.successLoad()
            handleOnResourceReady(mediaDataSource, performanceMonitoring)
        }, {
            listener?.failedLoad()
        })
    }
}

fun ImageView.loadImageCenterCrop(url: String){
    this.loadImage(url) {
        centerCrop()
        setRoundedRadius(15.toFloat())
        setPlaceHolder(R.drawable.placeholder_grey)
    }
}

fun ImageView.loadImageWithoutPlaceholder(url: String){
    this.loadImageWithoutPlaceholder(url)
}

fun ImageView.loadImageWithoutPlaceholder(url: String, fpmItemLabel: String = "", listener: ImageHandler.ImageLoaderStateListener? = null){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImageWithoutPlaceholder(url) {
        listener({ _, mediaDataSource ->
            handleOnResourceReady(mediaDataSource, performanceMonitoring)
            listener?.successLoad()
        }, {
            GlideErrorLogHelper().logError(context, it, url)
            listener?.failedLoad()
        })
    }
}

fun ImageView.loadImageNoRounded(url: String, placeholder: Int = -1){
    this.loadImage(url) {
        setPlaceHolder(placeholder)
        centerCrop()
    }
}

fun ImageView.loadGif(url: String){
    this.loadAsGif(url) {
        setRoundedRadius(10.toFloat())
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

fun handleOnResourceReady(dataSource: MediaDataSource?, performanceMonitoring: PerformanceMonitoring?) {
    if (dataSource == MediaDataSource.REMOTE) {
        performanceMonitoring?.stopTrace()
    }
}