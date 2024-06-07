package com.tokopedia.circular_view_pager.presentation.widgets.shimmeringImageView

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home_page_banner.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDataSource
import com.tokopedia.unifycomponents.LoaderUnify
import kotlinx.android.synthetic.main.layout_shimmering_image_view.view.*


class ShimmeringImageView @JvmOverloads constructor(context: Context, private val attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr){

    companion object{
        private const val FPM_ATTRIBUTE_IMAGE_URL = "image_url"
        private const val TRUNCATED_URL_PREFIX = "https://images.tokopedia.net/img/cache/"
    }

    private var loaderImageView: LoaderUnify?=null

    init {
        init()
    }

    private fun init(){
        View.inflate(context, R.layout.layout_shimmering_image_view, this)
        loaderImageView = attrs?.let { LoaderUnify(context, it) }
        loaderImageView?.type = LoaderUnify.TYPE_RECT
        this.addView(loaderImageView)
    }

    fun loadImage(url: String, fpmItemLabel: String = ""){
        loaderImageView?.visibility = View.VISIBLE
        imageView?.let {
            if (context.isValidGlideContext()) {
                val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)

                it.loadImage(url) {
                    centerCrop()
                    transition(BitmapTransitionOptions.withCrossFade())
                    listener(
                        onSuccess = { _, mediaDataSource ->
                            loaderImageView?.visibility = View.GONE
                            stopTraceOnResourceReady( mediaDataSource?.let { dataSource ->
                                MediaDataSource.mapTo(dataSource)
                            }, performanceMonitoring)
                        }
                    )
                }
            }
        }
    }

    fun loadImageRounded(url: String, roundedRadius: Int, fpmItemLabel: String = ""){
        loaderImageView?.visibility = View.VISIBLE
        imageView?.let {
            if (context.isValidGlideContext()) {
                val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)

                it.loadImage(url) {
                    transforms(listOf(CenterCrop(), RoundedCorners(roundedRadius)))
                    setCacheStrategy(MediaCacheStrategy.RESOURCE)
                    transition(BitmapTransitionOptions.withCrossFade())
                    listener(
                        onSuccess = { _, mediaDataSource ->
                            loaderImageView?.visibility = View.GONE
                            stopTraceOnResourceReady( mediaDataSource?.let { dataSource ->  MediaDataSource.mapTo(dataSource) }, performanceMonitoring)
                        }
                    )
                }
            }
        }
    }

    private fun getPerformanceMonitoring(url: String, fpmItemLabel: String) : PerformanceMonitoring? {

        //FPM only allow max 100 chars, so the url needs to be truncated
        val truncatedUrl = url.removePrefix(TRUNCATED_URL_PREFIX)


        val performanceMonitoring: PerformanceMonitoring? = PerformanceMonitoring.start(fpmItemLabel)
        performanceMonitoring?.putCustomAttribute(FPM_ATTRIBUTE_IMAGE_URL, truncatedUrl)

        return performanceMonitoring
    }

    fun stopTraceOnResourceReady(dataSource: DataSource?, performanceMonitoring: PerformanceMonitoring?) {
        if (dataSource == DataSource.REMOTE) {
            performanceMonitoring?.stopTrace()
        }
    }

    fun Context?.isValidGlideContext(): Boolean {
        return when {
            this == null -> false
            this is Activity -> !(this.isDestroyed || this.isFinishing)
            else -> true
        }
    }
}
