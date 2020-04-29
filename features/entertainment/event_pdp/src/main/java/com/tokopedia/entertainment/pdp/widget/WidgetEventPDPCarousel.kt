package com.tokopedia.entertainment.pdp.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.pdp.R
import com.tokopedia.unifycomponents.BaseCustomView


class WidgetEventPDPCarousel @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                       defStyleAttr: Int = 0): BaseCustomView(context, attrs, defStyleAttr) {

    private val indicatorBannerContainer: LinearLayout
    private val vpBannerCategory: RecyclerView

    private var indicatorItems: ArrayList<ImageView> = arrayListOf()
    var imageUrls: ArrayList<String> = arrayListOf()

    var imageViewPagerListener: ImageViewPagerListener? = null

    var imageViewPagerAdapter: WidgetEventPDPCarouselAdapter? = null
        get() = WidgetEventPDPCarouselAdapter(imageUrls, imageViewPagerListener)

    var currentPosition: Int = 0

    override fun onFinishInflate() {
        super.onFinishInflate()
        invalidate()
        requestLayout()
    }

    interface ImageViewPagerListener {
        fun onImageClicked(position: Int)
    }

    init {
        val view = View.inflate(context, R.layout.widget_event_pdp_carousel, this)
        indicatorBannerContainer = view.findViewById(R.id.event_pdp_indicator_banner_container) as LinearLayout
        vpBannerCategory = view.findViewById(R.id.viewpager_event_pdp) as RecyclerView
    }

    fun buildView() {
        visibility = View.VISIBLE
        indicatorBannerContainer.visibility = View.VISIBLE

        indicatorItems.clear()
        indicatorBannerContainer.removeAllViews()
        vpBannerCategory.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        vpBannerCategory.layoutManager = layoutManager

        imageViewPagerAdapter = WidgetEventPDPCarouselAdapter(arrayListOf(), imageViewPagerListener)
        vpBannerCategory.adapter = imageViewPagerAdapter

        for (count in 0 until imageUrls.size) {
            val pointView = ImageView(context)
            pointView.setPadding(5,0,5,0)
            if (count == 0) pointView.setImageDrawable(resizeIndicator(getIndicatorFocus(),imageUrls.size))
            else pointView.setImageDrawable(resizeIndicator(getIndicator(),imageUrls.size))

            indicatorItems.add(pointView)
            indicatorBannerContainer.addView(pointView)
        }

        vpBannerCategory.clearOnScrollListeners()
        vpBannerCategory.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentPosition = (vpBannerCategory.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (currentPosition != -1) vpBannerCategory.smoothScrollToPosition(currentPosition)
                setCurrentIndicator()
            }
        })

        if (imageUrls.size == 1) indicatorBannerContainer.visibility = View.GONE

        val snapHelper = PagerSnapHelper()
        vpBannerCategory.onFlingListener = null
        snapHelper.attachToRecyclerView(vpBannerCategory)
    }

    fun setCurrentIndicator() {
        for (i in 0 until indicatorItems.size) {
            if (currentPosition != i) indicatorItems[i].setImageDrawable(resizeIndicator(getIndicator(),indicatorItems.size))
            else indicatorItems.get(i).setImageDrawable(resizeIndicator(getIndicatorFocus(),indicatorItems.size))
        }
    }

    fun setImages(images: List<String>) {
        imageUrls.clear()
        imageUrls.addAll(images)
        imageViewPagerAdapter?.addImages(images)
    }

    fun resizeIndicator(id: Int, size: Int): GradientDrawable{
        val getDisplayWidth = Resources.getSystem().getDisplayMetrics().widthPixels/(size+1)
        val gradientDrawable : GradientDrawable = context.resources.getDrawable(id) as GradientDrawable
        gradientDrawable.setSize(getDisplayWidth,16)
        return gradientDrawable
    }

    private fun getIndicatorFocus(): Int = R.drawable.widget_event_pdp_indicator_focus
    private fun getIndicator(): Int = R.drawable.widget_event_pdp_indicator
}