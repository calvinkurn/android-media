package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.decorator.HeaderItemDecoration

class StickyHeadRecyclerView : ConstraintLayout {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrSet: AttributeSet) : super(context, attrSet)

    constructor(context: Context?, attrSet: AttributeSet, defStyleAttr: Int) : super(context, attrSet, defStyleAttr)

    private val headerRecyclerView:FrameLayout

    private val recyclerView:RecyclerView
    init {
        val view = View.inflate(context, R.layout.sticky_header_recycler_view, this)
        with(view) {
            headerRecyclerView = findViewById(R.id.header_recycler_view)
            recyclerView = findViewById(R.id.recycler_view)

        }
    }

    fun addHeaderRecycelerView(view:View) {
        headerRecyclerView.getChildAt(0)?.let {
            if(it !== view) {
                headerRecyclerView.removeAllViews()
                headerRecyclerView.addView(view)
            }
        }?:headerRecyclerView.addView(view)

    }

    fun setAdapter(adapter: DiscoveryRecycleAdapter) {
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(HeaderItemDecoration(this,isHeader = {
            adapter.isStickyHeaderView(it)
        }))
    }
    fun setLayoutManager(layoutManager:RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManager
    }
    fun addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        recyclerView.addOnScrollListener(listener)
    }

    fun removeHeaderRecyclerView() {
        if(headerRecyclerView.childCount >0 ) {
            headerRecyclerView.animate().translationY(headerRecyclerView.getHeight().toFloat());
            headerRecyclerView.removeAllViews()
            headerRecyclerView.animate().translationY(0f);

        }
    }

    fun smoothScrollToTop(){
        recyclerView.smoothScrollToPosition(0)
    }
}