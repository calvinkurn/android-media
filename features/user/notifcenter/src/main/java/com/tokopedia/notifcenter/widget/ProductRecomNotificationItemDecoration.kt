package com.tokopedia.notifcenter.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toPx

class ProductRecomNotificationItemDecoration : RecyclerView.ItemDecoration() {

    val maxItem = 4
    val spacing = 8f.toPx().toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = (position + 1) % maxItem

        if (column != 0) {
            outRect.right = spacing
        }
    }


}