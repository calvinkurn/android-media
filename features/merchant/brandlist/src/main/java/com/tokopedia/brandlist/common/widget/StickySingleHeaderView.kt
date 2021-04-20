package com.tokopedia.brandlist.common.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.roundToInt


/**
 * Created by tlh on 2017/1/21 :)
 * https://github.com/TellH/RecyclerStickyHeaderView
 */
class StickySingleHeaderView : FrameLayout, OnStickySingleHeaderListener {
    private var hasInit = false
    private var mHeaderContainer: FrameLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var mHeaderHeight = -1
    private var adapter: OnStickySingleHeaderAdapter? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var stickyPosition = 4
    private var refreshSticky = false
    private var recyclerViewPaddingTop = 0
    private var currentScroll = 0
    private val headerContainerHeight = 162

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    val containerHeight: Int
        get() {
            mHeaderContainer!!.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            return headerContainerHeight
        }

    interface OnStickySingleHeaderAdapter {
        val stickyHeaderPosition: Int

        fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder
        fun bindSticky(viewHolder: RecyclerView.ViewHolder?)
        fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?)
        fun updateEtalaseListViewHolderData()
    }

    private fun initView() {
        if (hasInit) {
            return
        }
        hasInit = true
        clipToPadding = false
        clipChildren = false
        val view = getChildAt(0) as? RecyclerView
                ?: throw RuntimeException("RecyclerView should be the first child view.")
        mRecyclerView = view

        if (mRecyclerView == null) {
            return
        }

        recyclerViewPaddingTop = mRecyclerView?.paddingTop ?: 0
        mHeaderContainer = FrameLayout(context)
        mHeaderContainer?.let {
            it.setBackgroundColor(androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            it.clipToPadding = false
            it.clipChildren = false
            it.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setPadding(
                    convertPixelsToDp(16, context),
                    0,
                    mRecyclerView?.paddingRight ?: 0,
                    0)
            it.visibility = View.GONE
            addView(mHeaderContainer)
        }
        val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mHeaderHeight == -1 || adapter == null || gridLayoutManager == null) {
                    mHeaderHeight = mHeaderContainer?.height ?: 0
                    val adapter = mRecyclerView?.adapter
                    if (adapter !is OnStickySingleHeaderAdapter) throw RuntimeException("Your RecyclerView.Adapter should be the type of StickyHeaderViewAdapter.")
                    this@StickySingleHeaderView.adapter = adapter
                    this@StickySingleHeaderView.adapter?.setListener(this@StickySingleHeaderView)
                    stickyPosition = this@StickySingleHeaderView.adapter?.stickyHeaderPosition ?: 0
                    gridLayoutManager = mRecyclerView?.layoutManager as GridLayoutManager?
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentScroll = recyclerView.computeVerticalScrollOffset()
                this@StickySingleHeaderView.onScrolled(recyclerView, dx, dy)
            }
        }
        mRecyclerView?.addOnScrollListener(onScrollListener)
    }

    private fun convertPixelsToDp(px: Int, context: Context): Int {
        val result = px.toFloat() / (context.getResources().getDisplayMetrics().densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat())
        return result.roundToInt()
    }

    private fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        if (mHeaderHeight == -1 || adapter == null || gridLayoutManager == null) return
        val firstCompletelyVisiblePosition = gridLayoutManager?.findFirstCompletelyVisibleItemPosition()
        val firstVisiblePosition = gridLayoutManager?.findFirstVisibleItemPosition()
        if (firstCompletelyVisiblePosition != null) {
            if (firstCompletelyVisiblePosition > -1) {
                val _stickyPosition = 4
                if (firstCompletelyVisiblePosition >= _stickyPosition && currentScroll >= recyclerViewPaddingTop) { // make the etalase label always visible
                    if (!isStickyShowed || refreshSticky) {
                        showSticky()
                        mHeaderContainer?.visibility = View.VISIBLE
                        refreshSticky = false
                    }
                    if (firstVisiblePosition == _stickyPosition) {
                        adapter?.updateEtalaseListViewHolderData()
                    }
                } else { // make the etalase label always gone
                    if (isStickyShowed || refreshSticky) {
                        clearHeaderView()
                        mHeaderContainer?.visibility = View.GONE
                        refreshSticky = false
                    }
                }
            }
        }
    }

    override val isStickyShowed: Boolean
        get() = mHeaderContainer?.let { it.childCount > 0 } ?: false

    private fun showSticky() {
        clearHeaderView()
        val viewHolder = adapter?.createStickyViewHolder(mHeaderContainer)
        mHeaderContainer?.addView(viewHolder?.itemView)
        mHeaderHeight = mHeaderContainer?.height ?: 0
        adapter?.bindSticky(viewHolder)
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
        try {
            super.onLayout(changed, left, top, right, bottom)
        } catch (e: IndexOutOfBoundsException) {
        }
    }
}