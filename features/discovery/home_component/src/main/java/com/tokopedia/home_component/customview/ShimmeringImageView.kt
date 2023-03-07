package com.tokopedia.home_component.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home_component.R
import com.tokopedia.unifycomponents.LoaderUnify

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
    private var imageView: ImageView? = null

    private fun init(){
        val view = View.inflate(context, R.layout.layout_shimmering_image_view, this)
        loaderImageView = attrs?.let { LoaderUnify(context, it) }
        loaderImageView?.type = LoaderUnify.TYPE_RECT
        imageView = view?.findViewById(R.id.imageView)
        this.addView(loaderImageView)
    }

    fun loadImage(url: String, fpmItemLabel: String = ""){
        loaderImageView?.visibility = View.VISIBLE
        imageView?.let {
            val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            loaderImageView?.visibility = View.GONE
                            stopTraceOnResourceReady(dataSource, performanceMonitoring, fpmItemLabel)
                            return false
                        }
                    })
                    .into(it)
        }
    }

    fun loadImageRounded(url: String, radius: Int, fpmItemLabel: String = ""){
        loaderImageView?.visibility = View.VISIBLE
        imageView?.let {
            val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)
            Glide.with(context)
                    .load(url)
                    .transform(CenterCrop(), RoundedCorners(radius))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            loaderImageView?.visibility = View.GONE
                            stopTraceOnResourceReady(dataSource, performanceMonitoring, fpmItemLabel)
                            return false
                        }
                    })
                    .into(it)
        }
    }

    private fun getPerformanceMonitoring(url: String, fpmItemLabel: String) : PerformanceMonitoring? {

        //FPM only allow max 100 chars, so the url needs to be truncated
        val truncatedUrl = url.removePrefix(TRUNCATED_URL_PREFIX)


        val performanceMonitoring: PerformanceMonitoring? = PerformanceMonitoring.start(fpmItemLabel)
        performanceMonitoring?.putCustomAttribute(FPM_ATTRIBUTE_IMAGE_URL, truncatedUrl)

        return performanceMonitoring
    }

    fun stopTraceOnResourceReady(dataSource: DataSource?,
                                 performanceMonitoring: PerformanceMonitoring?,
                                 fpmItemLabel: String) {
        if (dataSource == DataSource.REMOTE) {
            performanceMonitoring?.stopTrace()
        }
    }
}
