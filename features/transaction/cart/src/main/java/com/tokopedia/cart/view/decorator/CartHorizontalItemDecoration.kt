package com.tokopedia.cart.view.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R

class CartHorizontalItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        if (position == 0) {
            outRect.left = parent.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
            outRect.right = parent.context?.resources?.getDimension(R.dimen.dp_4)?.toInt() ?: 0
        } else if (position == itemCount - 1) {
            outRect.right = parent.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
            outRect.left = parent.context?.resources?.getDimension(R.dimen.dp_4)?.toInt() ?: 0
        } else {
            outRect.right = parent.context?.resources?.getDimension(R.dimen.dp_4)?.toInt() ?: 0
            outRect.left = parent.context?.resources?.getDimension(R.dimen.dp_4)?.toInt() ?: 0
        }
    }
}
