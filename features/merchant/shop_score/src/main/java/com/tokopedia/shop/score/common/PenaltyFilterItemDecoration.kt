package com.tokopedia.shop.score.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class PenaltyFilterItemDecoration : RecyclerView.ItemDecoration() {

    companion object {
        const val PADDING_ITEM = 8
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = PADDING_ITEM.toPx()
        outRect.top = PADDING_ITEM.toPx()
    }
}