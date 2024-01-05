package com.tokopedia.minicart.bmgm.presentation.adapter.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx

/**
 * Created by @ilhamsuaib on 05/08/23.
 */

class BmgmBundledProductItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val index = parent.getChildAdapterPosition(view)
        val isFirstItem = index == Int.ZERO

        if (!isFirstItem) {
            setSpacer(parent.context, outRect)
        }
    }

    private fun setSpacer(context: Context, outRect: Rect) {
        outRect.left = context.dpToPx(4).toInt()
    }
}