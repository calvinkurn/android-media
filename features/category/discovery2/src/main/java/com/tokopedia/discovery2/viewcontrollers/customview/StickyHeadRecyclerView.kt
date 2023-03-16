package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.decorator.HeaderItemDecoration

class StickyHeadRecyclerView : ConstraintLayout {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrSet: AttributeSet) : super(context, attrSet)

    constructor(context: Context?, attrSet: AttributeSet, defStyleAttr: Int) : super(context, attrSet, defStyleAttr)

    private val headerRecyclerView: FrameLayout
    private val recyclerView: RecyclerView
    private var headerItemDecoration: HeaderItemDecoration? = null

    init {
        val view = View.inflate(context, R.layout.sticky_header_recycler_view, this)
        with(view) {
            headerRecyclerView = findViewById(R.id.header_recycler_view)
            recyclerView = findViewById(R.id.recycler_view)
        }
    }

    fun addHeaderRecyclerView(view: View) {
        headerRecyclerView.getChildAt(0)?.let {
            if (it !== view) {
                headerRecyclerView.removeAllViews()
                headerRecyclerView.addView(view)
            }
        } ?: headerRecyclerView.addView(view)

    }

    fun setAdapter(adapter: DiscoveryRecycleAdapter) {
        recyclerView.adapter?.let {
            headerItemDecoration?.let { recyclerView.removeItemDecoration(it) }
        }
        recyclerView.adapter = adapter
        headerItemDecoration = HeaderItemDecoration(this, isHeader = {
            adapter.isStickyHeaderView(it)
        }, notifySection = {
            adapter.notifySectionId(it)
        }
        )
        recyclerView.addItemDecoration(headerItemDecoration!!)
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManager
    }

    fun addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        recyclerView.addOnScrollListener(listener)
    }

    fun removeOnScrollListener(listener: RecyclerView.OnScrollListener) {
        recyclerView.removeOnScrollListener(listener)
    }

    fun setOnTouchListenerRecyclerView(listener: View.OnTouchListener){
        recyclerView.setOnTouchListener(listener)
    }

    fun addOnItemTouchListener(listener: RecyclerView.OnItemTouchListener){
        recyclerView.addOnItemTouchListener(listener)
    }

    fun removeHeaderRecyclerView() {
        if (headerRecyclerView.childCount > 0) {
            headerRecyclerView.animate().translationY(headerRecyclerView.getHeight().toFloat());
            headerRecyclerView.removeAllViews()
            headerRecyclerView.animate().translationY(0f);

        }
    }

    fun addDecorator(itemDecoration: RecyclerView.ItemDecoration){
        recyclerView.addItemDecoration(itemDecoration)
    }

    fun smoothScrollToPosition(position: Int = 0,isHeaderSticky:Boolean = false) {
        if (position == 0) {
            recyclerView.smoothScrollToPosition(position)
        }
        else {
            if(isHeaderSticky) {
                try {
                    val smoothScroller: SmoothScroller =
                        object : LinearSmoothScroller(context) {
                            override fun getVerticalSnapPreference(): Int {
                                return SNAP_TO_START
                            }

                            override fun calculateDyToMakeVisible(view: View?, snapPreference: Int): Int {
                                return super.calculateDyToMakeVisible(view, snapPreference) + dpToPx(55).toInt()
                            }
                        }
                    smoothScroller.targetPosition = position
                    (recyclerView.layoutManager as? StaggeredGridLayoutManager)?.startSmoothScroll(smoothScroller)
                } catch (e: Exception) {
                }
            }
        else{
                (recyclerView.layoutManager as? StaggeredGridLayoutManager)?.scrollToPositionWithOffset(position, 0)
            }
        }
    }
    fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
}
