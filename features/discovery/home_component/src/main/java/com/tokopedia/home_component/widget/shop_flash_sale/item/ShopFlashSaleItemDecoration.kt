package com.tokopedia.home_component.widget.shop_flash_sale.item

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt

/**
 * Created by frenzel
 */
internal object ShopFlashSaleItemDecoration : RecyclerView.ItemDecoration() {
    private const val FIRST_POSITION = 0
    private const val SINGLE_ITEM = 1
    private const val OUTSIDE_MARGIN = 12f
    private const val INNER_MARGIN = 2f

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (parent.getChildAdapterPosition(view)) {
            FIRST_POSITION -> {
                outRect.left = OUTSIDE_MARGIN.toDpInt()
                if(state.itemCount > SINGLE_ITEM) {
                    outRect.right = INNER_MARGIN.toDpInt()
                } else {
                    outRect.right = OUTSIDE_MARGIN.toDpInt()
                }
            }
            //last position of card
            state.itemCount - 1 -> {
                outRect.right = OUTSIDE_MARGIN.toDpInt()
                outRect.left = INNER_MARGIN.toDpInt()
            }
            //card between first and last
            else -> {
                outRect.right = INNER_MARGIN.toDpInt()
                outRect.left = INNER_MARGIN.toDpInt()
            }
        }
    }
}
