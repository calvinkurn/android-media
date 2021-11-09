package com.tokopedia.product.detail.common.data.views

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.R
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Yehezkiel on 10/03/20
 */

class VariantItemDecorator(
        private val dividerDrawable: Drawable
) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.adapter?.apply {
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount
            for (i in 0 until childCount - 1) {
                val left = parent.context.resources.getDimensionPixelOffset(R.dimen.dp_16)
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + parent.context.resources.getDimensionPixelSize(R.dimen.dp_half)
                dividerDrawable.setBounds(left, top, right, bottom)
                dividerDrawable.draw(c)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (position != 0) {
            outRect.top = 16.toPx()
        }
    }
}