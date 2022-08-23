package com.tokopedia.tokofood.feature.merchant.presentation.decoration

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.tokofood.R

class ProductOptionDividerDecoration(context: Context): RecyclerView.ItemDecoration() {

    private val divider = ContextCompat.getDrawable(context, R.drawable.divider_tokofood_product_option)

    private val dividerMarginLeft = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (index in Int.ONE until parent.childCount) {
            var left = dividerMarginLeft.toInt()
            val right = parent.width
            val top = parent.getChildAt(index).top
            val bottom = top + divider?.intrinsicHeight.toZeroIfNull()
            if (index == 0) {
                left = 0
            }
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }
}