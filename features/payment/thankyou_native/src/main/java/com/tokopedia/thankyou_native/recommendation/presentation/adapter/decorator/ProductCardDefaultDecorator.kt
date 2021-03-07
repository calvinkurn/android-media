package com.tokopedia.thankyou_native.recommendation.presentation.adapter.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ProductCardDefaultDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return
        val itemCount = state.itemCount

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 0
        } else if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.right = view.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
        }
    }
}