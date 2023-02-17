package com.tokopedia.autocompletecomponent.searchbar

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.R as RComponent

class SearchBarKeywordItemDecoration(
    context: Context
) : RecyclerView.ItemDecoration() {

    private val verticalOffset = context.resources.getDimensionPixelSize(RComponent.dimen.unify_space_8)
    private val horizontalEdgeOffset = context.resources.getDimensionPixelSize(RComponent.dimen.unify_space_16)
    private val horizontalOffset = context.resources.getDimensionPixelSize(RComponent.dimen.unify_space_4)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return
        val size = state.itemCount
        val isLastItem = itemPosition == size - 1
        if(itemPosition == 0) {
            outRect.left = horizontalEdgeOffset
            outRect.right = horizontalOffset
        } else if(itemPosition > 0 && isLastItem) {
            outRect.left = 0
            outRect.right = horizontalEdgeOffset
        } else {
            outRect.left = 0
            outRect.right = horizontalOffset
        }
        outRect.bottom = verticalOffset
        outRect.top = verticalOffset
    }
}
