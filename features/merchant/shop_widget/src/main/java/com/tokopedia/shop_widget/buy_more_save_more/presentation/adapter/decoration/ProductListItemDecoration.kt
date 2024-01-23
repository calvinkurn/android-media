package com.tokopedia.shop_widget.buy_more_save_more.presentation.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.ZERO

class ProductListItemDecoration : RecyclerView.ItemDecoration() {

    companion object {
        private const val SPACING_16 = 16
        private const val SPACING_10 = 10
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
        if (spanIndex == Int.ZERO) {
            view.setPadding(SPACING_16, SPACING_16, SPACING_10, SPACING_16) //set left padding
        } else {

            view.setPadding(SPACING_10, SPACING_16, SPACING_16, SPACING_16) //set left padding
        }
    }
}
