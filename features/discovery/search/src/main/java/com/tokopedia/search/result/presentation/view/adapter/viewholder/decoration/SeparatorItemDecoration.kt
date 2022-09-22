package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter
import com.tokopedia.search.result.product.separator.VerticalSeparable

class SeparatorItemDecoration(
    context: Context,
    private val visitableAdapter: ProductListAdapter?,
) : RecyclerView.ItemDecoration() {

    private val separatorDrawable = ContextCompat.getDrawable(
        context,
        R.drawable.search_separator_drawable
    )

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val visitableAdapter = visitableAdapter ?: return
        val separatorDrawable = separatorDrawable ?: return
        if (parent.childCount.isZero()) return
        (0 until parent.childCount).forEach { index ->
            val view = parent.getChildAt(index)
            val childAdapterPosition = parent.getChildAdapterPosition(view)
            if (childAdapterPosition == RecyclerView.NO_POSITION) return

            val previousData = getPreviousData(childAdapterPosition)
            val currentData = visitableAdapter.itemList[childAdapterPosition]

            if (currentData is VerticalSeparable) {
                if (canDrawTopSeparator(currentData, previousData)) {
                    separatorDrawable.drawTopSeparator(view, parent, canvas)
                }
                if (currentData.verticalSeparator.hasBottomSeparator) {
                    separatorDrawable.drawBottomSeparator(view, parent, canvas)
                }
            }
        }
    }

    private fun getPreviousData(childAdapterPosition: Int): Visitable<*>? {
        val visitableAdapter = visitableAdapter ?: return null
        if (childAdapterPosition > 0) {
            return visitableAdapter.itemList[childAdapterPosition - 1]
        }
        return null
    }

    private fun Visitable<*>?.isBottomSeparator(): Boolean {
        return this is VerticalSeparable && this.verticalSeparator.hasBottomSeparator
    }

    private fun canDrawTopSeparator(
        verticalSeparable: VerticalSeparable,
        previousViewHolder: Visitable<*>?
    ): Boolean {
        return verticalSeparable.verticalSeparator.hasTopSeparator && !previousViewHolder.isBottomSeparator()
    }

    private fun Drawable.drawTopSeparator(view: View, parent: RecyclerView, canvas: Canvas) =
        apply {
            val left = parent.left
            val bottom = view.top
            val top = bottom - intrinsicHeight
            val right = parent.right
            bounds = Rect(left, top, right, bottom)
            draw(canvas)
        }

    private fun Drawable.drawBottomSeparator(view: View, parent: RecyclerView, canvas: Canvas) =
        apply {
            val left = parent.left
            val top = view.bottom
            val right = parent.right
            val bottom = view.bottom + intrinsicHeight
            bounds = Rect(left, top, right, bottom)
            draw(canvas)
        }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val visitableAdapter = visitableAdapter ?: return
        val separatorDrawable = separatorDrawable ?: return

        val childAdapterPosition = parent.getChildAdapterPosition(view)
        if (childAdapterPosition == RecyclerView.NO_POSITION) return

        val previousData = getPreviousData(childAdapterPosition)
        val currentData = visitableAdapter.itemList[childAdapterPosition]
        if (currentData is VerticalSeparable) {
            outRect.top = when {
                canDrawTopSeparator(currentData, previousData) -> separatorDrawable.intrinsicHeight
                else -> 0
            }
            outRect.bottom = when {
                currentData.verticalSeparator.hasBottomSeparator -> separatorDrawable.intrinsicHeight
                else -> 0
            }
        }
    }
}