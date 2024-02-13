package com.tokopedia.home_component.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.util.RoundedCornersTransformation
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaDataSource
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

            it.loadImage(url) {
                centerCrop()
                listener(
                    onSuccess = { _, dataSource ->
                        loaderImageView?.visibility = View.GONE
                        dataSource?.let { mediaDataSource ->
                            stopTraceOnResourceReady(MediaDataSource.mapTo(mediaDataSource), performanceMonitoring, fpmItemLabel)
                        }
                    }
                )
            }
        }
    }

    fun loadImageRounded(url: String, radius: Int, fpmItemLabel: String = "", cornerType: RoundedCornersTransformation.CornerType? = null){
        loaderImageView?.visibility = View.VISIBLE
        imageView?.let {
            val transformation = cornerType?.let {
                MultiTransformation(
                    RoundedCornersTransformation(radius, it)
                )
            } ?: RoundedCorners(radius)
            val performanceMonitoring = getPerformanceMonitoring(url, fpmItemLabel)

            it.loadImage(url) {
                transforms(listOf(CenterCrop(), transformation))
                listener(
                    onSuccess = { _, dataSource ->
                        loaderImageView?.visibility = View.GONE
                        dataSource?.let { mediaDataSource ->
                            stopTraceOnResourceReady(MediaDataSource.mapTo(mediaDataSource), performanceMonitoring, fpmItemLabel)
                        }
                    }
                )
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

    fun stopTraceOnResourceReady(dataSource: DataSource?,
                                 performanceMonitoring: PerformanceMonitoring?,
                                 fpmItemLabel: String) {
        if (dataSource == DataSource.REMOTE) {
            performanceMonitoring?.stopTrace()
        }
    }
}
