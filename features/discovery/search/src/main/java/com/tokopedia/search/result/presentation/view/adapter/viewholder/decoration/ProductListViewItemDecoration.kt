package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter
import com.tokopedia.unifycomponents.toPx

internal class ProductListViewItemDecoration(
    context: Context,
    private val visitableAdapter: ProductListAdapter?,
) : RecyclerView.ItemDecoration() {

    private val separatorDrawable = ContextCompat.getDrawable(
        context,
        R.drawable.search_list_view_separator_drawable
    )

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val visitableAdapter = visitableAdapter ?: return
        val separatorDrawable = separatorDrawable ?: return
        (0 until parent.childCount).forEach { index ->
            val view = parent.getChildAt(index)

            val childAdapterPosition = parent.getChildAdapterPosition(view)
            if (isAdapterPositionNotInRange(childAdapterPosition, visitableAdapter)) return

            val currentData = visitableAdapter.itemList[childAdapterPosition]
            val nextItemPosition = childAdapterPosition + 1
            val nextData = visitableAdapter.getNextData(nextItemPosition)

            if (canDrawListViewSeparator(currentData, view, nextData)) {
                separatorDrawable.apply {
                    val left = view.left + 8.toPx()
                    val top = view.bottom
                    val right = view.right - 8.toPx()
                    val bottom = view.bottom + intrinsicHeight
                    bounds = Rect(left, top, right, bottom)
                    draw(canvas)
                }
            }
        }
    }

    private fun ProductListAdapter.getNextData(nextItemPosition: Int) : Visitable<*>? {
        return if (nextItemPosition < itemCount) { itemList[nextItemPosition] } else null
    }

    private fun isAdapterPositionNotInRange(
        adapterPosition: Int,
        visitableAdapter: ProductListAdapter,
    ): Boolean {
        return adapterPosition == RecyclerView.NO_POSITION
            || adapterPosition >= visitableAdapter.itemCount
    }

    private fun canDrawListViewSeparator(
        currentData: Visitable<*>,
        view: View,
        nextData: Visitable<*>?
    ): Boolean {
        return currentData.isListViewExperiment()
            && view is ProductCardListView
            && nextData is ProductItemDataView
    }

    private fun Visitable<*>.isListViewExperiment(): Boolean {
        return this is ProductItemDataView
            && SearchConstant.ProductListType.LIST_VIEW == this.productListType
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
        if (isAdapterPositionNotInRange(childAdapterPosition, visitableAdapter)) return

        val currentData = visitableAdapter.itemList[childAdapterPosition]
        val nextItemPosition = childAdapterPosition + 1
        val nextData = visitableAdapter.getNextData(nextItemPosition)

        outRect.bottom = if (canDrawListViewSeparator(currentData, view, nextData)) {
            separatorDrawable.intrinsicHeight
        } else 0
    }
}
