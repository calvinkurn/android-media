package com.tokopedia.loginregister.common.view.emailextension

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EmailExtensionDecoration(
        val space: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.set(space, 0, 0, 0)
        } else if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
            outRect.set(0, 0, space, 0)
        }
    }
}