package com.tokopedia.hotel.roomlist.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.hotel.R
import kotlinx.android.synthetic.main.widget_image_view_pager.view.*

/**
 * @author by jessica on 16/04/19
 */

class ImageViewPager: BaseCustomView {

    var indicatorItems: ArrayList<ImageView> = arrayListOf()
    var imageUrls: ArrayList<String> = arrayListOf()

    var imageViewPagerListener: ImageViewPagerListener? = null

    var imageViewPagerAdapter: ImageViewPagerAdapter? = null
    get() = ImageViewPagerAdapter(imageUrls, imageViewPagerListener)

    var currentPosition: Int = 0

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) { init() }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) { init() }

    override fun onFinishInflate() {
        super.onFinishInflate()
        invalidate()
        requestLayout()
    }

    interface ImageViewPagerListener {
        fun onImageClicked(position: Int)
    }

    fun init() {
        View.inflate(context, R.layout.widget_image_view_pager, this)
    }

    fun buildView() {
        visibility = View.VISIBLE
        indicator_banner_container.visibility = View.VISIBLE

        indicatorItems.clear()
        indicator_banner_container.removeAllViews()
        viewpager_banner_category.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewpager_banner_category.layoutManager = layoutManager

        imageViewPagerAdapter = ImageViewPagerAdapter(arrayListOf(), imageViewPagerListener)
        viewpager_banner_category.adapter = imageViewPagerAdapter

        for (count in 0..imageUrls.size - 1) {
            val pointView = ImageView(context)
            pointView.setPadding(5,0,5,0)
            if (count == 0) pointView.setImageResource(getIndicatorFocus())
            else pointView.setImageResource(getIndicator())

            indicatorItems.add(pointView)
            indicator_banner_container.addView(pointView)
        }

        viewpager_banner_category.clearOnScrollListeners()
        viewpager_banner_category.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentPosition = (viewpager_banner_category.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (currentPosition != -1) viewpager_banner_category.smoothScrollToPosition(currentPosition)
                setCurrentIndicator()
            }
        })

        if (imageUrls.size == 1) indicator_banner_container.visibility = View.GONE

        val snapHelper = PagerSnapHelper()
        viewpager_banner_category.setOnFlingListener(null)
        snapHelper.attachToRecyclerView(viewpager_banner_category)
    }

    fun setCurrentIndicator() {
        for (i in 0..indicatorItems.size - 1) {
            if (currentPosition != i) indicatorItems.get(i).setImageResource(getIndicator())
            else indicatorItems.get(i).setImageResource(getIndicatorFocus())
        }
    }

    fun setImages(images: List<String>) {
        imageUrls.clear()
        imageUrls.addAll(images)
        imageViewPagerAdapter?.addImages(images)
    }

    fun getIndicatorFocus(): Int = R.drawable.widget_image_view_indicator_focus
    fun getIndicator(): Int = R.drawable.widget_image_view_indicator
}