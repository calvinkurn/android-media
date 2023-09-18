package com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE

class VerticalSpaceItemDecoration(
    private val verticalSpaceHeight: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemCount = parent.adapter?.itemCount?: 0
        val lastPosition = itemCount - Int.ONE
        if (parent.getChildAdapterPosition(view) != lastPosition) {
            outRect.bottom = verticalSpaceHeight
        } else {
            super.getItemOffsets(outRect, view, parent, state)
        }
    }
}
