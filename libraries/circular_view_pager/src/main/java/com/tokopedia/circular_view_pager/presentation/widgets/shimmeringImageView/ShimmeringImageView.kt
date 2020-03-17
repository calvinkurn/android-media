package com.tokopedia.circular_view_pager.presentation.widgets.shimmeringImageView

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home_page_banner.R
import com.tokopedia.home_page_banner.ext.CrossFadeFactory
import kotlinx.android.synthetic.main.layout_shimmering_image_view.view.*


class ShimmeringImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr){

    val FPM_ATTRIBUTE_IMAGE_URL = "image_url"
    val FPM_HOMEPAGE_BANNER = "homepage_banner"
    val TRUNCATED_URL_PREFIX = "https://ecs7.tokopedia.net/img/cache/"

    init {
        init()
    }

    private fun init(){
        View.inflate(context, R.layout.layout_shimmering_image_view, this)
        shimmeringView?.visibility = View.GONE
    }

    fun loadImage(url: String){
        shimmeringView?.visibility = View.VISIBLE
        imageView?.let {
            val performanceMonitoring = getPerformanceMonitoring(url)
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            shimmeringView?.visibility = View.GONE
                            stopTraceOnResourceReady(dataSource, resource, performanceMonitoring)
                            return false
                        }
                    })
                    .into(it)
        }
    }

    fun getPerformanceMonitoring(url: String) : PerformanceMonitoring? {
        var performanceMonitoring : PerformanceMonitoring? = null

        //FPM only allow max 100 chars, so the url needs to be truncated
        val truncatedUrl = url.removePrefix(TRUNCATED_URL_PREFIX)


        performanceMonitoring = PerformanceMonitoring.start(FPM_HOMEPAGE_BANNER)
        performanceMonitoring.putCustomAttribute(FPM_ATTRIBUTE_IMAGE_URL, truncatedUrl)

        return performanceMonitoring
    }

    fun stopTraceOnResourceReady(dataSource: DataSource?, resource: Drawable?, performanceMonitoring: PerformanceMonitoring?) {
        if (dataSource == DataSource.REMOTE) {
            performanceMonitoring?.stopTrace()
        }
    }
}