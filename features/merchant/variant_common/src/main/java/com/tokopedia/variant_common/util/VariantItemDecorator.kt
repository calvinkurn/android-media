package com.tokopedia.variant_common.util

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.R

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

}