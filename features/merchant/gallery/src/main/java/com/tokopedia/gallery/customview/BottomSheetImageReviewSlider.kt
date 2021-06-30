package com.tokopedia.gallery.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.gallery.R
import com.tokopedia.gallery.adapter.SliderAdapter
import com.tokopedia.gallery.viewmodel.ImageReviewItem

/**
 * Created by henrypriyono on 12/03/18.
 */

class BottomSheetImageReviewSlider : FrameLayout, ImageReviewSliderView {

    private var bottomSheetBehavior: UserLockBottomSheetBehavior<*>? = null
    private var containerView: View? = null
    private var backButton: View? = null
    private var bottomSheetLayout: View? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: SliderAdapter? = null

    private var callback: BottomSheetImageReviewSliderCallback? = null

    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    val isBottomSheetShown: Boolean
        get() = bottomSheetBehavior != null && bottomSheetBehavior!!.state != BottomSheetBehavior.STATE_HIDDEN

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        init()
    }

    private fun init() {
        bindView()
        initRecyclerView()
    }

    private fun bindView() {
        containerView = View.inflate(context, R.layout.review_image_slider, this)
        recyclerView = containerView!!.findViewById(R.id.review_image_slider_recycler_view)
        backButton = containerView!!.findViewById(R.id.backButton)
        bottomSheetLayout = this
    }

    private fun initRecyclerView() {
        adapter = SliderAdapter()
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = adapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        loadMoreTriggerListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (callback!!.isAllowLoadMore) {
                    callback!!.onRequestLoadMore(page)
                } else {
                    updateStateAfterGetData()
                }
            }
        }
        loadMoreTriggerListener?.let { recyclerView!!.addOnScrollListener(it) }
    }

    fun setup(callback: BottomSheetImageReviewSliderCallback) {
        this.callback = callback
        initListener()
    }

    fun closeView() {
        if (bottomSheetBehavior != null && bottomSheetBehavior!!.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun initListener() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout!!) as UserLockBottomSheetBehavior<*>
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN

        backButton!!.setOnClickListener { callback!!.onButtonBackPressed() }
    }

    override fun onBackPressed(): Boolean {
        return if (isBottomSheetShown) {
            closeView()
            true
        } else {
            false
        }
    }

    override fun resetState() {
        adapter!!.resetState()
        loadMoreTriggerListener!!.resetState()
    }

    override fun onLoadingData() {
        adapter!!.addLoading()
    }

    override fun displayImage(position: Int) {
        recyclerView!!.scrollToPosition(position)
        showBottomSheet()
    }

    private fun showBottomSheet() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onLoadDataSuccess(imageReviewItems: List<ImageReviewItem>, isHasNextPage: Boolean) {
        adapter!!.removeLoading()
        adapter!!.appendItems(imageReviewItems)
        loadMoreTriggerListener!!.updateStateAfterGetData()
        loadMoreTriggerListener!!.setHasNextPage(isHasNextPage)
    }

    override fun onLoadDataFailed() {
        adapter!!.removeLoading()
        loadMoreTriggerListener!!.updateStateAfterGetData()
        recyclerView!!.scrollToPosition(adapter!!.galleryItemCount - 1)
    }
}
