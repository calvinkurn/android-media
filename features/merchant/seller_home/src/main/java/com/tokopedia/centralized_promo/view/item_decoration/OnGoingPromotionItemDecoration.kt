package com.tokopedia.centralized_promo.view.item_decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class OnGoingPromotionItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) > 1) {
                top = margin
            }
            if (parent.getChildAdapterPosition(view) % 2 != 0) {
                left = margin
            }
            if (parent.getChildAdapterPosition(view) % 2 == 0) {
                right = margin
            }
            bottom = margin
        }
    }
}