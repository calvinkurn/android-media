package com.tokopedia.home_component.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R

internal class FlashSaleCarouselDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return
        val itemCount = state.itemCount

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = view.context.resources.getDimensionPixelSize(R.dimen.home_flash_sale_left_margin)
        } else if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.right = view.context.resources.getDimensionPixelSize(R.dimen.home_flash_sale_right_margin)
        }
    }
}
