package com.tokopedia.salam.umrah.pdp.presentation.itemdecoration

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author by M on 8/11/19
 */
class UmrahPdpFaqIndicator : RecyclerView.ItemDecoration() {
    private val indicatorMarginTop = (DP * 16).toInt()
    private val indicatorStrokeWidth = DP * 3
    private val indicatorItemWidth = DP * 3
    private val indicatorSeparatorPadding = DP * 7

    private val interpolator = AccelerateDecelerateInterpolator()

    private val paint = Paint()

    init {
        paint.strokeWidth = indicatorStrokeWidth
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val itemCount = parent.adapter?.itemCount

        // center horizontally, calculate width and subtract half from center
        val totalLength = itemCount?.let { indicatorItemWidth * it }
        val paddingBetweenItems = itemCount?.minus(1)?.let { 0.coerceAtLeast(it) }?.times(indicatorSeparatorPadding)
        val indicatorTotalWidth = paddingBetweenItems?.let { totalLength?.plus(it) }
        val indicatorStartX = indicatorTotalWidth?.let { (parent.width - it) / 2f }

        // center vertically in the allotted space
        val indicatorPosY = parent.height - indicatorMarginTop / 2f

        if (indicatorStartX != null) {
            drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)
        }

        // find active page (which should be highlighted)
        val layoutManager = parent.layoutManager as LinearLayoutManager?
        var activePosition = layoutManager?.findFirstVisibleItemPosition()
        val isLastPosition = layoutManager?.findLastCompletelyVisibleItemPosition() == (layoutManager?.itemCount)?.minus(1)
        if (isLastPosition) activePosition = itemCount?.minus(1)
        if (activePosition == RecyclerView.NO_POSITION) {
            return
        }

        // find offset of active page (if the user is scrolling)
        val activeChild = activePosition?.let { layoutManager?.findViewByPosition(it) }
        val left = activeChild?.left
        val width = activeChild?.width

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        var progress = width?.toFloat()?.let { itWidth -> (left?.times(-1))?.div(itWidth)?.let { interpolator.getInterpolation(it) } }
        if (isLastPosition) progress = 0f

        if (indicatorStartX != null) {
            if (activePosition != null) {
                if (progress != null) {
                    drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress)
                }
            }
        }
    }

    private fun drawInactiveIndicators(c: Canvas, indicatorStartX: Float, indicatorPosY: Float, itemCount: Int) {
        paint.color = colorInactive

        val itemWidth = indicatorItemWidth + indicatorSeparatorPadding

        var start = indicatorStartX
        for (i in 0 until itemCount) {

            c.drawCircle(start, indicatorPosY, indicatorItemWidth / 2f, paint)

            start += itemWidth
        }
    }

    private fun drawHighlights(c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
                               highlightPosition: Int, progress: Float) {
        paint.color = colorActive

        // width of item indicator including padding
        val itemWidth = indicatorItemWidth + indicatorSeparatorPadding

        if (progress == 0f) {
            // no swipe, draw a normal indicator
            val highlightStart = indicatorStartX + itemWidth * highlightPosition

            c.drawCircle(highlightStart, indicatorPosY, indicatorItemWidth / 2f, paint)

        } else {
            val highlightStart = indicatorStartX + itemWidth * highlightPosition
            // calculate partial highlight
            val partialLength = indicatorItemWidth * progress + indicatorSeparatorPadding * progress

            c.drawCircle(highlightStart + partialLength, indicatorPosY, indicatorItemWidth / 2f, paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = indicatorMarginTop
    }

    companion object {
        private val DP = Resources.getSystem().displayMetrics.density
        private const val colorActive = 0x6603ac0e
        private const val colorInactive = 0x66a6b03d
    }
}