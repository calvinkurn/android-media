package com.tokopedia.digital.home.presentation.adapter.adapter

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * Created by resakemal on 22/06/20.
 */
class RechargeItemProductCardsDecorator(val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.run {
            parent.let {
                // Add spacing to all items except last
                val childPosition = it.getChildAdapterPosition(view)
                if (childPosition < parent.childCount - 1) right = space
            }
        }

    }

}