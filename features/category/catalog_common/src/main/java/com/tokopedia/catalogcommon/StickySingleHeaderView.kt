package com.tokopedia.catalogcommon

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.orZero
import kotlin.math.roundToInt

/**
 * Created by tlh on 2017/1/21 :)
 * https://github.com/TellH/RecyclerStickyHeaderView
 */
class StickySingleHeaderView : FrameLayout, OnStickySingleHeaderListener {

    companion object {
        private const val TOP_SPACING_STICKY_HEADER = 75
    }

    private var hasInit = false
    private var mHeaderContainer: FrameLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var mHeaderHeight = -1
    private var adapter: OnStickySingleHeaderAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    var stickyPosition = 4
    private var refreshSticky = false
    private var recyclerViewPaddingTop = 0
    private var currentScroll = 0

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

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

        recyclerViewPaddingTop = mRecyclerView?.paddingTop ?: 0
        mHeaderContainer = FrameLayout(context)
        mHeaderContainer?.let {
            it.setBackgroundColor(androidx.core.content.ContextCompat.getColor(context, android.R.color.transparent))
            it.clipToPadding = false
            it.clipChildren = false
            val newLayoutParam = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            newLayoutParam.setMargins(Int.ZERO, TOP_SPACING_STICKY_HEADER.dpToPx(resources.displayMetrics), Int.ZERO, Int.ZERO)
            it.layoutParams = newLayoutParam
            it.setPadding(
                Int.ZERO,
                Int.ZERO,
                mRecyclerView?.paddingRight.orZero(),
                Int.ZERO
            )
            it.visibility = View.GONE
            addView(mHeaderContainer)
        }
        val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mHeaderHeight == -1 || adapter == null || linearLayoutManager == null) {
                    mHeaderHeight = mHeaderContainer?.height ?: 0
                    val adapter = mRecyclerView?.adapter
                    if (adapter !is OnStickySingleHeaderAdapter) throw RuntimeException("Your RecyclerView.Adapter should be the type of StickyHeaderViewAdapter.")
                    this@StickySingleHeaderView.adapter = adapter
                    this@StickySingleHeaderView.adapter?.setListener(this@StickySingleHeaderView)
                    stickyPosition = this@StickySingleHeaderView.adapter?.stickyHeaderPosition ?: 0
                    linearLayoutManager = mRecyclerView?.layoutManager as? LinearLayoutManager
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

    private fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        if (mHeaderHeight == -1 || adapter == null || linearLayoutManager == null) return
        val firstCompletelyVisiblePosition = linearLayoutManager?.findFirstCompletelyVisibleItemPosition()
        val lastVisiblePosition = linearLayoutManager?.findLastVisibleItemPosition()
        if (firstCompletelyVisiblePosition != null) {
            if (firstCompletelyVisiblePosition > -1) {
                val _stickyPosition = stickyPosition + 1
                if (firstCompletelyVisiblePosition >= _stickyPosition && currentScroll >= recyclerViewPaddingTop) { // make the etalase label always visible
                    if (!isStickyShowed || refreshSticky) {
                        showSticky()
                        mHeaderContainer?.visibility = View.VISIBLE
                        refreshSticky = false
                    }
                    if (lastVisiblePosition == _stickyPosition) {
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
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}
