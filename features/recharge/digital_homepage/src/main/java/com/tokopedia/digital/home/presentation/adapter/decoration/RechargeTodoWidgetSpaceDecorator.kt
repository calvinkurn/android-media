package com.tokopedia.digital.home.presentation.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RechargeTodoWidgetSpaceDecorator (private val space: Int, private val isShownBayarSekaligus: Boolean) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter
        if (adapter != null && parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 0
            outRect.right = space
        } else {
            outRect.left = 0
            outRect.right = space
        }
    }

}
