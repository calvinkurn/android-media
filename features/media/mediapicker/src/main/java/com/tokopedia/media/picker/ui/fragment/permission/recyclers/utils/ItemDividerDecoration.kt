package com.tokopedia.media.picker.ui.fragment.permission.recyclers.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.R

class ItemDividerDecoration(context: Context?) : RecyclerView.ItemDecoration() {

    private var divider: Drawable? = null
    private var dividerStartEndPadding = 0

    init {
        context?.let {
            dividerStartEndPadding = it.resources.getDimensionPixelSize(R.dimen.picker_item_permission_left_padding)
            divider = ContextCompat.getDrawable(it, R.drawable.bg_picker_permission_divider)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerStart = parent.paddingStart + dividerStartEndPadding
        val dividerEnd = (parent.width - parent.paddingEnd) - dividerStartEndPadding
        val childCount = parent.childCount

        for (i in 0..childCount - 2) {
            val view = parent.getChildAt(i)
            val layoutParams = view.layoutParams as RecyclerView.LayoutParams

            val dividerTop = view.bottom + layoutParams.bottomMargin
            val dividerBottom = dividerTop + (divider?.intrinsicHeight?: 0)

            divider?.setBounds(dividerStart, dividerTop, dividerEnd, dividerBottom)
            divider?.draw(c)
        }
    }

}