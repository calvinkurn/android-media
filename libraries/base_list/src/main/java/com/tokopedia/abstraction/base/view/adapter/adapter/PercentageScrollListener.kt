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
import kotlin.math.max
import kotlin.math.roundToInt

open class PercentageScrollListener : OnScrollListener() {

    private val globalVisibleRect = Rect()
    private val itemVisibleRect = Rect()
    private val set = ConstraintSet()
    private var percentText: TextView? = null

    private val DEV_OPT_ON_PERCENT_VIEW_ENABLED = "DEV_OPT_ON_PERCENT_VIEW_ENABLED"
    private val IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED = "IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED"

    private fun isPercentViewEnabled(context: Context): Boolean {
        val cache = context.getSharedPreferences(DEV_OPT_ON_PERCENT_VIEW_ENABLED, Context.MODE_PRIVATE)
        return cache.getBoolean(IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED, false)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        setVisiblePercentageOnScrollChanged(recyclerView)
    }

    private fun addDebugView(v: View){
        if(GlobalConfig.isAllowDebuggingTools() && isPercentViewEnabled(v.context)) {
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
            is StaggeredGridLayoutManager -> layoutManager.findFirstVisibleItemPositions(null).minOrNull()
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
        } ?: return

        val lastPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> layoutManager.findLastVisibleItemPositions(null).maxOrNull()
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> (layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
        } ?: return

        if (firstPosition == RecyclerView.NO_POSITION || lastPosition == RecyclerView.NO_POSITION) {
            return
        }

        recyclerView.getGlobalVisibleRect(globalVisibleRect)

        for (pos in firstPosition..lastPosition) {
            setItemViewVisiblePercentage(layoutManager, pos, recyclerView, globalVisibleRect)
        }
    }

    private fun setItemViewVisiblePercentage(layoutManager: RecyclerView.LayoutManager?, i: Int, recyclerView: RecyclerView, globalVisibleRect: Rect) {
        val itemView = layoutManager?.findViewByPosition(i) ?: return
        addDebugView(itemView)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(i) as? IAdsViewHolderTrackListener
            ?: return

        itemView.getGlobalVisibleRect(itemVisibleRect)

        val visibleAreaPercentage = getCalculateVisibleViewArea(itemView, itemVisibleRect, globalVisibleRect)

        val prevValue = viewHolder.visiblePercentage

        viewHolder.setVisiblePercentage(max(prevValue, visibleAreaPercentage))
        percentText?.text = "${viewHolder.visiblePercentage}%"
    }
}

fun getCalculateVisibleViewArea(itemView: View, itemVisibleRect: Rect, globalVisibleRect: Rect): Int {
    val visibleWidth = minOf(itemVisibleRect.right, globalVisibleRect.right) - maxOf(itemVisibleRect.left, globalVisibleRect.left)
    val visibleHeight = minOf(itemVisibleRect.bottom, globalVisibleRect.bottom) - maxOf(itemVisibleRect.top, globalVisibleRect.top)

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
