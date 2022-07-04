package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.toDpInt

/**
 * Created by dhaba
 */
class BalanceWidgetItemDecoration(private val spanCount: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {


        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 12f.toDpInt()
        }

        if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
            outRect.right = 12f.toDpInt()
        }
    }
}