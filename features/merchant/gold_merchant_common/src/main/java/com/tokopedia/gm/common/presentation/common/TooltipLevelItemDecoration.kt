package com.tokopedia.gm.common.presentation.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isOdd
import com.tokopedia.unifycomponents.toPx

/**
 * Created by @ilhamsuaib on 28/04/22.
 */

internal const val DECORATION_SPACE = 8

internal class TooltipLevelItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        parent.adapter.also {
            if (position.isOdd()) {
                outRect.left = DECORATION_SPACE.toPx()
            }
            outRect.top = DECORATION_SPACE.toPx()
        }
    }
}