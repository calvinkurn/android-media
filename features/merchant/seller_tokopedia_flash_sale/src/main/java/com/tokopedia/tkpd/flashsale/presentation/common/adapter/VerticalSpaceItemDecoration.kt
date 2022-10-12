package com.tokopedia.tkpd.flashsale.presentation.common.adapter

import android.graphics.Rect
import android.view.View

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.setMargin

class VerticalSpaceItemDecoration(private val spacingAmount: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        parent.adapter?.let {
            if (parent.getChildAdapterPosition(view) != it.itemCount.dec()) {
                outRect.bottom = spacingAmount
            }
        }
    }
}
