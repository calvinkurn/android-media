package com.tokopedia.media.editor.ui.adapter.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.editor.R

class ThumbnailDrawerDecoration(context: Context?) : RecyclerView.ItemDecoration() {

    private var dividerStartEndPadding = 0

    init {
        context?.let {
            dividerStartEndPadding =
                it.resources.getDimensionPixelSize(R.dimen.editor_item_drawer_padding)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return

        val itemCount = state.itemCount

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = dividerStartEndPadding
        } else if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.right = dividerStartEndPadding
        }

    }

}