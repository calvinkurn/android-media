package com.tokopedia.buyerorder.detail.view.adapter.divider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.orZero

class BuyerBundlingProductItemDivider(context: Context): RecyclerView.ItemDecoration() {

    private val divider: Drawable? = MethodChecker.getDrawable(context, com.tokopedia.abstraction.R.drawable.bg_line_separator_thin)
    private val padding = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.layout_lvl2).toInt()

    private val defaultMarginTop = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.spacing_lvl4).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        outRect.top =
                if (position == 0) {
                    0
                } else {
                    defaultMarginTop
                }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = padding
        val right = parent.width - padding
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + divider?.intrinsicHeight.orZero()
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }

}