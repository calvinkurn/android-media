package com.tokopedia.media.picker.ui.adapter.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.media.R
import com.tokopedia.media.picker.ui.adapter.PickerFragmentStateAdapter
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment
import com.tokopedia.picker.common.types.PageType

class PickerFragmentDecoration constructor(
    context: Context,
    @PageType private val pageType: Int
) : ItemDecoration() {

    private var margin = 0

    init {
        margin = context.resources.getDimensionPixelOffset(R.dimen.picker_page_margin_bottom)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.adapter !is PickerFragmentStateAdapter) return

        val adapter = parent.adapter as PickerFragmentStateAdapter
        val position = state.itemCount - 1

        // GalleryFragment have a special treatment for handling the page margin
        if (adapter.fragments[position] is GalleryFragment
            && parent.getChildAdapterPosition(view) == position) {
            // Add top margin for fix overlapping views issue with toolbar
            outRect.top = margin

            // The Gallery bottom margin only appear if the pageType is COMMON
            if (pageType == PageType.COMMON) {
                outRect.bottom = margin
            }
        }
    }
}
