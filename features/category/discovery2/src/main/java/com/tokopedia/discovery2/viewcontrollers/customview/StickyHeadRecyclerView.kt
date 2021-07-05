package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
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
        })
        recyclerView.addItemDecoration(headerItemDecoration!!)
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManager
    }

    fun addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        recyclerView.addOnScrollListener(listener)
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

    fun smoothScrollToPosition(position: Int = 0) {
        if (position == 0) {
            recyclerView.smoothScrollToPosition(position)
        } else {
            (recyclerView.layoutManager as? StaggeredGridLayoutManager)?.scrollToPositionWithOffset(position, 0)

        }
    }
}