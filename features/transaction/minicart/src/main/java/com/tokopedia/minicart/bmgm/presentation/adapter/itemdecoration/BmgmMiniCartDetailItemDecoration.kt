package com.tokopedia.minicart.bmgm.presentation.adapter.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created by @ilhamsuaib on 05/08/23.
 */

class BmgmMiniCartDetailItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val index = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount.orZero()
        val isLastItem = index == itemCount.minus(Int.ONE)

        if (isLastItem) {
            outRect.bottom = parent.context.dpToPx(16).toInt()
        }
    }
}