package com.tokopedia.home.beranda.helper.glide

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.listener.MediaListener
import com.tokopedia.media.loader.loadAsGif
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaDataSource
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat

const val FPM_ATTRIBUTE_IMAGE_URL = "image_url"
const val FPM_THEMATIC_CARD_VIEW = "home_thematic_card"
const val FPM_DYNAMIC_LEGO_BANNER = "home_lego_banner"
const val FPM_CATEGORY_WIDGET_ITEM = "home_category_widget_item"
const val FPM_USE_CASE_ICON = "home_use_case_icon"
const val FPM_DEALS_WIDGET_PRODUCT_IMAGE = "home_deals_widget_product_image"
const val FPM_SEE_ALL_CARD_BACKGROUND = "home_see_all_card_background_image"
const val FPM_RECOMMENDATION_LIST_CAROUSEL = "home_recommendation_list_carousel"
const val TRUNCATED_URL_PREFIX = "https://images.tokopedia.net/img/cache/"

fun ImageView.loadGif(url: String) = loadAsGif(url)

fun ImageView.loadImage(url: String, fpmItemLabel: String = "", listener: MediaListener? = null) {
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        listener({ resource, dataSource ->
            handleOnResourceReady(dataSource, resource, performanceMonitoring, fpmItemLabel)
            listener?.onLoaded(resource, dataSource)
        }, {
            GlideErrorLogHelper().logError(context, it, url)
            listener?.onFailed(it)
        })
    }
}

fun ImageView.loadImageRounded(url: String, roundedRadius: Int, fpmItemLabel: String = "") {
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)

    this.loadImage(url) {
        decodeFormat(MediaDecodeFormat.PREFER_ARGB_8888)
        centerCrop()
        transforms(listOf(CenterCrop(), RoundedCorners(roundedRadius)))
        listener(
            onSuccess = { _, dataSource ->
                handleOnResourceReady(dataSource, null, performanceMonitoring, fpmItemLabel)
            }
        )
    }
}

fun ImageView.loadMiniImage(
    url: String,
    width: Int,
    height: Int,
    fpmItemLabel: String = "",
    onLoaded: () -> Unit,
    onFailed: () -> Unit
) {
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    this.loadImage(url) {
        setPlaceHolder(com.tokopedia.topads.sdk.R.drawable.placeholder_grey)
        overrideSize(Resize(width, height))
        listener({ resource, dataSource ->
            onLoaded()
            handleOnResourceReady(dataSource, resource, performanceMonitoring, fpmItemLabel)
        }, {
            onFailed()
        })
    }
}

fun ImageView.loadImageNoRounded(url: String, placeholder: Int = -1) {
    this.loadImage(url) {
        centerCrop()
        setPlaceHolder(placeholder)
    }
}

fun getPerformanceMonitoring(url: String, fpmItemLabel: String = ""): PerformanceMonitoring? {
    var performanceMonitoring: PerformanceMonitoring? = null

    // FPM only allow max 100 chars, so the url needs to be truncated
    val truncatedUrl = url.removePrefix(TRUNCATED_URL_PREFIX)

    if (fpmItemLabel.isNotEmpty()) {
        performanceMonitoring = PerformanceMonitoring.start(fpmItemLabel)
        performanceMonitoring.putCustomAttribute(FPM_ATTRIBUTE_IMAGE_URL, truncatedUrl)
    }
    return performanceMonitoring
}

fun handleOnResourceReady(
    dataSource: MediaDataSource?,
    resource: Bitmap?,
    performanceMonitoring: PerformanceMonitoring?,
    fpmItemLabel: String
) {
    if (dataSource == MediaDataSource.REMOTE) {
        performanceMonitoring?.stopTrace()
    }
}
