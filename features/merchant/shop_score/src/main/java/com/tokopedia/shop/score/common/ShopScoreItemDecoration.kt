package com.tokopedia.shop.score.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isOdd
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx

class ShopScoreItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position > 0) {
            parent.adapter.also {
                outRect.left = 8.toPx()
            }
        }
    }
}

class TooltipLevelItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        parent.adapter.also {
            if (position.isOdd()) {
                outRect.left = 8.toPx()
            }
            outRect.top = 8.toPx()
        }
    }
}