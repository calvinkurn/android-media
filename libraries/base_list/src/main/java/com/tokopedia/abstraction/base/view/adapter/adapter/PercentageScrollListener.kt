package com.tokopedia.abstraction.base.view.adapter.adapter

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.baselist.R
import com.tokopedia.config.GlobalConfig
import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

open class PercentageScrollListener : OnScrollListener() {

    private val itemVisibleRect by lazy { Rect() }
    private val globalVisibleRect by lazy { Rect() }
    private val set = ConstraintSet()
    private var percentText: TextView? = null

    companion object {
        const val DEV_OPT_ON_PERCENT_VIEW_ENABLED = "DEV_OPT_ON_PERCENT_VIEW_ENABLED"
        const val IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED = "IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED"
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (abs(dy) > 0 || abs(dx) > 0 ) {
            setVisiblePercentageOnScrollChanged(recyclerView)
        }
    }

    private fun addDebugView(v: View) {
        if (GlobalConfig.isAllowDebuggingTools() && isPercentViewEnabled(v.context)) {
            val layout = v as? ConstraintLayout ?: return
            percentText = layout.findViewById(R.id.percent_text_view)
            if (percentText == null) {
                percentText = LayoutInflater.from(v.context)
                    .inflate(R.layout.percent_text_view, layout, false) as TextView
                layout.addView(percentText)
                set.clone(layout)
                set.connect(percentText!!.id, ConstraintSet.TOP, layout.id, ConstraintSet.TOP, 0)
                set.connect(percentText!!.id, ConstraintSet.RIGHT, layout.id, ConstraintSet.RIGHT, 0)
                set.applyTo(layout)
            }
        }
    }

    private fun setVisiblePercentageOnScrollChanged(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager

        val firstPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val firstVisiblePosition = IntArray(layoutManager.spanCount)
                layoutManager.findFirstVisibleItemPositions(firstVisiblePosition).minOrNull()
            }
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
        } ?: return

        val lastPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisiblePosition = IntArray(layoutManager.spanCount)
                layoutManager.findLastVisibleItemPositions(lastVisiblePosition).maxOrNull()
            }
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> (layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
        } ?: return

        if (firstPosition == RecyclerView.NO_POSITION || lastPosition == RecyclerView.NO_POSITION) {
            return
        }

        for (pos in firstPosition..lastPosition) {
            setItemViewVisiblePercentage(layoutManager, pos, recyclerView)
        }
    }

    private fun setItemViewVisiblePercentage(layoutManager: RecyclerView.LayoutManager?, i: Int, recyclerView: RecyclerView) {
        val itemView = layoutManager?.findViewByPosition(i) ?: return
        addDebugView(itemView)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(i) as? IAdsViewHolderTrackListener
            ?: return

        val visibleAreaPercentage = getCalculateVisibleViewArea(itemView, itemVisibleRect)

        val prevValue = viewHolder.visiblePercentage

        val maxPercentageResult = max(prevValue, visibleAreaPercentage)
        viewHolder.setVisiblePercentage(maxPercentageResult)

        setPercentageViewText(maxPercentageResult, percentText)
    }
}

fun getCalculateVisibleViewArea(itemView: View, itemVisibleRect: Rect): Int {

    itemView.getGlobalVisibleRect(itemVisibleRect)

    val visibleWidth = itemVisibleRect.right - itemVisibleRect.left
    val visibleHeight = itemVisibleRect.bottom - itemVisibleRect.top

    val visibleViewArea = visibleWidth * visibleHeight

    // Ensure that the visible area is non-negative
    val visibleArea = max(0, visibleViewArea)

    val totalArea = itemView.width * itemView.height

    val visibleAreaPercentage = if (totalArea > 0) {
        ((visibleArea.toFloat() / totalArea) * 100).roundToInt()
    } else {
        0
    }

    return visibleAreaPercentage
}

private fun isPercentViewEnabled(context: Context): Boolean {
    val cache = context.getSharedPreferences(PercentageScrollListener.DEV_OPT_ON_PERCENT_VIEW_ENABLED, Context.MODE_PRIVATE)
    return cache.getBoolean(PercentageScrollListener.IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED, false)
}

private fun setPercentageViewText(visiblePercentage: Int, textView: TextView?) {
    val isPercentViewEnabled = textView?.context?.let { isPercentViewEnabled(it) } == true
    if (GlobalConfig.isAllowDebuggingTools() && isPercentViewEnabled) {
        val visiblePercentageStr = StringBuilder(visiblePercentage.toString()).append("%").toString()
        if (textView?.text != visiblePercentageStr) {
            textView?.text = visiblePercentageStr
        }
    }
}
