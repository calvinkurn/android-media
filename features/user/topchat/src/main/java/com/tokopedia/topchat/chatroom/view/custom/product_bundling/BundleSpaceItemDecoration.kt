package com.tokopedia.topchat.chatroom.view.custom.product_bundling

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class BundleSpaceItemDecoration(
    private val spaceDp: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = spaceDp.toPx()
        outRect.left = spaceDp.toPx()
    }
}