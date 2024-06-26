package com.tokopedia.shop_widget.customview

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop_widget.R

class StickySingleHeaderView : FrameLayout, OnStickySingleHeaderListener {
    private var hasInit = false
    private var mHeaderContainer: FrameLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var mHeaderHeight = -1
    private var adapter: OnStickySingleHeaderAdapter? = null
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var stickyPosition = 0
    private var refreshSticky = false
    private var recyclerViewPaddingTop = 0
    private var currentScroll = 0
    private var viewHolder: RecyclerView.ViewHolder? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @Suppress("unused")
    val containerHeight: Int
        get() {
            if (null != mHeaderContainer) {
                mHeaderContainer?.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
                return mHeaderContainer?.measuredHeight.orZero()
            }
            return 0
        }

    interface OnStickySingleHeaderAdapter {
        val stickyHeaderPosition: Int
        fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder?
        fun bindSticky(viewHolder: RecyclerView.ViewHolder?)
        fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?)
        fun onStickyHide()
    }

    private fun initView() {
        if (hasInit) {
            return
        }
        hasInit = true
        val view = getRecyclerView()
        mRecyclerView = view
        recyclerViewPaddingTop = mRecyclerView?.paddingTop.orZero()
        mHeaderContainer = FrameLayout(context)
        val backgroundDrawable = MethodChecker.getDrawable(context, R.drawable.shop_widget_sticky_single_header_view_bottom_shadow)
        mHeaderContainer?.background = backgroundDrawable
        mHeaderContainer?.isClickable = true
        mHeaderContainer?.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mHeaderContainer?.visibility = GONE
        mHeaderContainer?.background = backgroundDrawable
        addView(mHeaderContainer)
        val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mHeaderHeight == -1 || adapter == null || staggeredGridLayoutManager == null) {
                    mHeaderHeight = mHeaderContainer?.height.orZero()
                    val adapter = mRecyclerView?.adapter
                    if (adapter !is OnStickySingleHeaderAdapter) throw RuntimeException("Your RecyclerView.Adapter should be the type of StickyHeaderViewAdapter.")
                    this@StickySingleHeaderView.adapter = adapter
                    this@StickySingleHeaderView.adapter?.setListener(this@StickySingleHeaderView)
                    staggeredGridLayoutManager = mRecyclerView?.layoutManager as StaggeredGridLayoutManager?
                }
                stickyPosition = adapter?.stickyHeaderPosition.orZero()
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentScroll = recyclerView.computeVerticalScrollOffset()
                this@StickySingleHeaderView.onScrolled(recyclerView, dx, dy)
            }
        }
        mRecyclerView?.addOnScrollListener(onScrollListener)
    }

    private fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        if (mHeaderHeight == -1 || adapter == null || staggeredGridLayoutManager == null) return
        val firstCompletelyVisiblePosition = staggeredGridLayoutManager?.findFirstCompletelyVisibleItemPositions(null)?.getOrNull(0).orZero()
        val firstVisiblePosition = staggeredGridLayoutManager?.findFirstVisibleItemPositions(null)?.getOrNull(0).orZero()
        if (firstCompletelyVisiblePosition > -1 && stickyPosition != -1) {
            if (firstCompletelyVisiblePosition > stickyPosition && currentScroll >= recyclerViewPaddingTop) {
                // make the etalase label always visible
                if (!isStickyShowed || refreshSticky) {
                    showSticky()
                    mHeaderContainer?.visibility = VISIBLE
                    refreshSticky = false
                }
            } else {
                // make the etalase label always gone
                if (isStickyShowed || refreshSticky) {
                    adapter?.onStickyHide()
                    Handler().post {
                        clearHeaderView()
                        mHeaderContainer?.visibility = GONE
                        refreshSticky = false
                    }
                }
            }
        }
    }

    override val isStickyShowed: Boolean
        get() = mHeaderContainer?.childCount.orZero() > 0

    private fun showSticky() {
        clearHeaderView()
        if (viewHolder == null) {
            viewHolder = adapter?.createStickyViewHolder(mHeaderContainer)
        }
        mHeaderContainer?.addView(viewHolder?.itemView)
        mHeaderHeight = mHeaderContainer?.height.orZero()
        adapter?.bindSticky(viewHolder)
        mHeaderContainer?.setPadding(mRecyclerView?.paddingLeft.orZero(), mHeaderContainer?.paddingTop.orZero(), mRecyclerView?.paddingRight.orZero(), mHeaderContainer?.paddingBottom.orZero())
    }

    override fun refreshSticky() {
        refreshSticky = true
        onScrolled(mRecyclerView, 0, 0)
    }

    // Remove the Header View
    fun clearHeaderView() {
        mHeaderContainer?.removeAllViews()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        initView()
        super.onLayout(changed, left, top, right, bottom)
    }

    private fun getRecyclerView(): RecyclerView {
        val view = getChildAt(0) ?: throw RuntimeException("StickySingleHeaderView should has child of RecyclerView.")

        return if (view !is RecyclerView && view is ViewGroup) view.findRecyclerView() ?: throw RuntimeException("StickySingleHeaderView should has child of RecyclerView.")
        else view as? RecyclerView ?: throw RuntimeException("StickySingleHeaderView should has child of RecyclerView.")
    }

    private fun ViewGroup.findRecyclerView(): RecyclerView? {
        for(i in 0..childCount) {
            val child: View? = this.getChildAt(i)
            if (child is RecyclerView) return child
        }
        return null
    }
}
