package com.tokopedia.videoTabComponent.view.custom

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.util.util.scrollLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.videoTabComponent.viewmodel.VideoTabAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class FeedPlayStickyHeaderRecyclerView : ConstraintLayout {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrSet: AttributeSet) : super(context, attrSet)

    constructor(context: Context?, attrSet: AttributeSet, defStyleAttr: Int) : super(context, attrSet, defStyleAttr)

    val recyclerView: RecyclerView
    private val headerRecyclerView: FrameLayout
    private var headerItemDecoration: FeedHeaderItemDecoration? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var shouldShowStickyHeader = false
    private var isStickyHeaderEnabled = false
    private var isSLideDownAnimationEnabled = false

    init {
        val view = View.inflate(context, R.layout.feed_sticky_header_recycler_view, this)
        with(view) {
            headerRecyclerView = findViewById(R.id.header_recycler_view)
            recyclerView = findViewById(R.id.recycler_view)
        }

    }

    fun addHeaderRecyclerView(view: View) {
        shouldShowStickyHeader = true
        isStickyHeaderEnabled = true

        headerRecyclerView.getChildAt(0)?.let {
            if (it !== view) {
                headerRecyclerView.removeAllViews()
                headerRecyclerView.addView(view)
            }
        } ?: headerRecyclerView.addView(view)
    }

    fun scrollToPosition(position: Int) {
        if (shouldShowStickyHeader)
            recyclerView.scrollLayout(position)
    }

    fun setShouldShowStickyHeaderValue(shouldShow: Boolean, time: Long) {
        this.shouldShowStickyHeader = shouldShow

    }


    fun setAdapter(adapter: VideoTabAdapter) {
        recyclerView.adapter?.let {
            headerItemDecoration?.let { recyclerView.removeItemDecoration(it) }
        }
        recyclerView.adapter = adapter
        headerItemDecoration = FeedHeaderItemDecoration(this, isHeader = {
            adapter.isStickyHeaderView(it)
        })
        recyclerView.addItemDecoration(headerItemDecoration!!)
    }

    fun addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        recyclerView.addOnScrollListener(listener)
    }

    fun removeOnScrollListener(listener: RecyclerView.OnScrollListener) {
        recyclerView.removeOnScrollListener(listener)
    }

    fun removeHeaderRecyclerView() {
        isStickyHeaderEnabled = false
        shouldShowStickyHeader = false
        isSLideDownAnimationEnabled = false
        if (headerRecyclerView.childCount > 0) {
            headerRecyclerView.animate().translationY(headerRecyclerView.height.toFloat())
            headerRecyclerView.removeAllViews()
            headerRecyclerView.animate().translationY(0f);

        }
    }
    fun addDecorator(itemDecoration: RecyclerView.ItemDecoration){
        recyclerView.addItemDecoration(itemDecoration)
    }
    fun getLayoutManager() = recyclerView.layoutManager

    fun getViewHolderAtPosition(position: Int) =  recyclerView.findViewHolderForAdapterPosition(position)
    fun isStickyRecyclerViewIsEnabled() = isStickyHeaderEnabled

    fun smoothScrollToPosition(position: Int = 0) {
        if (position == 0) {
            recyclerView.smoothScrollToPosition(position)
        } else {
            (recyclerView.layoutManager as? StaggeredGridLayoutManager)?.scrollToPositionWithOffset(position, 0)

        }
    }

}
