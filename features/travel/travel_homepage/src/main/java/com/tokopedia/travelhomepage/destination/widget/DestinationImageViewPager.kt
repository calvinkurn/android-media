package com.tokopedia.travelhomepage.destination.widget

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder.DestinationImageViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_travel_homepage_destination.view.*
import kotlinx.android.synthetic.main.widget_image_view_pager.view.*

/**
 * @author by jessica on 16/04/19
 */

class DestinationImageViewPager: FrameLayout {

    var imageUrls: ArrayList<String> = arrayListOf()

    var imageViewPagerListener: ImageViewPagerListener? = null

    var destinationImageViewPagerAdapter: DestinationImageViewPagerAdapter? = null
    get() = DestinationImageViewPagerAdapter(imageUrls, imageViewPagerListener)

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
        fun onImageScrolled(position: Int)
    }

    fun init() {
        View.inflate(context, R.layout.widget_image_view_pager, this)
    }

    fun buildView() {
        visibility = View.VISIBLE

        viewpager_banner_category.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewpager_banner_category.layoutManager = layoutManager

        destinationImageViewPagerAdapter = DestinationImageViewPagerAdapter(arrayListOf(), imageViewPagerListener)
        viewpager_banner_category.adapter = destinationImageViewPagerAdapter

        viewpager_banner_category.clearOnScrollListeners()
        viewpager_banner_category.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentPosition = (viewpager_banner_category.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (currentPosition != -1) viewpager_banner_category.smoothScrollToPosition(currentPosition)
                imageViewPagerListener?.onImageScrolled(currentPosition)
            }
        })

        if (imageUrls.size == 1) indicator_banner_container.visibility = View.GONE

        val snapHelper = PagerSnapHelper()
        viewpager_banner_category.setOnFlingListener(null)
        snapHelper.attachToRecyclerView(viewpager_banner_category)
    }

    fun setImages(images: List<String>) {
        imageUrls.clear()
        imageUrls.addAll(images)
        destinationImageViewPagerAdapter?.addImages(images)
    }

}