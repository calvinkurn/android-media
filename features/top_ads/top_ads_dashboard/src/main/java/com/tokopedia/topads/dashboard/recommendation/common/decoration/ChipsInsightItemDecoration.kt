package com.tokopedia.topads.dashboard.recommendation.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.toPx

class ChipsInsightItemDecoration :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == Int.ZERO) {
            outRect.left = 8.toPx()
        }
        if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount
                ?: Int.ZERO) - Int.ONE
        ) {
            outRect.right = 8.toPx()
        }
    }
}
