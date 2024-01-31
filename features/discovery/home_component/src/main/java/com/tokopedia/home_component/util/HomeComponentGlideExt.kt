package com.tokopedia.home_component.util

import android.widget.ImageView
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadAsGif
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.media.loader.wrapper.MediaDataSource
import java.io.File
import java.net.URI
import java.util.*
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.topads.sdk.R as topadssdkR

const val FPM_ATTRIBUTE_IMAGE_URL = "image_url"
const val FPM_PRODUCT_ORGANIC_CHANNEL = "home_product_organic"
const val FPM_THEMATIC_CARD_VIEW = "home_thematic_card"
const val FPM_DYNAMIC_LEGO_BANNER = "home_lego_banner"
const val FPM_CATEGORY_WIDGET_ITEM = "home_category_widget_item"
const val FPM_USE_CASE_ICON = "home_use_case_icon"
const val FPM_DEALS_WIDGET_PRODUCT_IMAGE = "home_deals_widget_product_image"
const val FPM_SEE_ALL_CARD_BACKGROUND = "home_see_all_card_background_image"
const val FPM_RECOMMENDATION_LIST_CAROUSEL = "home_recommendation_list_carousel"
const val TRUNCATED_URL_PREFIX = "https://images.tokopedia.net/img/cache/"

fun ImageView.loadImage(
    url: String,
    fpmItemLabel: String = "",
    listener: ImageLoaderStateListener? = null,
    properties: Properties.() -> Unit = {}
){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    if(url.isGif()) {
        this.loadAsGif(url) {
            this.properties()
            homeLoadImageListener(url, fpmItemLabel, listener, performanceMonitoring, this@loadImage)
        }
    } else {
        this.loadImage(url) {
            this.properties()
            homeLoadImageListener(url, fpmItemLabel, listener, performanceMonitoring, this@loadImage)
        }
    }
}

fun ImageView.loadImageFitCenter(url: String, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    if(url.isGif()) {
        this.loadAsGif(url) {
            fitCenter()
            setPlaceHolder(topadssdkR.drawable.placeholder_grey)
            homeLoadImageListener(url, fpmItemLabel, null, performanceMonitoring, this@loadImageFitCenter)
        }
    } else {
        this.loadImage(url) {
            fitCenter()
            setPlaceHolder(topadssdkR.drawable.placeholder_grey)
            homeLoadImageListener(url, fpmItemLabel, null, performanceMonitoring, this@loadImageFitCenter)
        }
    }
}

fun ImageView.loadImageRounded(url: String, roundedRadius: Int, fpmItemLabel: String = ""){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    if(url.isGif()) {
        this.loadAsGif(url) {
            centerCrop()
            setRoundedRadius(roundedRadius.toFloat())
            homeLoadImageListener(url, fpmItemLabel, null, performanceMonitoring, this@loadImageRounded)
        }
    } else {
        this.loadImage(url) {
            centerCrop()
            setRoundedRadius(roundedRadius.toFloat())
            homeLoadImageListener(url, fpmItemLabel, null, performanceMonitoring, this@loadImageRounded)
        }
    }
}

fun ImageView.loadMiniImage(url: String, width: Int, height: Int, fpmItemLabel: String = "", listener: ImageLoaderStateListener? = null){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    if(url.isGif()) {
        this.loadAsGif(url) {
            overrideSize(Resize(width, height))
            setPlaceHolder(topadssdkR.drawable.placeholder_grey)
            fitCenter()
            homeLoadImageListener(url, fpmItemLabel, listener, performanceMonitoring, this@loadMiniImage)
        }
    } else {
        this.loadImage(url) {
            overrideSize(Resize(width, height))
            setPlaceHolder(topadssdkR.drawable.placeholder_grey)
            fitCenter()
            homeLoadImageListener(url, fpmItemLabel, listener, performanceMonitoring, this@loadMiniImage)
        }
    }
}

fun ImageView.loadImageCenterCrop(url: String){
    if(url.isGif()) {
        this.loadAsGif(url) {
            centerCrop()
            setRoundedRadius(15.toFloat())
            setPlaceHolder(topadssdkR.drawable.placeholder_grey)
        }
    } else {
        this.loadImage(url) {
            centerCrop()
            setRoundedRadius(15.toFloat())
            setPlaceHolder(topadssdkR.drawable.placeholder_grey)
        }
    }
}

fun ImageView.loadImageWithoutPlaceholder(url: String){
    if(url.isGif()) {
        this.loadAsGif(url) {
            setPlaceHolder(-1)
        }
    } else {
        this.loadImageWithoutPlaceholder(url)
    }
}

fun ImageView.loadImageWithoutPlaceholder(
    url: String,
    fpmItemLabel: String = "",
    listener: ImageLoaderStateListener? = null,
    skipErrorPlaceholder: Boolean = false,
){
    val imageView = this
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    if(url.isGif()) {
        this.loadAsGif(url) {
            setPlaceHolder(-1)
            if(skipErrorPlaceholder) setErrorDrawable(home_componentR.drawable.bg_transparent)
            homeLoadImageListener(url, fpmItemLabel, listener, performanceMonitoring, imageView)
        }
    } else {
        this.loadImageWithoutPlaceholder(url) {
            homeLoadImageListener(url, fpmItemLabel, listener, performanceMonitoring, imageView)
        }
    }
}

fun ImageView.loadImageNoRounded(url: String, placeholder: Int = -1, listener: ImageLoaderStateListener? = null){
    if(url.isGif()) {
        this.loadAsGif(url) {
            setPlaceHolder(placeholder)
            centerCrop()
            if (listener != null) {
                listener({ _, mediaDataSource ->
                    listener.successLoad(this@loadImageNoRounded)
                }, {
                    GlideErrorLogHelper().logError(context, it, url)
                    listener.failedLoad(this@loadImageNoRounded)
                })
            }
        }
    } else {
        this.loadImage(url) {
            setPlaceHolder(placeholder)
            centerCrop()
            if (listener != null) {
                listener({ _, mediaDataSource ->
                    listener.successLoad(this@loadImageNoRounded)
                }, {
                    GlideErrorLogHelper().logError(context, it, url)
                    listener.failedLoad(this@loadImageNoRounded)
                })
            }
        }
    }
}

fun ImageView.loadImageNormal(url: String, placeholder: Int = -1){
    if(url.isGif()) {
        this.loadAsGif(url) {
            setPlaceHolder(placeholder)
        }
    } else {
        this.loadImage(url) {
            setPlaceHolder(placeholder)
        }
    }
}

fun ImageView.loadImageWithDefault(url: String, fpmItemLabel: String = "", defaultImage: Int){
    val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
    if(url.isGif()) {
        this.loadAsGif(url) {
            setPlaceHolder(defaultImage)
            setErrorDrawable(defaultImage)
            homeLoadImageListener(url, fpmItemLabel, null, performanceMonitoring, this@loadImageWithDefault)
        }
    } else {
        this.loadImage(url) {
            setPlaceHolder(defaultImage)
            setErrorDrawable(defaultImage)
            homeLoadImageListener(url, fpmItemLabel, null, performanceMonitoring, this@loadImageWithDefault)
        }
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

fun handleOnResourceReady(dataSource: MediaDataSource?,
                          performanceMonitoring: PerformanceMonitoring?,
                          fpmItemLabel: String) {
    if (dataSource == MediaDataSource.REMOTE) {
        performanceMonitoring?.stopTrace()
    }
}

private fun Properties.homeLoadImageListener(
    url: String,
    fpmItemLabel: String,
    listener: ImageLoaderStateListener? = null,
    performanceMonitoring: PerformanceMonitoring?,
    view: ImageView,
) = listener({ _, mediaDataSource ->
    handleOnResourceReady(mediaDataSource, performanceMonitoring, fpmItemLabel)
    listener?.successLoad(view)
}, {
    GlideErrorLogHelper().logError(view.context, it, url)
    listener?.failedLoad(view)
})

private fun String.isGif(): Boolean {
    val ext = extReader(this)
    return ext.isNotEmpty() && (ext == "gif" || ext == "gifv")
}

private fun extReader(url: String): String {
    val uri: URI? = try {
        URI(url)
    } catch (_: Exception) {
        null
    }

    return try {
        File(uri?.path.orEmpty()).extension.lowercase(Locale.ENGLISH)
    } catch (_: Exception) {
        ""
    }
}

interface ImageLoaderStateListener {
    fun successLoad(view: ImageView)
    fun failedLoad(view: ImageView)
}
