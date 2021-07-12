package com.tokopedia.buyerorder.detail.view.adapter.divider

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker

class BuyerBundlingProductItemDivider(context: Context): RecyclerView.ItemDecoration() {

    private val divider = MethodChecker.getDrawable(context, com.tokopedia.abstraction.R.drawable.bg_line_separator_thin)
    private val padding = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.layout_lvl2).toInt()

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = padding
        val right = parent.width - padding
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

}