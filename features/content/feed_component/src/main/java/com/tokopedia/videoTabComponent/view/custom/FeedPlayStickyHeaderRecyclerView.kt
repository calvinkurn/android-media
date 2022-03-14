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

    private val headerRecyclerView: FrameLayout
    private val recyclerView: RecyclerView
    private var headerItemDecoration: FeedHeaderItemDecoration? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var shouldShowStickyHeader = false
    private var isSLideDownAnimationEnabled = false
    private var handlerView: Handler? = Handler(Looper.getMainLooper())
    private val runnable by lazy { getRunnableForHeaderVisibility() }


    init {
        val view = View.inflate(context, R.layout.feed_sticky_header_recycler_view, this)
        with(view) {
            headerRecyclerView = findViewById(R.id.header_recycler_view)
            recyclerView = findViewById(R.id.recycler_view)
        }

    }

    fun addHeaderRecyclerView(view: View) {
        shouldShowStickyHeader = true
        headerRecyclerView.getChildAt(0)?.let {
            if (it !== view) {
                headerRecyclerView.removeAllViews()
                headerRecyclerView.addView(view)
            }
        } ?: headerRecyclerView.addView(view)
    }

    fun setHeaderViewVisibility(shouldShow: Boolean){
        if (shouldShow) {
            isSLideDownAnimationEnabled = true
            slideDown(headerRecyclerView)
        } else {
            if (isSLideDownAnimationEnabled)
                slideUp(headerRecyclerView)
            else
                headerRecyclerView.gone()
        }


    }
    private fun slideDown(view: View) {
        view.visible()
        view.animate().translationY(0f).setDuration(100L).start()

    }
    private fun slideUp(view: View) {
        if (!view.isVisible) {
            return
        }
        view.visible()
        view.animate().translationY((headerRecyclerView.height.toFloat())*-1).setDuration(100L).withEndAction { headerRecyclerView.gone() }.start()


    }


    private fun getRunnableForHeaderVisibility() = Runnable {
            if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE)
                slideDown(headerRecyclerView)
    }

    fun scrollToPosition(position: Int) {
        if (shouldShowStickyHeader)
            recyclerView.scrollLayout(position)
    }

    fun setShouldShowStickyHeaderValue(shouldShow: Boolean, time: Long) {
        this.shouldShowStickyHeader = shouldShow
        if (shouldShowStickyHeader){

            handlerView?.postDelayed(
                runnable
            , time)
        }else {
            handlerView?.removeCallbacks(runnable)
        }
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
    fun removeHeaderRecyclerView() {
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

    fun smoothScrollToPosition(position: Int = 0) {
        if (position == 0) {
            recyclerView.smoothScrollToPosition(position)
        } else {
            (recyclerView.layoutManager as? StaggeredGridLayoutManager)?.scrollToPositionWithOffset(position, 0)

        }
    }

}