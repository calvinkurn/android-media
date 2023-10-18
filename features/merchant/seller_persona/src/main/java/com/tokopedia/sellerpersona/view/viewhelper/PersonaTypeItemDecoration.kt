package com.tokopedia.sellerpersona.view.viewhelper

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created By @ilhamsuaib on 08/09/21
 */

class PersonaTypeItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val lastIndex = parent.adapter?.itemCount.orZero().minus(Int.ONE)
        val isLastItem = parent.getChildAdapterPosition(view) == lastIndex
        val isFirstIndex = parent.getChildAdapterPosition(view) == Int.ZERO
        val dp8 = view.context.resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.layout_lvl1
        )
        if (isFirstIndex) {
            outRect.left = dp8
        } else if (isLastItem) {
            outRect.right = dp8
        }
    }
}