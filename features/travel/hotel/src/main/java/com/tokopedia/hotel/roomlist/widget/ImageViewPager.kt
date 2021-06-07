package com.tokopedia.hotel.roomlist.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * @author by jessica on 16/04/19
 */

class ImageViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                               defStyleAttr: Int = 0): BaseCustomView(context, attrs, defStyleAttr) {

    private val indicatorBannerContainer: LinearLayout
    private val vpBannerCategory: RecyclerView

    private var indicatorItems: ArrayList<ImageView> = arrayListOf()
    var imageUrls: ArrayList<String> = arrayListOf()

    var imageViewPagerListener: ImageViewPagerListener? = null

    var imageViewPagerAdapter: ImageViewPagerAdapter? = null
    get() = ImageViewPagerAdapter(imageUrls, imageViewPagerListener)

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
        val view = View.inflate(context, R.layout.widget_hotel_image_view_pager, this)
        indicatorBannerContainer = view.findViewById(R.id.hotel_indicator_banner_container) as LinearLayout
        vpBannerCategory = view.findViewById(R.id.viewpager_banner_category) as RecyclerView
    }

    fun buildView() {
        visibility = View.VISIBLE
        indicatorBannerContainer.visibility = View.VISIBLE

        indicatorItems.clear()
        indicatorBannerContainer.removeAllViews()
        vpBannerCategory.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        vpBannerCategory.layoutManager = layoutManager

        imageViewPagerAdapter = ImageViewPagerAdapter(arrayListOf(), imageViewPagerListener)
        vpBannerCategory.adapter = imageViewPagerAdapter

        for (count in 0 until imageUrls.size) {
            val pointView = ImageView(context)
            pointView.setPadding(5,0,5,0)
            if (count == 0) pointView.setImageResource(getIndicatorFocus())
            else pointView.setImageResource(getIndicator())

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
            if (currentPosition != i) indicatorItems[i].setImageResource(getIndicator())
            else indicatorItems.get(i).setImageResource(getIndicatorFocus())
        }
    }

    fun setImages(images: List<String>) {
        imageUrls.clear()
        imageUrls.addAll(images)
        imageViewPagerAdapter?.addImages(images)
    }

    private fun getIndicatorFocus(): Int = R.drawable.widget_image_view_indicator_focus
    private fun getIndicator(): Int = R.drawable.hotel_widget_image_view_indicator
}