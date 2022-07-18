package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt

/**
 * Created by dhaba
 */
class BalanceWidgetItemDecoration(private val spanCount: Int) : RecyclerView.ItemDecoration() {

    companion object {
        private const val BALANCE_WIDGET_FIRST_ITEM_POSITION = 0
        private const val BALANCE_2_ITEMS = 2
        private var MARGIN_LEFT_FIRST_ITEM = 10f.toDpInt()
        private var MARGIN_LEFT_SECOND_ITEM_2_ITEMS = 12f.toDpInt()
        private var MARGIN_LEFT_NEXT_ITEM_3_ITEMS = 8f.toDpInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == BALANCE_WIDGET_FIRST_ITEM_POSITION) {
            outRect.left = MARGIN_LEFT_FIRST_ITEM
        } else {
            if (spanCount == BALANCE_2_ITEMS) {
                outRect.left = MARGIN_LEFT_SECOND_ITEM_2_ITEMS
            } else {
                //BALANCE WIDGET 3 items
                outRect.left = MARGIN_LEFT_NEXT_ITEM_3_ITEMS
            }
        }
    }
}