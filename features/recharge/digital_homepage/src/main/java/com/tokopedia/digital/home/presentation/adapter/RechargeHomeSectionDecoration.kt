package com.tokopedia.digital.home.presentation.adapter

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * Created by resakemal on 24/06/20.
 */
class RechargeHomeSectionDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter
        if (adapter != null && parent.getChildAdapterPosition(view) != adapter.itemCount - 1) {
            outRect.bottom = space
        }
    }

}