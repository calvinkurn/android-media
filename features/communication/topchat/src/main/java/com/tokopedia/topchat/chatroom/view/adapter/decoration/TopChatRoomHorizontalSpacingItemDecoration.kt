package com.tokopedia.topchat.chatroom.view.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TopChatRoomHorizontalSpacingItemDecoration(
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        outRect.apply {
            when (position) {
                0 -> {
                    right = spacing
                }
                itemCount -> {
                    // Last Item
                    left = spacing
                }
                else -> {
                    left = spacing
                    right = spacing
                }
            }
        }
    }
}
