package com.tokopedia.affiliate.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.orZero

class AffiliateStickyHeaderView : FrameLayout, OnStickyHeaderListener {
    private var hasInit = false
    private var mHeaderContainer: LinearLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var mHeaderHeight = -1
    private var adapter: OnStickyHeaderAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var stickyPositions = intArrayOf()
    private var refreshSticky = false
    private var recyclerViewPaddingTop = 0
    private var currentScroll = 0
    private var viewHolders: List<StickyState>? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    interface OnStickyHeaderAdapter {
        fun stickyHeaderPositions(): IntArray
        fun createStickyViewHolder(parent: ViewGroup?): List<StickyState>
        fun bindSticky(viewHolder: RecyclerView.ViewHolder?)
        fun setListener(onAffiliateStickyHeaderViewListener: OnStickyHeaderListener?)
    }

    private fun initView() {
        if (hasInit) {
            return
        }
        hasInit = true
        clipToPadding = false
        clipChildren = false
        val view = getChildAt(0) as? RecyclerView
        mRecyclerView = view
        recyclerViewPaddingTop = mRecyclerView?.paddingTop.orZero()
        mHeaderContainer = LinearLayout(context)
        mHeaderContainer?.orientation = LinearLayout.VERTICAL
        val backgroundColor =
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Background)
        mHeaderContainer?.setBackgroundColor(backgroundColor)
        mHeaderContainer?.clipToPadding = false
        mHeaderContainer?.clipChildren = false
        mHeaderContainer?.isClickable = true
        mHeaderContainer?.layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mHeaderContainer?.visibility = GONE
        mHeaderContainer?.setBackgroundColor(backgroundColor)
        addView(mHeaderContainer)
        val onScrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (mHeaderHeight == -1 || adapter == null || linearLayoutManager == null) {
                        mHeaderHeight = mHeaderContainer?.height.orZero()
                        val adapter = mRecyclerView?.adapter
                        if (adapter !is OnStickyHeaderAdapter)
                            throw TypeCastException(
                                "Your RecyclerView.Adapter should be the type of StickyHeaderViewAdapter."
                            )
                        this@AffiliateStickyHeaderView.adapter = adapter
                        this@AffiliateStickyHeaderView.adapter?.setListener(this@AffiliateStickyHeaderView)
                        linearLayoutManager =
                            mRecyclerView?.layoutManager as? LinearLayoutManager
                    }
                    stickyPositions = adapter?.stickyHeaderPositions() ?: intArrayOf()
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    currentScroll = recyclerView.computeVerticalScrollOffset()
                    this@AffiliateStickyHeaderView.onScrolled()
                }
            }
        mRecyclerView?.addOnScrollListener(onScrollListener)
    }

    private fun onScrolled() {
        if (mHeaderHeight == -1 || adapter == null || linearLayoutManager == null) return
        if (viewHolders.isNullOrEmpty()) {
            viewHolders = adapter?.createStickyViewHolder(mHeaderContainer)
        }
        viewHolders?.forEach {
            val isItemVisibleOnScreen =
                linearLayoutManager?.findViewByPosition(it.position)?.let { view ->
                    linearLayoutManager?.isViewPartiallyVisible(
                        view,
                        true,
                        true
                    )
                } ?: false

            if (isItemVisibleOnScreen) {
                if (it.isStickyVisible) {
                    it.isStickyVisible = false
                    showSticky()
                }
            } else {
                if (!it.isStickyVisible) {
                    it.isStickyVisible = true
                    showSticky()
                    mHeaderContainer?.visibility = VISIBLE
                    refreshSticky = false
                }
            }
        }

        if (!isStickyShowed) {
            mHeaderContainer?.post {
                clearHeaderView()
                mHeaderContainer?.visibility = GONE
                refreshSticky = false
            }
        }
    }

    override val isStickyShowed: Boolean
        get() = mHeaderContainer?.childCount.orZero() > 0

    private fun showSticky() {
        clearHeaderView()
        if (viewHolders.isNullOrEmpty()) {
            viewHolders = adapter?.createStickyViewHolder(mHeaderContainer)
        }
        viewHolders?.forEach { viewHolder ->
            if (viewHolder.isStickyVisible) {
                mHeaderContainer?.addView(viewHolder.view.itemView)
                mHeaderHeight = mHeaderContainer?.height.orZero()
                adapter?.bindSticky(viewHolder.view)
                mHeaderContainer?.setPadding(
                    mRecyclerView?.paddingLeft.orZero(),
                    mHeaderContainer?.paddingTop.orZero(),
                    mRecyclerView?.paddingRight.orZero(),
                    mHeaderContainer?.paddingBottom.orZero()
                )
            }
        }
    }

    override fun refreshSticky() {
        refreshSticky = true
        onScrolled()
    }

    // Remove the Header View
    private fun clearHeaderView() {
        mHeaderContainer?.removeAllViews()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        initView()
        super.onLayout(changed, left, top, right, bottom)
    }

    class StickyState(
        val position: Int,
        val view: RecyclerView.ViewHolder,
        var isStickyVisible: Boolean = false
    )
}
