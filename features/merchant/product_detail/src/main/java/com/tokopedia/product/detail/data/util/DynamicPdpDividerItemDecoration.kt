package com.tokopedia.product.detail.data.util

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R

class DynamicPdpDividerItemDecoration(val context: Context) : RecyclerView.ItemDecoration() {

    val divider = MethodChecker.getDrawable(context, R.drawable.horizontal_line_divider)

    override
    fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        (0 until childCount).forEachIndexed { index, i ->
            val child = parent.getChildAt(index)
            val position = parent.getChildAdapterPosition(child)

            if (position != 0 && position < state.itemCount-1) {

                val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + divider.intrinsicHeight

                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
        }

    }
}